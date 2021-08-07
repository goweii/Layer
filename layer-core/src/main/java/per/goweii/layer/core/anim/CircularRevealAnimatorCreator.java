package per.goweii.layer.core.anim;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import per.goweii.layer.core.Layer;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularRevealAnimatorCreator implements Layer.AnimatorCreator {
    private boolean mUsePercentX = true;
    private boolean mUsePercentY = true;
    private float mCenterPercentX = 0.5F;
    private float mCenterPercentY = 0.5F;
    private int mCenterX = 0;
    private int mCenterY = 0;
    private TimeInterpolator mInTimeInterpolator = null;
    private TimeInterpolator mOutTimeInterpolator = null;

    public CircularRevealAnimatorCreator setCenterPercentX(float centerPercentX) {
        mUsePercentX = true;
        mCenterPercentX = centerPercentX;
        return this;
    }

    public CircularRevealAnimatorCreator setCenterPercentY(float centerPercentY) {
        mUsePercentY = true;
        mCenterPercentY = centerPercentY;
        return this;
    }

    public CircularRevealAnimatorCreator setCenterX(int centerX) {
        mUsePercentX = false;
        mCenterX = centerX;
        return this;
    }

    public CircularRevealAnimatorCreator setCenterY(int centerY) {
        mUsePercentY = false;
        mCenterY = centerY;
        return this;
    }

    public void setInTimeInterpolator(TimeInterpolator inTimeInterpolator) {
        mInTimeInterpolator = inTimeInterpolator;
    }

    public void setOutTimeInterpolator(TimeInterpolator outTimeInterpolator) {
        mOutTimeInterpolator = outTimeInterpolator;
    }

    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        int x = target.getWidth();
        int y = target.getHeight();
        int centerX = mUsePercentX ? (int) (mCenterPercentX * x) : mCenterX;
        int centerY = mUsePercentY ? (int) (mCenterPercentY * y) : mCenterY;
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        if (mInTimeInterpolator != null) {
            animator.setInterpolator(mInTimeInterpolator);
        } else {
            animator.setInterpolator(new DecelerateInterpolator());
        }
        return animator;
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        int x = target.getWidth();
        int y = target.getHeight();
        int centerX = mUsePercentX ? (int) (mCenterPercentX * x) : mCenterX;
        int centerY = mUsePercentY ? (int) (mCenterPercentY * y) : mCenterY;
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        if (mOutTimeInterpolator != null) {
            animator.setInterpolator(mOutTimeInterpolator);
        } else {
            animator.setInterpolator(new AccelerateInterpolator());
        }
        return animator;
    }
}
