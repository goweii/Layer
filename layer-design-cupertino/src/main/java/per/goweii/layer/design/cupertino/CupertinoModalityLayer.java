package per.goweii.layer.design.cupertino;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import per.goweii.layer.core.anim.AnimatorHelper;
import per.goweii.layer.core.utils.Utils;
import per.goweii.layer.dialog.DialogLayer;

public class CupertinoModalityLayer extends DialogLayer {

    private int mWindowBackgroundBackup = -1;
    private Drawable mDecorBackgroundBackup = null;
    private Drawable mDecorChildBackgroundBackup = null;
    private ViewOutlineProvider mDecorChildOutlineProviderBackup = null;
    private boolean mDecorChildClipToOutlineBackup = false;

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
        setSwipeDismiss(0);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        return AnimatorHelper.createBottomInAnim(view);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        return AnimatorHelper.createBottomOutAnim(view);
    }

    @NonNull
    @Override
    protected Animator onCreateDefBackgroundInAnimator(@NonNull View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator bgAnim = super.onCreateDefBackgroundInAnimator(view);
        View decorChild = getViewHolder().getDecorChild();
        decorChild.setPivotX(decorChild.getWidth() / 2F);
        decorChild.setPivotY(decorChild.getHeight());
        float fromScaleX = decorChild.getScaleX();
        float fromScaleY = decorChild.getScaleY();
        float dcHeight = decorChild.getHeight();
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        float toScale = (dcHeight - statusBarHeight) / dcHeight;
        ObjectAnimator dcScaleX = ObjectAnimator.ofFloat(decorChild, "scaleX", fromScaleX, toScale);
        ObjectAnimator dcScaleY = ObjectAnimator.ofFloat(decorChild, "scaleY", fromScaleY, toScale);
        dcScaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationAnimatedFraction = animation.getAnimatedFraction();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View decorChild = getViewHolder().getDecorChild();
                    decorChild.invalidateOutline();
                }
            }
        });
        animatorSet.playTogether(bgAnim, dcScaleX, dcScaleY);
        return animatorSet;
    }

    @NonNull
    @Override
    protected Animator onCreateDefBackgroundOutAnimator(@NonNull View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator bgAnim = super.onCreateDefBackgroundOutAnimator(view);
        View decorChild = getViewHolder().getDecorChild();
        decorChild.setPivotX(decorChild.getWidth() / 2F);
        decorChild.setPivotY(decorChild.getHeight());
        float fromScaleX = decorChild.getScaleX();
        float fromScaleY = decorChild.getScaleY();
        float toScale = 1F;
        ObjectAnimator dcScaleX = ObjectAnimator.ofFloat(decorChild, "scaleX", fromScaleX, toScale);
        ObjectAnimator dcScaleY = ObjectAnimator.ofFloat(decorChild, "scaleY", fromScaleY, toScale);
        dcScaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimationAnimatedFraction = 1F - animation.getAnimatedFraction();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View decorChild = getViewHolder().getDecorChild();
                    decorChild.invalidateOutline();
                }
            }
        });
        animatorSet.playTogether(bgAnim, dcScaleX, dcScaleY);
        return animatorSet;
    }

    @Override
    protected void fitDecorInsides() {
        super.fitDecorInsides();
        int paddingTop = getViewHolder().getContentWrapper().getPaddingTop();
        int cornerRadius = (int) mDecorChildCornerRadius;
        Utils.setViewPaddingTop(getViewHolder().getContentWrapper(), paddingTop + cornerRadius);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mDecorChildClipToOutlineBackup = decorChild.getClipToOutline();
            mDecorChildOutlineProviderBackup = decorChild.getOutlineProvider();
            decorChild.setClipToOutline(true);
            decorChild.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    float cornerRadius = mDecorChildCornerRadius * mAnimationAnimatedFraction;
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            decorChild.setOutlineProvider(mDecorChildOutlineProviderBackup);
            decorChild.setClipToOutline(mDecorChildClipToOutlineBackup);
        }
    }

    private class OnSwipeListenerImpl implements OnSwipeListener {
        private float mFromScale = 1F;
        private float mToScale = 1F;

        @Override
        public void onStart(@NonNull DialogLayer layer) {
            View decorChild = getViewHolder().getDecorChild();
            decorChild.setPivotX(decorChild.getWidth() / 2F);
            decorChild.setPivotY(decorChild.getHeight());
            mFromScale = 1F;
            float dcHeight = decorChild.getHeight();
            float statusBarHeight = Utils.getStatusBarHeight(getActivity());
            mToScale = (dcHeight - statusBarHeight) / dcHeight;
        }

        @Override
        public void onSwiping(@NonNull DialogLayer layer, int direction, float fraction) {
            mAnimationAnimatedFraction = 1F - fraction;
            View decorChild = getViewHolder().getDecorChild();
            float scale = mFromScale + (mToScale - mFromScale) * mAnimationAnimatedFraction;
            decorChild.setScaleX(scale);
            decorChild.setScaleY(scale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                decorChild.invalidateOutline();
            }
        }

        @Override
        public void onEnd(@NonNull DialogLayer layer, int direction) {
            mAnimationAnimatedFraction = 0F;
            View decorChild = getViewHolder().getDecorChild();
            decorChild.setScaleX(mFromScale);
            decorChild.setScaleY(mFromScale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                decorChild.invalidateOutline();
            }
        }
    }
}
