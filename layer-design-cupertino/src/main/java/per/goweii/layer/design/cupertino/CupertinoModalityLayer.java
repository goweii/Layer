package per.goweii.layer.design.cupertino;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.listener.DefaultAnimatorListener;
import per.goweii.layer.core.utils.Utils;
import per.goweii.layer.core.widget.SwipeLayout;
import per.goweii.layer.dialog.DialogLayer;

public class CupertinoModalityLayer extends DialogLayer {

    private int mWindowBackgroundBackup = -1;
    private Drawable mDecorBackgroundBackup = null;
    private Drawable mDecorChildBackgroundBackup = null;

    private float mAnimationAnimatedFraction = 0F;
    private float mDecorChildCornerRadius = 0F;

    public CupertinoModalityLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CupertinoModalityLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    private void init() {
        mDecorChildCornerRadius = getActivity().getResources().getDimension(R.dimen.layer_design_cupertino_corner_radius_big);
        setAvoidStatusBar(true);
        addOnSwipeListener(new OnSwipeListenerImpl());
        setSwipeDismiss(SwipeLayout.Direction.BOTTOM);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        FrameLayout decor = getViewHolder().getDecor();
        View decorChild = getViewHolder().getDecorChild();
        mDecorBackgroundBackup = decor.getBackground();
        mDecorChildBackgroundBackup = decorChild.getBackground();
        TypedArray typedArray = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        mWindowBackgroundBackup = typedArray.getResourceId(0, 0);
        typedArray.recycle();
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        decor.setBackground(new ColorDrawable(Color.BLACK));
        if (mDecorChildBackgroundBackup == null) {
            if (mDecorBackgroundBackup != null) {
                decorChild.setBackground(mDecorBackgroundBackup);
            } else if (mWindowBackgroundBackup != -1) {
                decorChild.setBackgroundResource(mWindowBackgroundBackup);
            }
        }
    }

