package per.goweii.layer.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import per.goweii.layer.core.utils.Utils;

/**
 * 父容器是 FrameLayout 的浮层。
 * 内部会根据 {@link Level} 来控制浮层显示的前后关系。
 */
public class FrameLayer extends Layer {
    /**
     * 根据子 view 获取所有已显示的 FrameLayer
     *
     * @param view 子视图
     * @return 所有已显示的 FrameLayer
     */
    @Nullable
    public static List<Layer> findLayers(@NonNull View view) {
        LayerRootLayout layerRootLayout = findLayerRootLayout(view);
        if (layerRootLayout == null) return null;
        return layerRootLayout.getLayers();
    }

    /**
     * 根据子视图获取 LayerRootLayout
     *
     * @param view 子视图
     * @return LayerRootLayout
     */
    @Nullable
    public static LayerRootLayout findLayerRootLayout(@NonNull View view) {
        while (true) {
            if (view instanceof LayerRootLayout) {
                return (LayerRootLayout) view;
            }
            ViewParent viewParent = view.getParent();
            if (viewParent instanceof ViewGroup) {
                view = (ViewGroup) viewParent;
                continue;
            }
            return null;
        }
    }

    private final LayerRootLayout.OnConfigurationChangedListener
            mOnConfigurationChangedListener = new OnConfigurationChangedListenerImpl();

    public FrameLayer(@NonNull FrameLayout frameLayout) {
        super();
        getViewHolder().setRoot(frameLayout);
    }

    @IntRange(from = 0)
    protected int getLevel() {
        return 0;
    }

    @IntRange(from = 0)
    protected final int getRealLevel() {
        if (getConfig().mLevel >= 0) {
            return getConfig().mLevel;
        }
        return getLevel();
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @NonNull
    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @NonNull
    @Override
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

    @NonNull
    @Override
    protected ViewGroup onGetParent() {
        return installParent();
    }

    @Override
    protected void onResetParent() {
        uninstallParent();
    }

    /**
     * 配置变更回调，比如：暗亮色/语言/屏幕方向。
     *
     * @param newConfig 新配置
     */
    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
    }

    @Override
    protected void onGlobalLayout() {
        super.onGlobalLayout();
        // 确保浮层处于最前面
        ensureLayerLayoutIsFront();
    }

    @NonNull
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    public FrameLayer setLevel(int level) {
        getConfig().mLevel = level;
        return this;
    }

    @NonNull
    public FrameLayer setCancelableOnClickKeyBack(boolean cancelable) {
        setCancelableOnKeyBack(cancelable);
        return this;
    }

    /**
     * 构建带有层级关系的直接父容器 {@link LayerLevelLayout}
     *
     * @return 直接父容器 LayerLevelLayout
     */
    @NonNull
    protected ViewGroup installParent() {
        LayerRootLayout layerRootLayout = findLayerRootLayoutFromRoot();
        if (layerRootLayout == null) layerRootLayout = tryGetLayerRootLayoutFormHolder();
        if (layerRootLayout == null) layerRootLayout = createLayerRootLayout();
        if (layerRootLayout.getParent() == null) {
            getViewHolder().getRoot().addView(layerRootLayout);
        } else if (layerRootLayout.getParent() != getViewHolder().getRoot()) {
            ((ViewGroup) layerRootLayout.getParent()).removeView(layerRootLayout);
            getViewHolder().getRoot().addView(layerRootLayout);
        }
        layerRootLayout.registerOnConfigurationChangedListener(mOnConfigurationChangedListener);
        LayerLevelLayout layerLevelLayout = findLayerLevelLayoutFromLayerLayout(layerRootLayout);
        if (layerLevelLayout == null) layerLevelLayout = tryGetLayerLevelLayoutFormHolder();
        if (layerLevelLayout == null) layerLevelLayout = createLayerLevelLayout();
        if (layerLevelLayout.getParent() == null) {
            layerRootLayout.addView(layerLevelLayout);
        } else if (layerLevelLayout.getParent() != layerRootLayout) {
            ((ViewGroup) layerLevelLayout.getParent()).removeView(layerLevelLayout);
            layerRootLayout.addView(layerLevelLayout);
        }
        return layerLevelLayout;
    }

    /**
     * 回收直接父容器
     */
    protected void uninstallParent() {
        final LayerRootLayout layerRootLayout = findLayerRootLayoutFromRoot();
        if (layerRootLayout == null) return;
        layerRootLayout.unregisterOnConfigurationChangedListener(mOnConfigurationChangedListener);
        final LayerLevelLayout layerLevelLayout = findLayerLevelLayoutFromLayerLayout(layerRootLayout);
        if (layerLevelLayout == null) return;
        if (layerLevelLayout.getChildCount() == 0) {
            layerRootLayout.removeView(layerLevelLayout);
        }
        if (layerRootLayout.getChildCount() == 0) {
            getViewHolder().getRoot().removeView(layerRootLayout);
        }
    }

