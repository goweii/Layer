package per.goweii.layer.overlay;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.layer.core.DecorLayer;
import per.goweii.layer.core.anim.AnimatorHelper;
import per.goweii.layer.core.utils.Utils;

public class OverlayLayer extends DecorLayer {

    public OverlayLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public OverlayLayer(@NonNull Activity activity) {
        super(activity);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.OVERLAY;
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

    @Override
    public void show() {
        super.show();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        DragLayout container = new DragLayout(getActivity());
        if (getViewHolder().getOverlayNullable() == null) {
            getViewHolder().setOverlay(onCreateOverlay(inflater, container));
        }
        View overlay = getViewHolder().getOverlay();
        container.addView(overlay);
        return container;
    }

    @NonNull
    protected View onCreateOverlay(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view;
        if (getConfig().mOverlayView != null) {
            view = getConfig().mOverlayView;
        } else {
            view = inflater.inflate(getConfig().mOverlayViewId, parent, false);
        }
        Utils.removeViewParent(view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        FrameLayout.LayoutParams overlayParams;
        if (layoutParams == null) {
            overlayParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            overlayParams = (FrameLayout.LayoutParams) layoutParams;
        } else {
            overlayParams = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
        }
        view.setLayoutParams(overlayParams);
        return view;
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaOutAnim(view);
    }

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        initDragLayout();
        initOverlayView();
    }

    @CallSuper
    @Override
    protected void onPreShow() {
        super.onPreShow();
        initOverlayViewDefConfig();
        toNormal();
    }

    @CallSuper
    @Override
    protected void onPostShow() {
        super.onPostShow();
        getViewHolder().getChild().goEdge(getViewHolder().getOverlay());
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
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDragLayout() {
        final Config config = getConfig();
        final DragLayout dragLayout = getViewHolder().getChild();
        dragLayout.setPadding(config.mPaddingLeft, config.mPaddingTop, config.mPaddingRight, config.mPaddingBottom);
        dragLayout.setOutside(config.mOutside);
        dragLayout.setSnapEdge(config.mSnapEdge);
        dragLayout.setOnDragListener(new OverlayDragListener());
    }

    private void initOverlayView() {
        final Config config = getConfig();
        final View overlayView = getViewHolder().getOverlay();
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) overlayView.getLayoutParams();
        if (config.mMarginLeft != null) {
            params.leftMargin = config.mMarginLeft;
        }
        if (config.mMarginTop != null) {
            params.topMargin = config.mMarginTop;
        }
        if (config.mMarginRight != null) {
            params.rightMargin = config.mMarginRight;
        }
        if (config.mMarginBottom != null) {
            params.bottomMargin = config.mMarginBottom;
        }
        getListenerHolder().bindTouchListener(this);
    }

    private void initOverlayViewDefConfig() {
        final Config config = getConfig();
        final DragLayout dragLayout = getViewHolder().getChild();
        final View overlayView = getViewHolder().getOverlay();
        final int minLeft = dragLayout.getViewLeftMinInside(overlayView);
        final int maxLeft = dragLayout.getViewLeftMaxInside(overlayView);
        final int minTop = dragLayout.getViewTopMinInside(overlayView);
        final int maxTop = dragLayout.getViewTopMaxInside(overlayView);
        final float layoutPercentX;
        final float overlayPercentX;
        if (config.mDefPercentX < -1) {
            layoutPercentX = -1;
            overlayPercentX = config.mDefPercentX + 1;
        } else if (config.mDefPercentX > 1) {
            layoutPercentX = 1;
            overlayPercentX = config.mDefPercentX - 1;
        } else {
            layoutPercentX = config.mDefPercentX;
            overlayPercentX = 0;
        }
        final float layoutPercentY;
        final float overlayPercentY;
        if (config.mDefPercentY < -1) {
            layoutPercentY = -1;
            overlayPercentY = config.mDefPercentY + 1;
        } else if (config.mDefPercentY > 1) {
            layoutPercentY = 1;
            overlayPercentY = config.mDefPercentY - 1;
        } else {
            layoutPercentY = config.mDefPercentY;
            overlayPercentY = 0;
        }
        final int halfRangeH = (maxLeft - minLeft) / 2;
        final int halfRangeV = (maxTop - minTop) / 2;
        final int centerLeft = minLeft + halfRangeH;
        final int centerTop = minTop + halfRangeV;
        final int overlayWidthWithMargin = overlayView.getWidth() + Utils.getViewMarginLeft(overlayView) + Utils.getViewMarginRight(overlayView);
        final int overlayHeightWithMargin = overlayView.getHeight() + Utils.getViewMarginTop(overlayView) + Utils.getViewMarginBottom(overlayView);
        final int defLeft = (int) (centerLeft + halfRangeH * layoutPercentX + overlayWidthWithMargin * overlayPercentX);
        final int defTop = (int) (centerTop + halfRangeV * layoutPercentY + overlayHeightWithMargin * overlayPercentY);
        overlayView.offsetLeftAndRight(defLeft - overlayView.getLeft());
        overlayView.offsetTopAndBottom(defTop - overlayView.getTop());
        overlayView.setPivotX(overlayView.getWidth() * config.mPivotX);
        overlayView.setPivotY(overlayView.getHeight() * config.mPivotY);
        overlayView.setAlpha(config.mDefAlpha);
        overlayView.setScaleX(config.mDefScale);
        overlayView.setScaleY(config.mDefScale);
    }

    public OverlayLayer setOverlayView(@LayoutRes int layoutId) {
        getConfig().mOverlayViewId = layoutId;
        return this;
    }

    public OverlayLayer setOverlayView(@NonNull View overlayView) {
        getConfig().mOverlayView = overlayView;
        return this;
    }

    public OverlayLayer setDefPercentX(float p) {
        getConfig().mDefPercentX = p;
        return this;
    }

    public OverlayLayer setDefPercentY(float p) {
        getConfig().mDefPercentY = p;
        return this;
    }

    public OverlayLayer setDefAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        getConfig().mDefAlpha = alpha;
        return this;
    }

