package per.goweii.layer.core.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import per.goweii.layer.core.Layer;

public class CommonAnimatorCreator implements Layer.AnimatorCreator {

    public interface Attr {
        @NonNull
        Animator createIn(@NonNull View target);

        @NonNull
        Animator createOut(@NonNull View target);
    }

    public static class AlphaAttr implements Attr {
        private float mFrom = 0F;
        private float mTo = 1F;

        private TimeInterpolator mInTimeInterpolator = null;
        private TimeInterpolator mOutTimeInterpolator = null;

        public AlphaAttr setFrom(float from) {
            this.mFrom = from;
            return this;
        }

        public AlphaAttr setTo(float to) {
            this.mTo = to;
            return this;
        }

        public AlphaAttr setTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mInTimeInterpolator = timeInterpolator;
            this.mOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public AlphaAttr setInTimeInterpolator(TimeInterpolator inTimeInterpolator) {
            this.mInTimeInterpolator = inTimeInterpolator;
            return this;
        }

        public AlphaAttr setOutTimeInterpolator(TimeInterpolator outTimeInterpolator) {
            this.mOutTimeInterpolator = outTimeInterpolator;
            return this;
        }

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", mFrom, mTo);
            alpha.setInterpolator(mInTimeInterpolator);
            return alpha;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), mFrom);
            alpha.setInterpolator(mOutTimeInterpolator);
            return alpha;
        }
    }

    public static class ScaleAttr implements Attr {
        private float mFromX = 0F;
        private float mFromY = 0F;
        private float mToX = 1F;
        private float mToY = 1F;

        private float mPivotX = 0F;
        private float mPivotY = 0F;
        private float mPivotPercentX = 0.5F;
        private float mPivotPercentY = 0.5F;

        private boolean mUsePivotPercent = true;

        public ScaleAttr setFrom(float from) {
            return setFrom(from, from);
        }

        public ScaleAttr setFrom(float fromX, float fromY) {
            this.mFromX = fromX;
            this.mFromY = fromY;
            return this;
        }

        public ScaleAttr setTo(float to) {
            return setTo(to, to);
        }

        public ScaleAttr setTo(float toX, float toY) {
            this.mToX = toX;
            this.mToY = toY;
            return this;
        }

        public ScaleAttr setPivot(float pivot) {
            return setPivot(pivot, pivot);
        }

        public ScaleAttr setPivot(float pivotX, float pivotY) {
            this.mUsePivotPercent = false;
            this.mPivotX = pivotX;
            this.mPivotY = pivotY;
            return this;
        }

        public ScaleAttr setPivotPercent(float pivotPercent) {
            return setPivotPercent(pivotPercent, pivotPercent);
        }

        public ScaleAttr setPivotPercent(float pivotPercentX, float pivotPercentY) {
            this.mUsePivotPercent = true;
            this.mPivotPercentX = pivotPercentX;
            this.mPivotPercentY = pivotPercentY;
            return this;
        }

        public float getPivotX(@NonNull View target) {
            float pivot;
            if (mUsePivotPercent) {
                pivot = target.getWidth() * mPivotPercentX;
            } else {
                pivot = mPivotX;
            }
            return pivot;
        }

        public float getPivotY(@NonNull View target) {
            float pivot;
            if (mUsePivotPercent) {
                pivot = target.getHeight() * mPivotPercentY;
            } else {
                pivot = mPivotY;
            }
            return pivot;
        }

        private TimeInterpolator xInTimeInterpolator = null;
        private TimeInterpolator yInTimeInterpolator = null;
        private TimeInterpolator xOutTimeInterpolator = null;
        private TimeInterpolator yOutTimeInterpolator = null;

        public ScaleAttr setTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", mFromX, mToX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", mFromY, mToY);
            target.setPivotX(getPivotX(target));
            target.setPivotY(getPivotY(target));
            scaleX.setInterpolator(xInTimeInterpolator);
            scaleY.setInterpolator(yInTimeInterpolator);
            set.playTogether(scaleX, scaleY);
            return set;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), mFromX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), mFromY);
            target.setPivotX(getPivotX(target));
            target.setPivotY(getPivotY(target));
            scaleX.setInterpolator(xOutTimeInterpolator);
            scaleY.setInterpolator(yOutTimeInterpolator);
            set.playTogether(scaleX, scaleY);
            return set;
        }
    }

    public static class TranslationAttr implements Attr {
        private float mFromX = 0F;
        private float mFromY = 0F;
        private float mToX = 0F;
        private float mToY = Float.MAX_VALUE;
        private float mFromPercentX = 0F;
        private float mFromPercentY = 0F;
        private float mToPercentX = 0F;
        private float mToPercentY = 1F;

        private boolean mUseFromPercent = true;
        private boolean mUseToPercent = true;

        public TranslationAttr setFrom(float from) {
            return setFrom(from, from);
        }

        public TranslationAttr setFrom(float fromX, float fromY) {
            this.mUseFromPercent = false;
            this.mFromX = fromX;
            this.mFromY = fromY;
            return this;
        }

        public TranslationAttr setFromPercent(float fromPercent) {
            return setFromPercent(fromPercent, fromPercent);
        }

        public TranslationAttr setFromPercent(float fromPercentX, float fromPercentY) {
            this.mUseFromPercent = true;
            this.mFromPercentX = fromPercentX;
            this.mFromPercentY = fromPercentY;
            return this;
        }

        public TranslationAttr setTo(float to) {
            return setTo(to, to);
        }

        public TranslationAttr setTo(float toX, float toY) {
            this.mUseToPercent = false;
            this.mToX = toX;
            this.mToY = toY;
            return this;
        }

        public TranslationAttr setToPercent(float toPercent) {
            return setToPercent(toPercent, toPercent);
        }

        public TranslationAttr setToPercent(float toPercentX, float toPercentY) {
            this.mUseToPercent = true;
            this.mToPercentX = toPercentX;
            this.mToPercentY = toPercentY;
            return this;
        }

        public float getFromX(@NonNull View target) {
            float from;
            if (mUseFromPercent) {
                from = target.getWidth() * mFromPercentX;
            } else {
                from = mFromX;
            }
            return from;
        }

        public float getFromY(@NonNull View target) {
            float from;
            if (mUseFromPercent) {
                from = target.getHeight() * mFromPercentY;
            } else {
                from = mFromY;
            }
            return from;
        }

        public float getToX(@NonNull View target) {
            float to;
            if (mUseToPercent) {
                to = target.getWidth() * mToPercentX;
            } else {
                to = mToX;
            }
            return to;
        }

        public float getToY(@NonNull View target) {
            float to;
            if (mUseToPercent) {
                to = target.getHeight() * mToPercentY;
            } else {
                to = mToY;
            }
            return to;
        }

        private TimeInterpolator mXInTimeInterpolator = null;
        private TimeInterpolator mYInTimeInterpolator = null;
        private TimeInterpolator mXOutTimeInterpolator = null;
        private TimeInterpolator mYOutTimeInterpolator = null;

        public TranslationAttr setTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXInTimeInterpolator = timeInterpolator;
            this.mYInTimeInterpolator = timeInterpolator;
            this.mXOutTimeInterpolator = timeInterpolator;
            this.mYOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXInTimeInterpolator = timeInterpolator;
            this.mYInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXOutTimeInterpolator = timeInterpolator;
            this.mYOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXInTimeInterpolator = timeInterpolator;
            this.mXOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mYInTimeInterpolator = timeInterpolator;
            this.mYOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mXOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mYInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.mYOutTimeInterpolator = timeInterpolator;
            return this;
        }

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", getFromX(target), getToX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", getFromY(target), getToY(target));
            translationX.setInterpolator(mXInTimeInterpolator);
            translationY.setInterpolator(mYInTimeInterpolator);
            set.playTogether(translationX, translationY);
            return set;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), getFromX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), getFromY(target));
            translationX.setInterpolator(mXOutTimeInterpolator);
            translationY.setInterpolator(mYOutTimeInterpolator);
            set.playTogether(translationX, translationY);
            return set;
        }
    }

    private final List<Attr> mAttrs = new ArrayList<>();
    private TimeInterpolator mInTimeInterpolator = null;
    private TimeInterpolator mOutTimeInterpolator = null;

    public CommonAnimatorCreator addAttr(Attr attr) {
        this.mAttrs.add(attr);
        return this;
    }

    public CommonAnimatorCreator setTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.mInTimeInterpolator = timeInterpolator;
        this.mOutTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator setInTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.mInTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.mOutTimeInterpolator = timeInterpolator;
        return this;
    }

    @NonNull
    @Override
    public Animator createInAnimator(@NonNull View target) {
        List<Animator> animators = new ArrayList<>(mAttrs.size());
        for (Attr attr : mAttrs) {
            animators.add(attr.createIn(target));
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(mInTimeInterpolator);
        return set;
    }

    @NonNull
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        List<Animator> animators = new ArrayList<>(mAttrs.size());
        for (Attr attr : mAttrs) {
            animators.add(attr.createOut(target));
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(mOutTimeInterpolator);
        return set;
    }
}
