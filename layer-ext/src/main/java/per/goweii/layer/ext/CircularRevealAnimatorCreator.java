package per.goweii.layer.ext;

import android.animation.Animator;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import per.goweii.layer.Layer;
import per.goweii.layer.utils.AnimatorHelper;

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