    @Override
    protected void onPreShow() {
        super.onPreShow();
        final FrameLayout decor = getViewHolder().getDecor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            foreachDecorChildren(decor, new ForeachCallback() {
                @Override
                public void onEach(@NonNull final View child) {
                    child.setTag(R.id.layer_design_cupertino_view_clip_to_outline_backup, child.getClipToOutline());
                    child.setTag(R.id.layer_design_cupertino_view_outline_provider_backup, child.getOutlineProvider());
                    child.setClipToOutline(true);
                    child.setOutlineProvider(new ViewOutlineProvider() {
                        @Override
                        public void getOutline(View view, Outline outline) {
                            float cornerRadius = mDecorChildCornerRadius * mAnimationAnimatedFraction;
                            outline.setRoundRect(
                                    0, 0,
                                    getViewHolder().getDecor().getWidth(),
                                    getViewHolder().getDecor().getHeight(),
                                    cornerRadius
                            );
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onPostDismiss() {
        super.onPostDismiss();
        final FrameLayout decor = getViewHolder().getDecor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            foreachDecorChildren(decor, new ForeachCallback() {
                @Override
                public void onEach(@NonNull final View child) {
                    Object tagClipToOutlineBackup = child.getTag(R.id.layer_design_cupertino_view_clip_to_outline_backup);
                    Object tagOutlineProviderBackup = child.getTag(R.id.layer_design_cupertino_view_outline_provider_backup);
                    if (tagClipToOutlineBackup instanceof Boolean) {
                        child.setClipToOutline((boolean) tagClipToOutlineBackup);
                    } else {
                        child.setClipToOutline(false);
                    }
                    if (tagOutlineProviderBackup instanceof ViewOutlineProvider) {
                        child.setOutlineProvider((ViewOutlineProvider) tagOutlineProviderBackup);
                    } else {
                        child.setOutlineProvider(null);
                    }
                }
            });
        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        if (mWindowBackgroundBackup != -1) {
            getActivity().getWindow().setBackgroundDrawableResource(mWindowBackgroundBackup);
        }
        FrameLayout decor = getViewHolder().getDecor();
        View decorChild = getViewHolder().getDecorChild();
        decor.setBackground(mDecorBackgroundBackup);
        decorChild.setBackground(mDecorChildBackgroundBackup);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        final List<Animator> scaleAnimList = new ArrayList<>();
        final AnimatorSet animatorSet = new AnimatorSet();

        final SwipeLayout swipeLayout = getViewHolder().getContentWrapper();
        final ValueAnimator contentInAnimator = ValueAnimator.ofInt(swipeLayout.getHeight(), 0);
        contentInAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                swipeLayout.swipeTo(0, animatedValue);
            }
        });
        contentInAnimator.addListener(new DefaultAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                swipeLayout.startFakeSwipe(SwipeLayout.Direction.BOTTOM);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                swipeLayout.endFakeSwipe();
            }
        });
        scaleAnimList.add(contentInAnimator);

        animatorSet.playTogether(scaleAnimList);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        return animatorSet;
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        final AnimatorSet animatorSet = new AnimatorSet();
        final List<Animator> scaleAnimList = new ArrayList<>();

        final SwipeLayout swipeLayout = getViewHolder().getContentWrapper();
        final ValueAnimator contentOutAnimator = ValueAnimator.ofInt(swipeLayout.getSwipeY(), swipeLayout.getHeight());
        contentOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                swipeLayout.swipeTo(0, animatedValue);
            }
        });
        contentOutAnimator.addListener(new DefaultAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                swipeLayout.startFakeSwipe(SwipeLayout.Direction.BOTTOM);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                swipeLayout.endFakeSwipe();
            }
        });
        scaleAnimList.add(contentOutAnimator);

        animatorSet.playTogether(scaleAnimList);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        return animatorSet;
    }

    @Override
    protected void fitDecorInsets(@NonNull Rect insets) {
        int cornerRadius = (int) mDecorChildCornerRadius;
        insets.top = insets.top + cornerRadius;
        super.fitDecorInsets(insets);
    }

    private class OnSwipeListenerImpl implements OnSwipeListener {
        private float mFromScale = 1F;
        private float mToScale = 1F;

        @Override
        public void onStart(@NonNull DialogLayer layer) {
            final FrameLayout decor = getViewHolder().getDecor();
            foreachDecorChildren(decor, new ForeachCallback() {
                @Override
                public void onEach(@NonNull View view) {
                    view.setPivotX(decor.getWidth() / 2F);
                    view.setPivotY(decor.getHeight() / 2F);
                }
            });
            mFromScale = 1F;
            float dcHeight = decor.getHeight();
            float statusBarHeight = Utils.getStatusBarHeight(getActivity());
            mToScale = (dcHeight - statusBarHeight * 2F) / dcHeight;
        }

        @Override
        public void onSwiping(@NonNull DialogLayer layer, int direction, float fraction) {
            mAnimationAnimatedFraction = 1F - fraction;
            final FrameLayout decor = getViewHolder().getDecor();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                foreachDecorChildren(decor, new ForeachCallback() {
                    @Override
                    public void onEach(@NonNull View view) {
                        float scale = mFromScale + (mToScale - mFromScale) * mAnimationAnimatedFraction;
                        view.setScaleX(scale);
                        view.setScaleY(scale);
                        view.invalidateOutline();
                    }
                });
            }
        }

        @Override
        public void onEnd(@NonNull DialogLayer layer, int direction) {
            mAnimationAnimatedFraction = 0F;
            final FrameLayout decor = getViewHolder().getDecor();
            foreachDecorChildren(decor, new ForeachCallback() {
                @Override
                public void onEach(@NonNull View view) {
                    view.setScaleX(mFromScale);
                    view.setScaleY(mFromScale);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.invalidateOutline();
                    }
                }
            });
        }
    }

    private static void foreachDecorChildren(@NonNull FrameLayout decor, @NonNull ForeachCallback callback) {
        for (int i = 0; i < decor.getChildCount() - 1; i++) {
            final View child = decor.getChildAt(i);
            if (child instanceof ViewStub) continue;
            if (!child.isShown()) continue;
            callback.onEach(child);
        }
    }

    private interface ForeachCallback {
        void onEach(@NonNull final View view);
    }
}