    private void ensureLayerLayoutIsFront() {
        final ViewGroup root = getViewHolder().getRoot();
        int count = root.getChildCount();
        if (count <= 1) return;
        LayerRootLayout layerRootLayout = findLayerRootLayoutFromRoot();
        if (layerRootLayout == null) return;
        int index = root.indexOfChild(layerRootLayout);
        if (index < 0) return;
        if (index == count - 1) return;
        layerRootLayout.bringToFront();
    }

    @Nullable
    protected LayerRootLayout findLayerRootLayoutFromRoot() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerRootLayout layerRootLayout = null;
        final int count = root.getChildCount();
        for (int i = count; i >= 0; i--) {
            View child = root.getChildAt(i);
            if (child instanceof LayerRootLayout) {
                layerRootLayout = (LayerRootLayout) child;
                break;
            }
        }
        if (layerRootLayout != null) {
            if (layerRootLayout != getViewHolder().getLayerRootLayout()) {
                getViewHolder().setLayerRootLayout(layerRootLayout);
            }
        }
        return layerRootLayout;
    }

    @Nullable
    protected LayerRootLayout tryGetLayerRootLayoutFormHolder() {
        if (getViewHolder().getLayerRootLayout() == null) return null;
        LayerRootLayout layerRootLayout = getViewHolder().getLayerRootLayout();
        Utils.removeViewParent(layerRootLayout);
        return layerRootLayout;
    }

    @NonNull
    protected LayerRootLayout createLayerRootLayout() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerRootLayout layerRootLayout = new LayerRootLayout(root.getContext());
        layerRootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getViewHolder().setLayerRootLayout(layerRootLayout);
        return layerRootLayout;
    }

    @Nullable
    protected LayerLevelLayout findLayerLevelLayoutFromLayerLayout(@NonNull LayerRootLayout group) {
        LayerLevelLayout layerLevelLayout = group.findLevelLayout(getRealLevel());
        if (layerLevelLayout != null) {
            if (layerLevelLayout != getViewHolder().getLayerLevelLayout()) {
                getViewHolder().setLayerLevelLayout(layerLevelLayout);
            }
        }
        return layerLevelLayout;
    }

    @Nullable
    protected LayerLevelLayout tryGetLayerLevelLayoutFormHolder() {
        if (getViewHolder().getLayerLevelLayout() == null) {
            return null;
        }
        LayerLevelLayout layerLevelLayout = getViewHolder().getLayerLevelLayout();
        if (layerLevelLayout.mLevel != getRealLevel()) {
            getViewHolder().setLayerLevelLayout(null);
            return null;

        }
        if (layerLevelLayout.getParent() != null) {
            ((ViewGroup) layerLevelLayout.getParent()).removeView(layerLevelLayout);
        }
        return layerLevelLayout;
    }

    @NonNull
    protected LayerLevelLayout createLayerLevelLayout() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerLevelLayout layerLevelLayout = new LayerLevelLayout(root.getContext(), getRealLevel());
        layerLevelLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getViewHolder().setLayerLevelLayout(layerLevelLayout);
        return layerLevelLayout;
    }

    private class OnConfigurationChangedListenerImpl implements LayerRootLayout.OnConfigurationChangedListener {
        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            FrameLayer.this.onConfigurationChanged(newConfig);
        }
    }

    public static class ViewHolder extends Layer.ViewHolder {
        private FrameLayout mRoot;

        private LayerRootLayout mLayerRootLayout;
        private LayerLevelLayout mLayerLevelLayout;

        public void setRoot(@NonNull FrameLayout root) {
            mRoot = root;
        }

        @NonNull
        public FrameLayout getRoot() {
            return mRoot;
        }

        public void setLayerRootLayout(@Nullable LayerRootLayout layerRootLayout) {
            mLayerRootLayout = layerRootLayout;
        }

        @Nullable
        public LayerRootLayout getLayerRootLayout() {
            return mLayerRootLayout;
        }

        public void setLayerLevelLayout(@Nullable LayerLevelLayout layerLevelLayout) {
            mLayerLevelLayout = layerLevelLayout;
        }

        @Nullable
        public LayerLevelLayout getLayerLevelLayout() {
            return mLayerLevelLayout;
        }

        @NonNull
        @Override
        public LayerLevelLayout getParent() {
            return (LayerLevelLayout) super.getParent();
        }
    }

    protected static class Config extends Layer.Config {
        protected int mLevel = -1;
    }

    protected static class ListenerHolder extends Layer.ListenerHolder {
    }

    /**
     * 浮层层级
     * 数字越大层级越高，显示在越上层
     */
    public static class Level {
        public static final int GUIDE = 1000;
        public static final int POPUP = 2000;
        public static final int DIALOG = 3000;
        public static final int OVERLAY = 4000;
        public static final int NOTIFICATION = 5000;
        public static final int TOAST = 6000;
    }

    /**
     * 各个层级浮层的容器，直接添加进在 {@link DecorLayer} 中会被直接添加进 DecorView 。
     */
    @SuppressLint("ViewConstructor")
    public static class LayerRootLayout extends FrameLayout {
        private final LinkedList<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new LinkedList<>();

        public LayerRootLayout(@NonNull Context context) {
            super(context);
        }

        public boolean dispatchKeyEventFromWindow(@NonNull KeyEvent event) {
            for (Layer layer : getLayers()) {
                if (layer.onKeyEvent(event)) {
                    return true;
                }
                final View child = layer.getViewHolder().getChildOrNull();
                if (child != null && child.dispatchKeyEvent(event)) {
                    return true;
                }
            }
            return false;
        }

        @Nullable
        public LayerLevelLayout findLevelLayout(int level) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                final View child = getChildAt(i);
                if (child instanceof LayerLevelLayout) {
                    final LayerLevelLayout layerLevelLayout = (LayerLevelLayout) child;
                    if (level == layerLevelLayout.getLevel()) {
                        return layerLevelLayout;
                    }
                }
            }
            return null;
        }

        @Override
        public void addView(View child, int index, ViewGroup.LayoutParams params) {
            if (!(child instanceof LayerLevelLayout)) {
                super.addView(child, index, params);
                return;
            }
            int lastIndex = -1;
            LayerLevelLayout layerLevelLayout = (LayerLevelLayout) child;
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                lastIndex = i;
                View iChild = getChildAt(i);
                if (iChild instanceof LayerLevelLayout) {
                    LayerLevelLayout iLayerLevelLayout = (LayerLevelLayout) iChild;
                    int compare = layerLevelLayout.compareTo(iLayerLevelLayout);
                    if (compare == 0) {
                        throw new RuntimeException("已经存在相同level：" + layerLevelLayout.mLevel + "的LevelLayout");
                    } else if (compare < 0) {
                        lastIndex--;
                        break;
                    }
                }
            }
            super.addView(layerLevelLayout, lastIndex + 1, params);
        }

        @Override
        protected void onConfigurationChanged(@NonNull Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            for (OnConfigurationChangedListener listener : mOnConfigurationChangedListeners) {
                listener.onConfigurationChanged(newConfig);
            }
        }

        protected void registerOnConfigurationChangedListener(@NonNull OnConfigurationChangedListener listener) {
            mOnConfigurationChangedListeners.add(listener);
        }

        protected void unregisterOnConfigurationChangedListener(@NonNull OnConfigurationChangedListener listener) {
            mOnConfigurationChangedListeners.remove(listener);
        }

        @Nullable
        public Layer getTopLayer() {
            final int childCount = getChildCount();
            if (childCount > 0) {
                final View view = getChildAt(childCount - 1);
                if (view instanceof LayerLevelLayout) {
                    final LayerLevelLayout layerLevelLayout = (LayerLevelLayout) view;
                    return layerLevelLayout.getTopLayer();
                }
            }
            return null;
        }

        @NonNull
        public List<Layer> getLayers() {
            final List<Layer> layers = new LinkedList<>();
            for (int i = getChildCount() - 1; i >= 0; i--) {
                final View view = getChildAt(i);
                if (view instanceof LayerLevelLayout) {
                    final LayerLevelLayout layerLevelLayout = (LayerLevelLayout) view;
                    layers.addAll(layerLevelLayout.getLayers());
                }
            }
            return layers;
        }

        public interface OnConfigurationChangedListener {
            void onConfigurationChanged(@NonNull Configuration newConfig);
        }
    }

    /**
     * 控制浮层上下层级的容器
     */
    @SuppressLint("ViewConstructor")
    public static class LayerLevelLayout extends FrameLayout implements Comparable<LayerLevelLayout> {
        private final int mLevel;

        public LayerLevelLayout(@NonNull Context context, int level) {
            super(context);
            mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }

        @Override
        public int compareTo(LayerLevelLayout o) {
            final int mOtherLevel;
            if (o != null) {
                mOtherLevel = o.mLevel;
            } else {
                mOtherLevel = -1;
            }
            return mLevel - mOtherLevel;
        }

        @Nullable
        public Layer getTopLayer() {
            final int childCount = getChildCount();
            if (childCount > 0) {
                final View view = getChildAt(childCount - 1);
                final Object tag = view.getTag(R.id.layer_tag);
                if (tag instanceof Layer) {
                    return (Layer) tag;
                }
            }
            return null;
        }

        @NonNull
        public List<Layer> getLayers() {
            final List<Layer> layers = new LinkedList<>();
            for (int i = getChildCount() - 1; i >= 0; i--) {
                final View view = getChildAt(i);
                final Object tag = view.getTag(R.id.layer_tag);
                if (tag instanceof Layer) {
                    final Layer layer = (Layer) tag;
                    layers.add(layer);
                }
            }
            return layers;
        }
    }
}
