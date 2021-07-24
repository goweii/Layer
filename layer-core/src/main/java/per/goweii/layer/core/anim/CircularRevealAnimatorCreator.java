package per.goweii.layer.core.anim;

import android.animation.Animator;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import per.goweii.layer.core.Layer;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularRevealAnimatorCreator implements Layer.AnimatorCreator {
    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        return AnimatorHelper.createCircularRevealInAnim(target);
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        return AnimatorHelper.createCircularRevealOutAnim(target);
    }
}