    public OverlayLayer setDefScale(float scale) {
        getConfig().mDefScale = scale;
        return this;
    }

    public OverlayLayer setPivotX(float pivot) {
        getConfig().mPivotX = pivot;
        return this;
    }

    public OverlayLayer setPivotY(float pivot) {
        getConfig().mPivotY = pivot;
        return this;
    }

    public OverlayLayer setNormalAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        getConfig().mNormalAlpha = alpha;
        return this;
    }

    public OverlayLayer setNormalScale(float scale) {
        getConfig().mNormalScale = scale;
        return this;
    }

    public OverlayLayer setLowProfileAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        getConfig().mLowProfileAlpha = alpha;
        return this;
    }

    public OverlayLayer setLowProfileScale(float scale) {
        getConfig().mLowProfileScale = scale;
        return this;
    }

    public OverlayLayer setLowProfileIndent(@FloatRange(from = 0.0, to = 1.0) float indent) {
        getConfig().mLowProfileIndent = indent;
        return this;
    }

    public OverlayLayer setLowProfileDelay(long delay) {
        getConfig().mLowProfileDelay = delay;
        return this;
    }

    public OverlayLayer setSnapEdge(int edge) {
        getConfig().mSnapEdge = edge;
        return this;
    }

    public OverlayLayer setOutside(boolean outside) {
        getConfig().mOutside = outside;
        return this;
    }

    public OverlayLayer setMarginLeft(@Nullable Integer margin) {
        getConfig().mMarginLeft = margin;
        return this;
    }

    public OverlayLayer setMarginTop(@Nullable Integer margin) {
        getConfig().mMarginTop = margin;
        return this;
    }

    public OverlayLayer setMarginRight(@Nullable Integer margin) {
        getConfig().mMarginRight = margin;
        return this;
    }

    public OverlayLayer setMarginBottom(@Nullable Integer margin) {
        getConfig().mMarginBottom = margin;
        return this;
    }

    public OverlayLayer setPaddingLeft(int padding) {
        getConfig().mPaddingLeft = padding;
        return this;
    }

    public OverlayLayer setPaddingTop(int padding) {
        getConfig().mPaddingTop = padding;
        return this;
    }

    public OverlayLayer setPaddingRight(int padding) {
        getConfig().mPaddingRight = padding;
        return this;
    }

    public OverlayLayer setPaddingBottom(int padding) {
        getConfig().mPaddingBottom = padding;
        return this;
    }

    public OverlayLayer addOnOverlayClickListener(@NonNull OnClickListener listener) {
        addOnClickListener(listener);
        return this;
    }

    public OverlayLayer setOnOverlayLongClickListener(@NonNull OnLongClickListener listener) {
        addOnLongClickListener(listener);
        return this;
    }

    public void toNormal() {
        getViewHolder().getOverlay().removeCallbacks(mOverlayLowProfileRunnable);
        getViewHolder().getOverlay().post(mOverlayNormalRunnable);
    }

    public void toLowProfile() {
        if (getConfig().mNormalAlpha != getConfig().mLowProfileAlpha) {
            getViewHolder().getOverlay().removeCallbacks(mOverlayLowProfileRunnable);
            getViewHolder().getOverlay().postDelayed(mOverlayLowProfileRunnable, getConfig().mLowProfileDelay);
        }
    }

    private final Runnable mOverlayNormalRunnable = new Runnable() {
        @Override
        public void run() {
            getViewHolder().getOverlay().animate()
                    .alpha(getConfig().mNormalAlpha)
                    .scaleX(getConfig().mNormalScale)
                    .scaleY(getConfig().mNormalScale)
                    .translationX(0F)
                    .translationY(0F)
                    .start();
        }
    };

    private final Runnable mOverlayLowProfileRunnable = new Runnable() {
        @Override
        public void run() {
            float[] xy = calcIntentTranslation();
            getViewHolder().getOverlay().animate()
                    .alpha(getConfig().mLowProfileAlpha)
                    .scaleX(getConfig().mLowProfileScale)
                    .scaleY(getConfig().mLowProfileScale)
                    .translationX(xy[0])
                    .translationY(xy[1])
                    .start();
        }
    };

    private float[] calcIntentTranslation() {
        float percent = getConfig().mLowProfileIndent;
        float x = 0F;
        float y = 0F;
        if (percent != 0) {
            final DragLayout dragLayout = getViewHolder().getChild();
            final View overlayView = getViewHolder().getOverlay();
            final int currEdge = dragLayout.calcCurrEdge(overlayView);
            final int overlayWidthWithMargin = overlayView.getWidth() + Utils.getViewMarginLeft(overlayView) + Utils.getViewMarginRight(overlayView);
            final int overlayHeightWithMargin = overlayView.getHeight() + Utils.getViewMarginTop(overlayView) + Utils.getViewMarginBottom(overlayView);
            if ((currEdge & DragLayout.Edge.LEFT) != 0) {
                x = -overlayWidthWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.RIGHT) != 0) {
                x = overlayWidthWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.TOP) != 0) {
                y = -overlayHeightWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.BOTTOM) != 0) {
                y = overlayHeightWithMargin * percent;
            }
        }
        return new float[]{x, y};
    }

    private class OverlayDragListener implements DragLayout.OnDragListener {
        @Override
        public void onStart(@NonNull View view) {
            toNormal();
        }

        @Override
        public void onDragging(@NonNull View view) {
        }

        @Override
        public void onRelease(@NonNull View view) {
        }

        @Override
        public void onStop(@NonNull View view) {
            toLowProfile();
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mOverlayView;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
        }

        @NonNull
        @Override
        public DragLayout getChild() {
            return (DragLayout) super.getChild();
        }

        @Nullable
        @Override
        protected DragLayout getChildNullable() {
            return (DragLayout) super.getChildNullable();
        }

        void setOverlay(@NonNull View overlayView) {
            mOverlayView = overlayView;
        }

        @Nullable
        protected View getOverlayNullable() {
            return mOverlayView;
        }

        @NonNull
        public View getOverlay() {
            Utils.requireNonNull(mOverlayView, "必须在show方法后调用");
            return mOverlayView;
        }

        @Nullable
        @Override
        protected View getNoIdClickView() {
            return mOverlayView;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected View mOverlayView = null;
        protected int mOverlayViewId = -1;

        private boolean mOutside = true;
        private int mSnapEdge = OverlayLayer.Edge.HORIZONTAL;

        @FloatRange(from = -2F, to = 2F)
        private float mDefPercentX = 2F;
        @FloatRange(from = -2F, to = 2F)
        private float mDefPercentY = 0.236F;
        @FloatRange(from = 0F, to = 1F)
        private float mDefAlpha = 1F;
        private float mDefScale = 1F;

        private float mPivotX = 0.5F;
        private float mPivotY = 0.5F;

        @FloatRange(from = 0F, to = 1F)
        private float mNormalAlpha = 1F;
        private float mNormalScale = 1F;
        @IntRange(from = 0)
        private long mLowProfileDelay = 3000L;
        @FloatRange(from = 0, to = 1)
        private float mLowProfileAlpha = 0.8F;
        private float mLowProfileScale = 1F;
        @FloatRange(from = 0, to = 1)
        private float mLowProfileIndent = 0F;

        private Integer mMarginLeft = null;
        private Integer mMarginTop = null;
        private Integer mMarginRight = null;
        private Integer mMarginBottom = null;

        private int mPaddingLeft = 0;
        private int mPaddingTop = 0;
        private int mPaddingRight = 0;
        private int mPaddingBottom = 0;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private GestureDetector mGestureDetector = null;

        public void bindTouchListener(@NonNull final OverlayLayer layer) {
            final View overlayView = layer.getViewHolder().getOverlay();
            mGestureDetector = new GestureDetector(overlayView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    layer.toNormal();
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    overlayView.performClick();
                    layer.toLowProfile();
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    overlayView.performLongClick();
                    layer.toLowProfile();
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            overlayView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mGestureDetector.onTouchEvent(event);
                }
            });
        }
    }

    public static class Edge {
        public static final int NONE = 0;

        public static final int LEFT = 1;
        public static final int TOP = 1 << 1;
        public static final int RIGHT = 1 << 2;
        public static final int BOTTOM = 1 << 3;

        public static final int HORIZONTAL = LEFT | RIGHT;
        public static final int VERTICAL = TOP | BOTTOM;

        public static final int ALL = HORIZONTAL | VERTICAL;
    }
}
