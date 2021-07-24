package per.goweii.layer.core.anim;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.layer.core.Layer;

/**
 * @author CuiZhen
 */
public class NullAnimatorCreator implements Layer.AnimatorCreator {
    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        return null;
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        return null;
    }
}
