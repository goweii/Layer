package per.goweii.layer.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import per.goweii.layer.core.utils.Utils;

public class DecorLayer extends FrameLayer {
    private final Activity mActivity;

    private final Rect mInsets = new Rect();
    private final Rect mTempRect = new Rect();

    private Runnable mShowRunnable = null;
    private WindowInsetsChangedListener mWindowInsetsChangedListener = null;

    public DecorLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public DecorLayer(@NonNull Activity activity) {
        super((FrameLayout) activity.getWindow().getDecorView());
        mActivity = activity;
    }

    @NonNull
    public Activity getActivity() {
        return mActivity;
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
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mActivity);
    }

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        Rect decorInsets = getDecorInsets();
        fitDecorInsets(decorInsets);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getActivity().getWindow().getDecorView());
            if (windowInsetsController != null) {
                if (mWindowInsetsChangedListener == null) {
                    mWindowInsetsChangedListener = new WindowInsetsChangedListener();
                }
                windowInsetsController.addOnControllableInsetsChangedListener(mWindowInsetsChangedListener);
            }
        }
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getActivity().getWindow().getDecorView());
            if (windowInsetsController != null && mWindowInsetsChangedListener != null) {
                windowInsetsController.removeOnControllableInsetsChangedListener(mWindowInsetsChangedListener);
            }
        }
    }

    @Override
    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.onViewLayout(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                Rect decorInsets = getDecorInsets();
                fitDecorInsets(decorInsets);
            }
        });
    }

    protected void fitDecorInsets(@NonNull Rect insets) {
        getViewHolder().getParent().setClipToPadding(false);
        getViewHolder().getParent().setClipChildren(false);
        Utils.setViewPadding(getViewHolder().getParent(), insets);
    }

    @NonNull
    protected final Rect getDecorInsets() {
        mInsets.setEmpty();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(getActivity().getWindow().getDecorView());
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            mInsets.set(insets.left, insets.top, insets.right, insets.bottom);
        } else {
            Utils.getViewMargin(getViewHolder().getDecorChild(), mTempRect);
            mInsets.set(mTempRect);
            Utils.getViewPadding(getViewHolder().getDecorChild(), mTempRect);
            mInsets.left += mTempRect.left;
            mInsets.top += mTempRect.top;
            mInsets.right += mTempRect.right;
            mInsets.bottom += mTempRect.bottom;
            int statusBarHeightIfVisible = Utils.getStatusBarHeightIfVisible(getActivity());
            if (mInsets.top < statusBarHeightIfVisible) {
                mInsets.top = statusBarHeightIfVisible;
            }
        }
        return mInsets;
    }

    public void showImmediately(boolean withAnim) {
        if (mShowRunnable != null) {
            getViewHolder().getDecor().removeCallbacks(mShowRunnable);
            mShowRunnable = null;
        }
        super.show(withAnim);
    }

    @Override
    public void show(final boolean withAnim) {
        if (mShowRunnable == null) {
            mShowRunnable = new Runnable() {
                @Override
                public void run() {
                    mShowRunnable = null;
                    DecorLayer.super.show(withAnim);
                }
            };
            getViewHolder().getDecor().post(mShowRunnable);
        }
    }

    @Override
    public void dismiss(boolean withAnim) {
        if (mShowRunnable != null) {
            getViewHolder().getDecor().removeCallbacks(mShowRunnable);
            mShowRunnable = null;
        } else {
            super.dismiss(withAnim);
        }
    }

    private class WindowInsetsChangedListener implements WindowInsetsControllerCompat.OnControllableInsetsChangedListener {
        @Override
        public void onControllableInsetsChanged(@NonNull WindowInsetsControllerCompat controller, int typeMask) {
            if (isShown()) {
                Rect decorInsets = getDecorInsets();
                fitDecorInsets(decorInsets);
            }
        }
    }

    public static class ViewHolder extends FrameLayer.ViewHolder {
        private FrameLayout mActivityContent;
        private View mDecorChild;

        @Override
        public void setRoot(@NonNull FrameLayout root) {
            super.setRoot(root);
            mDecorChild = root.getChildAt(0);
            mActivityContent = root.findViewById(android.R.id.content);
        }

        @NonNull
        public FrameLayout getDecor() {
            return getRoot();
        }

        @NonNull
        public View getDecorChild() {
            return mDecorChild;
        }

        @NonNull
        public FrameLayout getActivityContent() {
            return mActivityContent;
        }
    }

    protected static class Config extends FrameLayer.Config {
    }

    protected static class ListenerHolder extends FrameLayer.ListenerHolder {
    }
}
