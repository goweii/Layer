package per.goweii.layer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

import per.goweii.layer.utils.Utils;

public class FrameLayer extends Layer {
    private final LayerRootLayout.OnConfigurationChangedListener mOnConfigurationChangedListener = new OnConfigurationChangedListenerImpl();

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

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
    }

    @CallSuper
    @Override
    protected void onPreShow() {
        super.onPreShow();
    }

    @CallSuper
    @Override
    protected void onPostShow() {
        super.onPostShow();
    }

    @CallSuper
    @Override
    protected void onPreDismiss() {
        super.onPreDismiss();
    }

    @CallSuper
    @Override
    protected void onPostDismiss() {
        super.onPostDismiss();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
        uninstallParent();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        getViewHolder().setParent(null);
    }

    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
    }

    @Override
    protected void onGlobalLayout() {
        super.onGlobalLayout();
        ensureLayerLayoutIsFront();
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

    @NonNull
    private ViewGroup installParent() {
        LayerRootLayout layerRootLayout = findLayerLayoutFromRoot();
        if (layerRootLayout == null) layerRootLayout = tryGetLayerLayoutFormHolder();
        if (layerRootLayout == null) layerRootLayout = createLayerLayout();
        if (layerRootLayout.getParent() == null) {
            getViewHolder().getRoot().addView(layerRootLayout);
        } else if (layerRootLayout.getParent() != getViewHolder().getRoot()) {
            ((ViewGroup) layerRootLayout.getParent()).removeView(layerRootLayout);
            getViewHolder().getRoot().addView(layerRootLayout);
        }
        layerRootLayout.registerOnConfigurationChangedListener(mOnConfigurationChangedListener);
        LayerLevelLayout layerLevelLayout = findLevelLayoutFromLayerLayout(layerRootLayout);
        if (layerLevelLayout == null) layerLevelLayout = tryGetLevelLayoutFormHolder();
        if (layerLevelLayout == null) layerLevelLayout = createLevelLayout();
        if (layerLevelLayout.getParent() == null) {
            layerRootLayout.addView(layerLevelLayout);
        } else if (layerLevelLayout.getParent() != layerRootLayout) {
            ((ViewGroup) layerLevelLayout.getParent()).removeView(layerLevelLayout);
            layerRootLayout.addView(layerLevelLayout);
        }
        return layerLevelLayout;
    }

    private void uninstallParent() {
        final LayerRootLayout layerRootLayout = findLayerLayoutFromRoot();
        if (layerRootLayout == null) return;
        layerRootLayout.unregisterOnConfigurationChangedListener(mOnConfigurationChangedListener);
        final LayerLevelLayout layerLevelLayout = findLevelLayoutFromLayerLayout(layerRootLayout);
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
        LayerRootLayout layerRootLayout = findLayerLayoutFromRoot();
        if (layerRootLayout == null) return;
        int index = root.indexOfChild(layerRootLayout);
        if (index < 0) return;
        if (index == count - 1) return;
        layerRootLayout.bringToFront();
    }

    @Nullable
    private LayerRootLayout findLayerLayoutFromRoot() {
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
    private LayerRootLayout tryGetLayerLayoutFormHolder() {
        if (getViewHolder().getLayerRootLayout() == null) return null;
        LayerRootLayout layerRootLayout = getViewHolder().getLayerRootLayout();
        Utils.removeViewParent(layerRootLayout);
        return layerRootLayout;
    }

    @NonNull
    private LayerRootLayout createLayerLayout() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerRootLayout layerRootLayout = new LayerRootLayout(root.getContext());
        layerRootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getViewHolder().setLayerRootLayout(layerRootLayout);
        return layerRootLayout;
    }

    @Nullable
    private LayerLevelLayout findLevelLayoutFromLayerLayout(LayerRootLayout group) {
        LayerLevelLayout layerLevelLayout = group.findLevelLayout(getRealLevel());
        if (layerLevelLayout != null) {
            if (layerLevelLayout != getViewHolder().getLayerLevelLayout()) {
                getViewHolder().setLayerLevelLayout(layerLevelLayout);
            }
        }
        return layerLevelLayout;
    }

    @Nullable
    private LayerLevelLayout tryGetLevelLayoutFormHolder() {
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
    private LayerLevelLayout createLevelLayout() {
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
     * 各个层级浮层的容器，直接添加进DecorView
     */
    @SuppressLint("ViewConstructor")
    public static class LayerRootLayout extends FrameLayout {
        private final LinkedList<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new LinkedList<>();

        public LayerRootLayout(@NonNull Context context) {
            super(context);
        }

        @Nullable
        public LayerLevelLayout findLevelLayout(int level) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child instanceof LayerLevelLayout) {
                    LayerLevelLayout layerLevelLayout = (LayerLevelLayout) child;
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
    }
}
