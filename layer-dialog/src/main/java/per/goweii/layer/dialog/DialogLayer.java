package per.goweii.layer.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.DecorLayer;
import per.goweii.layer.core.Layers;
import per.goweii.layer.core.anim.AnimatorHelper;
import per.goweii.layer.core.utils.InputMethodCompat;
import per.goweii.layer.core.utils.Utils;
import per.goweii.layer.core.widget.SwipeLayout;

public class DialogLayer extends DecorLayer {
    protected static final long DEFAULT_ANIMATOR_DURATION = 220L;
    protected static final float DEFAULT_BACKGROUND_DIM_AMOUNT = 0.6F;

    private InputMethodCompat mInputMethodCompat = null;

    public static void create(@NonNull DialogLayerActivity.OnLayerCreatedCallback callback) {
        DialogLayerActivity.start(Layers.getApplication(), callback);
    }

    public DialogLayer() {
        this(Layers.requireCurrentActivity());
    }

    public DialogLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public DialogLayer(@NonNull Activity activity) {
        super(activity);
        setInterceptKeyEvent(true);
        setCancelableOnKeyBack(true);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.DIALOG;
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

    @CallSuper
    @Override
    protected void onAttach() {
        onInitContent();
        onInitBackground();
        onInitContainer();
        super.onAttach();
        registerInputMethodCompat();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
        unregisterInputMethodCompat();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final Context context = getActivity();

        ContainerLayout container = new ContainerLayout(context);

        View background = onCreateBackground(inflater, container);
        if (background != null) {
            getViewHolder().setBackground(background);
            ViewGroup.LayoutParams backgroundLayoutParams = background.getLayoutParams();
            if (backgroundLayoutParams == null) {
                backgroundLayoutParams = generateBackgroundDefaultLayoutParams();
            }
            background.setLayoutParams(backgroundLayoutParams);
            container.addView(background);
        }

        SwipeLayout contentWrapper = new SwipeLayout(getActivity());
        getViewHolder().setContentWrapper(contentWrapper);
        contentWrapper.setLayoutParams(generateContentWrapperDefaultLayoutParams());
        container.addView(contentWrapper);

        View content = onCreateContent(inflater, contentWrapper);
        getViewHolder().setContent(content);
        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();
        FrameLayout.LayoutParams newContentLayoutParams;
        if (contentLayoutParams == null) {
            newContentLayoutParams = generateContentDefaultLayoutParams();
        } else if (contentLayoutParams instanceof FrameLayout.LayoutParams) {
            newContentLayoutParams = (FrameLayout.LayoutParams) contentLayoutParams;
        } else {
            newContentLayoutParams = new FrameLayout.LayoutParams(contentLayoutParams.width, contentLayoutParams.height);
        }
        content.setLayoutParams(newContentLayoutParams);
        contentWrapper.addView(content);

        return container;
    }

    @NonNull
    protected View onCreateBackground(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getConfig().mBackgroundView != null) {
            ViewGroup backgroundParent = (ViewGroup) getConfig().mBackgroundView.getParent();
            if (backgroundParent != null) {
                backgroundParent.removeView(getConfig().mBackgroundView);
            }
            return getConfig().mBackgroundView;
        }
        if (getConfig().mBackgroundViewId > 0) {
            return inflater.inflate(getConfig().mBackgroundViewId, parent, false);
        }
        if (getConfig().mBackgroundBitmap != null) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageBitmap(getConfig().mBackgroundBitmap);
            if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                imageView.setColorFilter(getConfig().mBackgroundColor);
            }
            return imageView;
        }
        if (getConfig().mBackgroundDrawable != null) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageDrawable(getConfig().mBackgroundDrawable);
            if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                imageView.setColorFilter(getConfig().mBackgroundColor);
            }
            return imageView;
        }
        if (getConfig().mBackgroundResource != -1) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(getConfig().mBackgroundResource);
            if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                imageView.setColorFilter(getConfig().mBackgroundColor);
            }
        }
        if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
            View view = new View(getActivity());
            view.setBackgroundColor(getConfig().mBackgroundColor);
            return view;
        }
        return null;
    }

    @NonNull
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getConfig().mContentView != null) {
            Utils.removeViewParent(getConfig().mContentView);
            return getConfig().mContentView;
        }
        if (getConfig().mContentViewId != View.NO_ID) {
            return inflater.inflate(getConfig().mContentViewId, parent, false);
        }
        throw new IllegalStateException("?????????contentView");
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        Animator backgroundAnimator = null;
        if (getViewHolder().getBackground() != null) {
            backgroundAnimator = onCreateBackgroundInAnimator(getViewHolder().getBackground());
        }
        Animator contentAnimator = onCreateContentInAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) return null;
        if (backgroundAnimator == null) return contentAnimator;
        if (contentAnimator == null) return backgroundAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Nullable
    protected Animator onCreateBackgroundInAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createInAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundInAnimator(view);
        }
        return backgroundAnimator;
    }

    @NonNull
    protected Animator onCreateDefBackgroundInAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createAlphaInAnim(view);
        animator.setDuration(DEFAULT_ANIMATOR_DURATION);
        return animator;
    }

    @Nullable
    protected Animator onCreateContentInAnimator(@NonNull View view) {
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createInAnimator(view);
        } else {
            contentAnimator = onCreateDefContentInAnimator(view);
        }
        return contentAnimator;
    }

    @NonNull
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createZoomAlphaInAnim(view);
        animator.setDuration(DEFAULT_ANIMATOR_DURATION);
        return animator;
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator backgroundAnimator = null;
        if (getViewHolder().getBackground() != null) {
            backgroundAnimator = onCreateBackgroundOutAnimator(getViewHolder().getBackground());
        }
        Animator contentAnimator = onCreateContentOutAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) return null;
        if (backgroundAnimator == null) return contentAnimator;
        if (contentAnimator == null) return backgroundAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Nullable
    protected Animator onCreateBackgroundOutAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createOutAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundOutAnimator(view);
        }
        return backgroundAnimator;
    }

    @NonNull
    protected Animator onCreateDefBackgroundOutAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createAlphaOutAnim(view);
        animator.setDuration(DEFAULT_ANIMATOR_DURATION);
        return animator;
    }

    @Nullable
    protected Animator onCreateContentOutAnimator(@NonNull View view) {
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createOutAnimator(view);
        } else {
            contentAnimator = onCreateDefContentOutAnimator(view);
        }
        return contentAnimator;
    }

    @NonNull
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createZoomAlphaOutAnim(view);
        animator.setDuration(DEFAULT_ANIMATOR_DURATION);
        return animator;
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    protected FrameLayout.LayoutParams generateBackgroundDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    protected FrameLayout.LayoutParams generateContentWrapperDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    protected FrameLayout.LayoutParams generateContentDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void fitDecorInsets(@NonNull Rect insets) {
        Utils.setViewPadding(getViewHolder().getContentWrapper(), insets);
        if (getConfig().mAvoidStatusBar) {
            int paddingTop = getViewHolder().getContentWrapper().getPaddingTop();
            int statusBarHeight = Utils.getStatusBarHeightIfVisible(getActivity());
            Utils.setViewPaddingTop(getViewHolder().getContentWrapper(), Math.max(paddingTop, statusBarHeight));
        }
        getViewHolder().getContentWrapper().setClipToPadding(false);
        getViewHolder().getContentWrapper().setClipChildren(false);
    }

    protected void onInitContent() {
        getViewHolder().getContent().setClickable(true);
        FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        if (getConfig().mGravity != Gravity.NO_GRAVITY) {
            contentParams.gravity = getConfig().mGravity;
        }
        getViewHolder().getContent().setLayoutParams(contentParams);
    }

    protected void onInitBackground() {
    }

    protected void onInitContainer() {
        if (getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getContainer().setForceFocusInside(true);
            getViewHolder().getContainer().setHandleTouchEvent(true);
            if (getConfig().mCancelableOnTouchOutside) {
                getViewHolder().getContainer().setOnTappedListener(new ContainerLayout.OnTappedListener() {
                    @Override
                    public void onTapped() {
                        dismiss();
                    }
                });
            }
        } else {
            getViewHolder().getContainer().setOnTappedListener(null);
            getViewHolder().getContainer().setForceFocusInside(false);
            getViewHolder().getContainer().setHandleTouchEvent(false);
        }
        if (getConfig().mOutsideTouchedToDismiss || getConfig().mOnOutsideTouchListener != null) {
            getViewHolder().getContainer().setOnTouchedListener(new ContainerLayout.OnTouchedListener() {
                @Override
                public void onTouched() {
                    if (getConfig().mOutsideTouchedToDismiss) {
                        dismiss();
                    }
                    if (getConfig().mOnOutsideTouchListener != null) {
                        getConfig().mOnOutsideTouchListener.onOutsideTouched();
                    }
                }
            });
        }
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        getViewHolder().getContentWrapper().setSwipeDirection(getConfig().mSwipeDirection);
        getViewHolder().getContentWrapper().setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onStart(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (getConfig().mSwipeTransformer == null) {
                    getConfig().mSwipeTransformer = new SwipeTransformer() {
                        @Override
                        public void onSwiping(@NonNull DialogLayer layer, @SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                            if (getViewHolder().getBackground() != null) {
                                getViewHolder().getBackground().setAlpha(1F - fraction);
                            }
                        }
                    };
                }
                getListenerHolder().notifyOnSwipeStart(DialogLayer.this);
            }

            @Override
            public void onSwiping(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (getConfig().mSwipeTransformer != null) {
                    getConfig().mSwipeTransformer.onSwiping(DialogLayer.this, direction, fraction);
                }
                getListenerHolder().notifyOnSwiping(DialogLayer.this, direction, fraction);
            }

            @Override
            public void onEnd(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (fraction == 1F) {
                    getListenerHolder().notifyOnSwipeEnd(DialogLayer.this, direction);
                    // ?????????????????????????????????removeView??????????????????dispatchDraw????????????
                    // ????????????????????????viewGroup???childCount???????????????-1???????????????view??????
                    getViewHolder().getContentWrapper().setVisibility(View.INVISIBLE);
                    getViewHolder().getContentWrapper().post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss(false);
                        }
                    });
                }
            }
        });
        getViewHolder().getContentWrapper().setVisibility(View.VISIBLE);
    }

    private void registerInputMethodCompat() {
        final SparseBooleanArray mapping = getConfig().mInputMethodMapping;
        if (mapping == null || mapping.size() == 0) {
            return;
        }
        if (mInputMethodCompat == null) {
            mInputMethodCompat = InputMethodCompat.attach(getActivity());
        } else {
            mInputMethodCompat.clear();
        }
        mInputMethodCompat.setListener(new InputMethodCompat.InputMethodListener() {
            @Override
            public void onOpen(int height) {
                getListenerHolder().notifyOnSoftInputOpen(DialogLayer.this, height);
            }

            @Override
            public void onClose(int height) {
                getListenerHolder().notifyOnSoftInputClose(DialogLayer.this, height);
            }

            @Override
            public void onHeightChange(int height) {
                getListenerHolder().notifyOnSoftInputHeightChange(DialogLayer.this, height);
            }
        });
        mInputMethodCompat.setMoveView(getViewHolder().getContentWrapper());
        for (int i = 0; i < mapping.size(); i++) {
            boolean alignToContentOrFocus = mapping.valueAt(i);
            int focusId = mapping.keyAt(i);
            if (focusId == View.NO_ID) {
                if (alignToContentOrFocus) {
                    mInputMethodCompat.setFollowViews(getViewHolder().getContent());
                }
            } else {
                if (alignToContentOrFocus) {
                    mInputMethodCompat.setFollowViews(getViewHolder().getContent(), findViewById(focusId));
                } else {
                    mInputMethodCompat.setFollowViews(null, findViewById(focusId));
                }
            }
        }
    }

    private void unregisterInputMethodCompat() {
        if (mInputMethodCompat != null) {
            mInputMethodCompat.setListener(null);
            mInputMethodCompat.clear();
            mInputMethodCompat.detach();
            mInputMethodCompat = null;
        }
    }

    /**
     * ??????????????????View
     *
     * @param contentView ?????????View
     */
    @NonNull
    public DialogLayer setContentView(@Nullable View contentView) {
        getConfig().mContentView = contentView;
        return this;
    }

    /**
     * ???????????????????????????
     *
     * @param contentViewId ???????????????ID
     */
    @NonNull
    public DialogLayer setContentView(@LayoutRes int contentViewId) {
        getConfig().mContentViewId = contentViewId;
        return this;
    }

    /**
     * ????????????????????????View
     *
     * @param backgroundView ???????????????View
     */
    @NonNull
    public DialogLayer setBackgroundView(@Nullable View backgroundView) {
        getConfig().mBackgroundView = backgroundView;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param backgroundViewId ???????????????ID
     */
    @NonNull
    public DialogLayer setBackgroundView(@LayoutRes int backgroundViewId) {
        getConfig().mBackgroundViewId = backgroundViewId;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param resource ??????ID
     */
    @NonNull
    public DialogLayer setBackgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    /**
     * ????????????Drawable
     *
     * @param drawable Drawable
     */
    @NonNull
    public DialogLayer setBackgroundDrawable(@Nullable Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param bitmap ??????
     */
    @NonNull
    public DialogLayer setBackgroundBitmap(@Nullable Bitmap bitmap) {
        getConfig().mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param dimAmount ???????????? 0~1
     */
    @NonNull
    public DialogLayer setBackgroundDimAmount(@FloatRange(from = 0F, to = 1F) float dimAmount) {
        getConfig().mBackgroundColor = Color.argb((int) (dimAmount * 255), 0, 0, 0);
        return this;
    }

    /**
     * ??????????????????
     */
    @NonNull
    public DialogLayer setBackgroundDimDefault() {
        return setBackgroundDimAmount(DEFAULT_BACKGROUND_DIM_AMOUNT);
    }


    /**
     * ??????????????????
     *
     * @param colorInt ?????????
     */
    @NonNull
    public DialogLayer setBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    /**
     * ??????????????????
     *
     * @param colorRes ????????????ID
     */
    @NonNull
    public DialogLayer setBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    /**
     * ?????????????????????
     *
     * @param avoid ?????????????????????
     */
    @NonNull
    public DialogLayer setAvoidStatusBar(boolean avoid) {
        getConfig().mAvoidStatusBar = avoid;
        return this;
    }

    /**
     * ??????????????????gravity
     * ??????????????????????????????layout_gravity?????????????????????
     *
     * @param gravity {@link Gravity}
     */
    @NonNull
    public DialogLayer setGravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param swipeDirection {@link SwipeLayout.Direction}
     */
    @NonNull
    public DialogLayer setSwipeDismiss(int swipeDirection) {
        getConfig().mSwipeDirection = swipeDirection;
        return this;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param swipeTransformer SwipeTransformer
     */
    @NonNull
    public DialogLayer setSwipeTransformer(@Nullable SwipeTransformer swipeTransformer) {
        getConfig().mSwipeTransformer = swipeTransformer;
        return this;
    }

    /**
     * ????????????????????????
     *
     * @param swipeListener OnSwipeListener
     */
    @NonNull
    public DialogLayer addOnSwipeListener(@NonNull OnSwipeListener swipeListener) {
        getListenerHolder().addOnSwipeListener(swipeListener);
        return this;
    }

    /**
     * ???????????????????????????????????????
     * ??????????????????{@link AnimatorHelper}
     *
     * @param contentAnimatorCreator AnimatorCreator
     */
    @NonNull
    public DialogLayer setContentAnimator(@Nullable AnimatorCreator contentAnimatorCreator) {
        getConfig().mContentAnimatorCreator = contentAnimatorCreator;
        return this;
    }

    /**
     * ???????????????????????????????????????
     * ??????????????????{@link AnimatorHelper}
     *
     * @param backgroundAnimatorCreator AnimatorCreator
     */
    @NonNull
    public DialogLayer setBackgroundAnimator(@Nullable AnimatorCreator backgroundAnimatorCreator) {
        getConfig().mBackgroundAnimatorCreator = backgroundAnimatorCreator;
        return this;
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param cancelable ???????????????
     */
    @NonNull
    public DialogLayer setCancelableOnTouchOutside(boolean cancelable) {
        getConfig().mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * ????????????????????????????????????
     *
     * @param cancelable ???????????????
     */
    @NonNull
    @Override
    public DialogLayer setCancelableOnClickKeyBack(boolean cancelable) {
        return (DialogLayer) super.setCancelableOnClickKeyBack(cancelable);
    }

    /**
     * ?????????????????????????????????????????????
     * ????????????View???????????????????????????
     *
     * @param alignToContentOrFocus true????????????contentView???false????????????focusView??????
     * @param focusIds              ??????View
     */
    @NonNull
    public DialogLayer addInputMethodCompat(boolean alignToContentOrFocus, @Nullable int... focusIds) {
        if (getConfig().mInputMethodMapping == null) {
            getConfig().mInputMethodMapping = new SparseBooleanArray(1);
        }
        if (focusIds != null && focusIds.length > 0) {
            for (int focusId : focusIds) {
                getConfig().mInputMethodMapping.append(focusId, alignToContentOrFocus);
            }
        } else {
            getConfig().mInputMethodMapping.append(View.NO_ID, alignToContentOrFocus);
        }
        return this;
    }

    public DialogLayer addOnInputMethodListener(@NonNull OnInputMethodListener onInputMethodListener) {
        getListenerHolder().addOnInputMethodListener(onInputMethodListener);
        return this;
    }

    /**
     * ????????????????????????????????????
     * ?????????true???false????????????activityContent????????????
     *
     * @param intercept ????????????????????????
     */
    @NonNull
    public DialogLayer setOutsideInterceptTouchEvent(boolean intercept) {
        getConfig().mOutsideInterceptTouchEvent = intercept;
        return this;
    }

    @NonNull
    public DialogLayer setOnOutsideTouchListener(OnOutsideTouchListener listener) {
        getConfig().mOnOutsideTouchListener = listener;
        return this;
    }

    @NonNull
    public DialogLayer setOutsideTouchToDismiss(boolean toDismiss) {
        getConfig().mOutsideTouchedToDismiss = toDismiss;
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private SwipeLayout mContentWrapper;
        private View mBackground;
        private View mContent;

        @NonNull
        public ContainerLayout getContainer() {
            return (ContainerLayout) getChild();
        }

        protected void setContent(@Nullable View content) {
            mContent = content;
        }

        @Nullable
        protected View getContentOrNull() {
            return mContent;
        }

        @NonNull
        public View getContent() {
            Utils.requireNonNull(mContent, "?????????show???????????????");
            return mContent;
        }

        public void setContentWrapper(@Nullable SwipeLayout contentWrapper) {
            mContentWrapper = contentWrapper;
        }

        @NonNull
        public SwipeLayout getContentWrapper() {
            Utils.requireNonNull(mContentWrapper, "?????????show???????????????");
            return mContentWrapper;
        }

        public void setBackground(@Nullable View background) {
            mBackground = background;
        }

        @Nullable
        public View getBackground() {
            return mBackground;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected boolean mOutsideInterceptTouchEvent = true;
        @Nullable
        protected OnOutsideTouchListener mOnOutsideTouchListener = null;
        protected boolean mOutsideTouchedToDismiss = false;

        @Nullable
        protected AnimatorCreator mBackgroundAnimatorCreator = null;
        @Nullable
        protected AnimatorCreator mContentAnimatorCreator = null;

        @Nullable
        protected View mContentView = null;
        @LayoutRes
        protected int mContentViewId = View.NO_ID;

        protected View mBackgroundView = null;
        protected int mBackgroundViewId = View.NO_ID;
        @Nullable
        protected Bitmap mBackgroundBitmap = null;
        @Nullable
        protected Drawable mBackgroundDrawable = null;
        @DrawableRes
        protected int mBackgroundResource = View.NO_ID;
        @ColorInt
        protected int mBackgroundColor = Color.TRANSPARENT;

        protected boolean mCancelableOnTouchOutside = true;

        protected boolean mAvoidStatusBar = false;

        protected int mGravity = Gravity.NO_GRAVITY;
        @SwipeLayout.Direction
        protected int mSwipeDirection = 0;
        @Nullable
        protected SwipeTransformer mSwipeTransformer = null;

        protected SparseBooleanArray mInputMethodMapping = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private List<OnSwipeListener> mOnSwipeListeners = null;
        private List<OnInputMethodListener> mOnInputMethodListeners = null;

        private void addOnSwipeListener(@NonNull OnSwipeListener onSwipeListener) {
            if (mOnSwipeListeners == null) {
                mOnSwipeListeners = new ArrayList<>(1);
            }
            mOnSwipeListeners.add(onSwipeListener);
        }

        private void addOnInputMethodListener(@NonNull OnInputMethodListener onInputMethodListener) {
            if (mOnInputMethodListeners == null) {
                mOnInputMethodListeners = new ArrayList<>(1);
            }
            mOnInputMethodListeners.add(onInputMethodListener);
        }

        private void notifyOnSwipeStart(@NonNull DialogLayer layer) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onStart(layer);
                }
            }
        }

        private void notifyOnSwiping(@NonNull DialogLayer layer,
                                     @SwipeLayout.Direction int direction,
                                     @FloatRange(from = 0F, to = 1F) float fraction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onSwiping(layer, direction, fraction);
                }
            }
        }

        private void notifyOnSwipeEnd(@NonNull DialogLayer layer,
                                      @SwipeLayout.Direction int direction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onEnd(layer, direction);
                }
            }
        }

        private void notifyOnSoftInputOpen(@NonNull DialogLayer layer, @Px int height) {
            if (mOnInputMethodListeners != null) {
                for (OnInputMethodListener onInputMethodListener : mOnInputMethodListeners) {
                    onInputMethodListener.onOpen(layer, height);
                }
            }
        }

        private void notifyOnSoftInputClose(@NonNull DialogLayer layer, @Px int height) {
            if (mOnInputMethodListeners != null) {
                for (OnInputMethodListener onInputMethodListener : mOnInputMethodListeners) {
                    onInputMethodListener.onClose(layer, height);
                }
            }
        }

        private void notifyOnSoftInputHeightChange(@NonNull DialogLayer layer, @Px int height) {
            if (mOnInputMethodListeners != null) {
                for (OnInputMethodListener onInputMethodListener : mOnInputMethodListeners) {
                    onInputMethodListener.onHeightChange(layer, height);
                }
            }
        }
    }

    public interface OnOutsideTouchListener {
        void onOutsideTouched();
    }

    public interface SwipeTransformer {
        void onSwiping(@NonNull DialogLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);
    }

    public interface OnSwipeListener {
        void onStart(@NonNull DialogLayer layer);

        void onSwiping(@NonNull DialogLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);

        void onEnd(@NonNull DialogLayer layer,
                   @SwipeLayout.Direction int direction);
    }

    public interface OnInputMethodListener {
        void onOpen(@NonNull DialogLayer layer, @Px int height);

        void onClose(@NonNull DialogLayer layer, @Px int height);

        void onHeightChange(@NonNull DialogLayer layer, @Px int height);
    }
}
