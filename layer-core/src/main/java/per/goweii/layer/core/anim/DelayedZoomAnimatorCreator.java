package per.goweii.layer.core.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.Layer;

public class DelayedZoomAnimatorCreator implements Layer.AnimatorCreator {
    private boolean mUsePercentX = true;
    private boolean mUsePercentY = true;
    private float mCenterPercentX = 0.0F;
    private float mCenterPercentY = 0.0F;
    private int mCenterX = 0;
    private int mCenterY = 0;

    public DelayedZoomAnimatorCreator setCenterPercentX(float centerPercentX) {
        mUsePercentX = true;
        mCenterPercentX = centerPercentX;
        return this;
    }

    public DelayedZoomAnimatorCreator setCenterPercentY(float centerPercentY) {
        mUsePercentY = true;
        mCenterPercentY = centerPercentY;
        return this;
    }

    public DelayedZoomAnimatorCreator setCenterX(int centerX) {
        mUsePercentX = false;
        mCenterX = centerX;
        return this;
    }

    public DelayedZoomAnimatorCreator setCenterY(int centerY) {
        mUsePercentY = false;
        mCenterY = centerY;
        return this;
    }

    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        int w = target.getWidth();
        int h = target.getHeight();
        int centerX = mUsePercentX ? (int) (mCenterPercentX * w) : mCenterX;
        int centerY = mUsePercentY ? (int) (mCenterPercentY * h) : mCenterY;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        if (target instanceof ViewGroup) {
            final ViewGroup targetGroup = (ViewGroup) target;
            for (int i = 0; i < targetGroup.getChildCount(); i++) {
                View targetChild = targetGroup.getChildAt(i);
                targetChild.setAlpha(0);
            }
            scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private boolean isChildAnimStart = false;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float f = animation.getAnimatedFraction();
                    if (!isChildAnimStart && f > 0.618F) {
                        isChildAnimStart = true;
                        final List<Animator> childAnimators = new ArrayList<>(targetGroup.getChildCount());
                        for (int i = 0; i < targetGroup.getChildCount(); i++) {
                            View targetChild = targetGroup.getChildAt(i);
                            ObjectAnimator alphaChild = ObjectAnimator.ofFloat(targetChild, "alpha", 0, 1);
                            alphaChild.setInterpolator(new DecelerateInterpolator(1.5F));
                            alphaChild.setStartDelay(18 * i);
                            alphaChild.setDuration(50);
                            childAnimators.add(alphaChild);
                        }
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(childAnimators);
                        set.start();
                    }
                }
            });
        }
        return set;
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        int w = target.getWidth();
        int h = target.getHeight();
        int centerX = mUsePercentX ? (int) (mCenterPercentX * w) : mCenterX;
        int centerY = mUsePercentY ? (int) (mCenterPercentY * h) : mCenterY;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        if (target instanceof ViewGroup) {
            final ViewGroup targetGroup = (ViewGroup) target;
            scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private boolean isChildAnimStart = false;

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!isChildAnimStart) {
                        isChildAnimStart = true;
                        final List<Animator> childAnimators = new ArrayList<>(targetGroup.getChildCount());
                        for (int i = targetGroup.getChildCount() - 1; i >= 0; i--) {
                            View targetChild = targetGroup.getChildAt(i);
                            ObjectAnimator alphaChild = ObjectAnimator.ofFloat(targetChild, "alpha", targetChild.getAlpha(), 0);
                            alphaChild.setInterpolator(new AccelerateInterpolator(1.5F));
                            alphaChild.setStartDelay(18 * (targetGroup.getChildCount() - 1 - i));
                            alphaChild.setDuration(50);
                            childAnimators.add(alphaChild);
                        }
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(childAnimators);
                        set.start();
                    }
                }
            });
        }
        return set;
    }
}
