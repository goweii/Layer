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
    private final LayerLayout.OnConfigurationChangedListener mOnConfigurationChangedListener = new OnConfigurationChangedListenerImpl();

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
        LayerLayout layerLayout = findLayerLayoutFromRoot();
        if (layerLayout == null) layerLayout = tryGetLayerLayoutFormHolder();
        if (layerLayout == null) layerLayout = createLayerLayout();
        if (layerLayout.getParent() == null) {
            getViewHolder().getRoot().addView(layerLayout);
        } else if (layerLayout.getParent() != getViewHolder().getRoot()) {
            ((ViewGroup) layerLayout.getParent()).removeView(layerLayout);
            getViewHolder().getRoot().addView(layerLayout);
        }
        layerLayout.registerOnConfigurationChangedListener(mOnConfigurationChangedListener);
        LevelLayout levelLayout = findLevelLayoutFromLayerLayout(layerLayout);
        if (levelLayout == null) levelLayout = tryGetLevelLayoutFormHolder();
        if (levelLayout == null) levelLayout = createLevelLayout();
        if (levelLayout.getParent() == null) {
            layerLayout.addView(levelLayout);
        } else if (levelLayout.getParent() != layerLayout) {
            ((ViewGroup) levelLayout.getParent()).removeView(levelLayout);
            layerLayout.addView(levelLayout);
        }
        return levelLayout;
    }

    private void uninstallParent() {
        final LayerLayout layerLayout = findLayerLayoutFromRoot();
        if (layerLayout == null) return;
        layerLayout.unregisterOnConfigurationChangedListener(mOnConfigurationChangedListener);
        final LevelLayout levelLayout = findLevelLayoutFromLayerLayout(layerLayout);
        if (levelLayout == null) return;
        if (levelLayout.getChildCount() == 0) {
            layerLayout.removeView(levelLayout);
        }
        if (layerLayout.getChildCount() == 0) {
            getViewHolder().getRoot().removeView(layerLayout);
        }
    }

    private void ensureLayerLayoutIsFront() {
        final ViewGroup root = getViewHolder().getRoot();
        int count = root.getChildCount();
        if (count <= 1) return;
        LayerLayout layerLayout = findLayerLayoutFromRoot();
        if (layerLayout == null) return;
        int index = root.indexOfChild(layerLayout);
        if (index < 0) return;
        if (index == count - 1) return;
        layerLayout.bringToFront();
    }

    @Nullable
    private LayerLayout findLayerLayoutFromRoot() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerLayout layerLayout = null;
        final int count = root.getChildCount();
        for (int i = count; i >= 0; i--) {
            View child = root.getChildAt(i);
            if (child instanceof LayerLayout) {
                layerLayout = (LayerLayout) child;
                break;
            }
        }
        if (layerLayout != null) {
            if (layerLayout != getViewHolder().getLayerLayout()) {
                getViewHolder().setLayerLayout(layerLayout);
            }
        }
        return layerLayout;
    }

    @Nullable
    private LayerLayout tryGetLayerLayoutFormHolder() {
        if (getViewHolder().getLayerLayout() == null) return null;
        LayerLayout layerLayout = getViewHolder().getLayerLayout();
        Utils.removeViewParent(layerLayout);
        return layerLayout;
    }

    @NonNull
    private LayerLayout createLayerLayout() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerLayout layerLayout = new LayerLayout(root.getContext());
        layerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getViewHolder().setLayerLayout(layerLayout);
        return layerLayout;
    }

    @Nullable
    private LevelLayout findLevelLayoutFromLayerLayout(LayerLayout group) {
        LevelLayout levelLayout = group.findLevelLayout(getRealLevel());
        if (levelLayout != null) {
            if (levelLayout != getViewHolder().getLevelLayout()) {
                getViewHolder().setLevelLayout(levelLayout);
            }
        }
        return levelLayout;
    }

    @Nullable
    private LevelLayout tryGetLevelLayoutFormHolder() {
        if (getViewHolder().getLevelLayout() == null) {
            return null;
        }
        LevelLayout levelLayout = getViewHolder().getLevelLayout();
        if (levelLayout.mLevel != getRealLevel()) {
            getViewHolder().setLevelLayout(null);
            return null;

        }
        if (levelLayout.getParent() != null) {
            ((ViewGroup) levelLayout.getParent()).removeView(levelLayout);
        }
        return levelLayout;
    }

    @NonNull
    private LevelLayout createLevelLayout() {
        final ViewGroup root = getViewHolder().getRoot();
        LevelLayout levelLayout = new LevelLayout(root.getContext(), getRealLevel());
        levelLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getViewHolder().setLevelLayout(levelLayout);
        return levelLayout;
    }

    private class OnConfigurationChangedListenerImpl implements LayerLayout.OnConfigurationChangedListener {
        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            FrameLayer.this.onConfigurationChanged(newConfig);
        }
    }

    public static class ViewHolder extends Layer.ViewHolder {
        private FrameLayout mRoot;

        private LayerLayout mLayerLayout;
        private LevelLayout mLevelLayout;

        public void setRoot(@NonNull FrameLayout root) {
            mRoot = root;
        }

        @NonNull
        public FrameLayout getRoot() {
            return mRoot;
        }

        public void setLayerLayout(@Nullable LayerLayout layerLayout) {
            mLayerLayout = layerLayout;
        }

        @Nullable
        public LayerLayout getLayerLayout() {
            return mLayerLayout;
        }

        public void setLevelLayout(@Nullable LevelLayout levelLayout) {
            mLevelLayout = levelLayout;
        }

        @Nullable
        public LevelLayout getLevelLayout() {
            return mLevelLayout;
        }

        @NonNull
        @Override
        public LevelLayout getParent() {
            return (LevelLayout) super.getParent();
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
    protected static class Level {
        public static final int GUIDE = 1000;
        public static final int POPUP = 2000;
        public static final int DIALOG = 3000;
        public static final int OVERLAY = 4000;
        public static final int NOTIFICATION = 5000;
        public static final int TOAST = 6000;

        public static boolean isFirstTopThanSecond(int first, int second) {
            return first > second;
        }
    }

    /**
     * 各个层级浮层的容器，直接添加进DecorView
     */
    @SuppressLint("ViewConstructor")
    public static class LayerLayout extends FrameLayout {
        private final LinkedList<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new LinkedList<>();

        public LayerLayout(@NonNull Context context) {
            super(context);
        }

        @Nullable
        public LevelLayout findLevelLayout(int level) {
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child instanceof LevelLayout) {
                    LevelLayout levelLayout = (LevelLayout) child;
                    if (level == levelLayout.getLevel()) {
                        return levelLayout;
                    }
                }
            }
            return null;
        }

        @Override
        public void addView(View child, int index, ViewGroup.LayoutParams params) {
            if (!(child instanceof LevelLayout)) {
                super.addView(child, index, params);
                return;
            }
            int lastIndex = -1;
            LevelLayout levelLayout = (LevelLayout) child;
            final int count = getChildCount();
            for (int i = 0; i < count; i++) {
                lastIndex = i;
                View iChild = getChildAt(i);
                if (iChild instanceof LevelLayout) {
                    LevelLayout iLevelLayout = (LevelLayout) iChild;
                    int compare = levelLayout.compareTo(iLevelLayout);
                    if (compare == 0) {
                        throw new RuntimeException("已经存在相同level：" + levelLayout.mLevel + "的LevelLayout");
                    } else if (compare < 0) {
                        lastIndex--;
                        break;
                    }
                }
            }
            super.addView(levelLayout, lastIndex + 1, params);
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
    public static class LevelLayout extends FrameLayout implements Comparable<LevelLayout> {
        private final int mLevel;

        public LevelLayout(@NonNull Context context, int level) {
            super(context);
            mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }

        public boolean isTopThan(@NonNull LevelLayout other) {
            return Level.isFirstTopThanSecond(mLevel, other.mLevel);
        }

        @Override
        public int compareTo(LevelLayout o) {
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
