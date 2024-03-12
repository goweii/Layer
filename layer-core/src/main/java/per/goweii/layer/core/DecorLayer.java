package per.goweii.layer.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import per.goweii.layer.core.listener.WindowCallbackDelegate;
import per.goweii.layer.core.utils.Utils;

/**
 * 父容器是 Activity 的根 view，即 DecorView 。
 */
public class DecorLayer extends FrameLayer {
    private final Activity mActivity;

    private final Rect mInsets = new Rect();
    private final Rect mTempRect = new Rect();

    private Runnable mShowRunnable = null;

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

    @NonNull
    @Override
    protected LayerRootLayout createLayerRootLayout() {
        final LayerRootLayout layerRootLayout = super.createLayerRootLayout();
        layerRootLayout.addOnAttachStateChangeListener(new LayerRootLayoutOnAttachStateChangeListenerImpl(getActivity(), layerRootLayout));
        return layerRootLayout;
    }

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        getDecorInsets(mInsets);
        fitDecorInsets(mInsets);
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(getViewHolder().getChild(), null);
        }
    }

    @Override
    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.onViewLayout(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                if (!mActivity.isDestroyed() && isShown()) {
                    getDecorInsets(mInsets);
                    fitDecorInsets(mInsets);
                }
            }
        });
    }

    @Override
    protected void onGlobalLayout() {
        super.onGlobalLayout();
        if (!mActivity.isDestroyed() && isShown()) {
            getDecorInsets(mInsets);
            fitDecorInsets(mInsets);
        }
    }

    protected final void getDecorInsets(@NonNull Rect insets) {
        insets.setEmpty();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(getViewHolder().getDecor());
            if (windowInsets != null) {
                Insets realInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime() | WindowInsetsCompat.Type.displayCutout());
                insets.set(realInsets.left, realInsets.top, realInsets.right, realInsets.bottom);
            }
        } else {
            Utils.getViewMargin(getViewHolder().getDecorChild(), mTempRect);
            insets.set(mTempRect);
            Utils.getViewPadding(getViewHolder().getDecorChild(), mTempRect);
            insets.left += mTempRect.left;
            insets.top += mTempRect.top;
            insets.right += mTempRect.right;
            insets.bottom += mTempRect.bottom;
            int statusBarHeightIfVisible = Utils.getStatusBarHeightIfVisible(getActivity());
            if (insets.top < statusBarHeightIfVisible) {
                insets.top = statusBarHeightIfVisible;
            }
        }
    }

    protected void fitDecorInsets(@NonNull Rect insets) {
        getViewHolder().getParent().setClipToPadding(false);
        getViewHolder().getParent().setClipChildren(false);
        Utils.setViewPadding(getViewHolder().getParent(), insets);
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

    @NonNull
    @Override
    public Layer setInterceptKeyEvent(boolean intercept) {
        getConfig().mInterceptKeyEventDispatch = intercept;
        return super.setInterceptKeyEvent(false);
    }

    private static class LayerRootLayoutOnAttachStateChangeListenerImpl implements View.OnAttachStateChangeListener {
        private final Activity mActivity;
        private final LayerRootLayout mLayerRootLayout;

        private WindowCallbackDelegate mWindowCallbackDelegate = null;
        private Window.Callback mOldWindowCallback = null;

        private LayerRootLayoutOnAttachStateChangeListenerImpl(@NonNull Activity activity, @NonNull LayerRootLayout layerRootLayout) {
            mActivity = activity;
            mLayerRootLayout = layerRootLayout;
        }

        @Override
        public void onViewAttachedToWindow(View v) {
            final Window window = mActivity.getWindow();
            mOldWindowCallback = window.getCallback();
            mWindowCallbackDelegate = new WindowCallbackDelegateImpl(mOldWindowCallback, mLayerRootLayout);
            window.setCallback(mWindowCallbackDelegate);
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            final Window window = mActivity.getWindow();
            window.setCallback(mOldWindowCallback);
            mOldWindowCallback = null;
            mWindowCallbackDelegate = null;
        }
    }

    private static class WindowCallbackDelegateImpl extends WindowCallbackDelegate {
        private final LayerRootLayout mLayerRootLayout;

        public WindowCallbackDelegateImpl(@NonNull Window.Callback callback,
                                          @NonNull LayerRootLayout layerRootLayout) {
            super(callback);
            mLayerRootLayout = layerRootLayout;
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (mLayerRootLayout.dispatchKeyEventFromWindow(event)) {
                return true;
            }
            return super.dispatchKeyEvent(event);
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
        private boolean mInterceptKeyEventDispatch = false;
    }

    protected static class ListenerHolder extends FrameLayer.ListenerHolder {
    }
}
