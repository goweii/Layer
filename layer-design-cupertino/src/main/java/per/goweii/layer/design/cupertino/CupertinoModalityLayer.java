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
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewStub;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.anim.AnimatorHelper;
import per.goweii.layer.core.utils.Utils;
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
        FrameLayout decor = getViewHolder().getDecor();
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        float toScale = (decor.getHeight() - statusBarHeight * 2F) / decor.getHeight();
        List<Animator> scaleAnimList = new ArrayList<>();
        for (int i = 0; i < decor.getChildCount() - 1; i++) {
            final View child = decor.getChildAt(i);
            child.setPivotX(decor.getWidth() / 2F);
            child.setPivotY(decor.getHeight() / 2F);
            float fromScaleX = child.getScaleX();
            float fromScaleY = child.getScaleY();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(child, "scaleX", fromScaleX, toScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(child, "scaleY", fromScaleY, toScale);
            scaleAnimList.add(scaleX);
            scaleAnimList.add(scaleY);
            scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAnimationAnimatedFraction = animation.getAnimatedFraction();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        child.invalidateOutline();
                    }
                }
            });
        }
        scaleAnimList.add(bgAnim);
        animatorSet.playTogether(scaleAnimList);
        return animatorSet;
    }

    @NonNull
    @Override
    protected Animator onCreateDefBackgroundOutAnimator(@NonNull View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator bgAnim = super.onCreateDefBackgroundOutAnimator(view);
        FrameLayout decor = getViewHolder().getDecor();
        float toScale = 1F;
        List<Animator> scaleAnimList = new ArrayList<>();
        for (int i = 0; i < decor.getChildCount() - 1; i++) {
            final View child = decor.getChildAt(i);
            if (child instanceof ViewStub) continue;
            if (!child.isShown()) continue;
            child.setPivotX(decor.getWidth() / 2F);
            child.setPivotY(decor.getHeight() / 2F);
            float fromScaleX = child.getScaleX();
            float fromScaleY = child.getScaleY();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(child, "scaleX", fromScaleX, toScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(child, "scaleY", fromScaleY, toScale);
            scaleAnimList.add(scaleX);
            scaleAnimList.add(scaleY);
            scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mAnimationAnimatedFraction = 1F - animation.getAnimatedFraction();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        child.invalidateOutline();
                    }
                }
            });
        }
        scaleAnimList.add(bgAnim);
        animatorSet.playTogether(scaleAnimList);
        return animatorSet;
    }

    @Override
    protected void fitDecorInsets(@NonNull Rect insets) {
        int cornerRadius = (int) mDecorChildCornerRadius;
        insets.top = insets.top + cornerRadius;
        super.fitDecorInsets(insets);
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
        FrameLayout decor = getViewHolder().getDecor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < decor.getChildCount() - 1; i++) {
                final View child = decor.getChildAt(i);
                if (child instanceof ViewStub) continue;
                if (!child.isShown()) continue;
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
        }
    }

    @Override
    protected void onPostDismiss() {
        super.onPostDismiss();
        FrameLayout decor = getViewHolder().getDecor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < decor.getChildCount() - 1; i++) {
                final View child = decor.getChildAt(i);
                if (child instanceof ViewStub) continue;
                if (!child.isShown()) continue;
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

    private class OnSwipeListenerImpl implements OnSwipeListener {
        private float mFromScale = 1F;
        private float mToScale = 1F;

        @Override
        public void onStart(@NonNull DialogLayer layer) {
            View decorChild = getViewHolder().getDecorChild();
            decorChild.setPivotX(decorChild.getWidth() / 2F);
            decorChild.setPivotY(decorChild.getHeight() / 2F);
            mFromScale = 1F;
            float dcHeight = decorChild.getHeight();
            float statusBarHeight = Utils.getStatusBarHeight(getActivity());
            mToScale = (dcHeight - statusBarHeight * 2F) / dcHeight;
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
