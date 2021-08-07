package per.goweii.layer.dialog.ktx

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.*
import per.goweii.layer.core.Layer
import per.goweii.layer.core.anim.AnimStyle
import per.goweii.layer.core.widget.SwipeLayout
import per.goweii.layer.dialog.DefaultDialogOnSwipeListener
import per.goweii.layer.dialog.DialogLayer

fun <T : DialogLayer> T.cancelableOnTouchOutside(enable: Boolean) = this.apply {
    this.setCancelableOnTouchOutside(enable)
}

fun <T : DialogLayer> T.contentView(contentView: View?) = this.apply {
    this.setContentView(contentView)
}

fun <T : DialogLayer> T.contentView(@LayoutRes contentViewId: Int) = this.apply {
    this.setContentView(contentViewId)
}

fun <T : DialogLayer> T.backgroundView(backgroundView: View?) = this.apply {
    this.setBackgroundView(backgroundView)
}

fun <T : DialogLayer> T.backgroundView(@LayoutRes backgroundViewId: Int) = this.apply {
    this.setBackgroundView(backgroundViewId)
}

fun <T : DialogLayer> T.avoidStatusBar(enable: Boolean) = this.apply {
    this.setAvoidStatusBar(enable)
}

fun <T : DialogLayer> T.gravity(gravity: Int) = this.apply {
    this.setGravity(gravity)
}

fun <T : DialogLayer> T.swipeDismiss(@SwipeLayout.Direction swipeDirection: Int) = this.apply {
    this.setSwipeDismiss(swipeDirection)
}

fun <T : DialogLayer> T.swipeTransformer(swipeTransformer: DialogLayer.SwipeTransformer) = this.apply {
    this.setSwipeTransformer(swipeTransformer)
}

fun <T : DialogLayer> T.onSwipeStart(onStart: T.() -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultDialogOnSwipeListener() {
        override fun onStart(layer: DialogLayer) {
            this@apply.onStart()
        }
    })
}

fun <T : DialogLayer> T.onSwiping(onSwiping: T.(direction: Int, fraction: Float) -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultDialogOnSwipeListener() {
        override fun onSwiping(layer: DialogLayer,
                               @SwipeLayout.Direction direction: Int,
                               @FloatRange(from = 0.0, to = 1.0) fraction: Float) {
            this@apply.onSwiping(direction, fraction)
        }
    })
}

fun <T : DialogLayer> T.onSwipeEnd(onEnd: T.(direction: Int) -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultDialogOnSwipeListener() {
        override fun onEnd(layer: DialogLayer, @SwipeLayout.Direction direction: Int) {
            this@apply.onEnd(direction)
        }
    })
}

fun <T : DialogLayer> T.animStyle(animStyle: AnimStyle?) = this.apply {
    this.setContentAnimator(animStyle)
}

fun <T : DialogLayer, R : Animator?> T.contentAnimator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.setContentAnimator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : DialogLayer> T.contentAnimator(creator: Layer.AnimatorCreator) = this.apply {
    this.setContentAnimator(creator)
}

fun <T : DialogLayer, R : Animator?> T.backgroundAnimator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.setBackgroundAnimator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : DialogLayer> T.backgroundAnimator(creator: Layer.AnimatorCreator) = this.apply {
    this.setBackgroundAnimator(creator)
}

fun <T : DialogLayer> T.backgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) = this.apply {
    this.setBackgroundDimAmount(dimAmount)
}

fun <T : DialogLayer> T.backgroundDimDefault() = this.apply {
    this.setBackgroundDimDefault()
}

fun <T : DialogLayer> T.backgroundBitmap(bitmap: Bitmap) = this.apply {
    this.setBackgroundView(ImageView(activity).also {
        it.scaleType = ImageView.ScaleType.CENTER_CROP
        it.setImageBitmap(bitmap)
    })
}

fun <T : DialogLayer> T.backgroundResource(@DrawableRes resource: Int) = this.apply {
    this.setBackgroundView(ImageView(activity).also {
        it.scaleType = ImageView.ScaleType.CENTER_CROP
        it.setImageResource(resource)
    })
}

fun <T : DialogLayer> T.backgroundDrawable(drawable: Drawable?) = this.apply {
    this.setBackgroundView(ImageView(activity).also {
        it.scaleType = ImageView.ScaleType.CENTER_CROP
        it.setImageDrawable(drawable)
    })
}

fun <T : DialogLayer> T.backgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.setBackgroundColorInt(colorInt)
}

fun <T : DialogLayer> T.backgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.setBackgroundColorRes(colorRes)
}

fun <T : DialogLayer> T.outsideInterceptTouchEvent(enable: Boolean) = this.apply {
    this.setOutsideInterceptTouchEvent(enable)
}

fun <T : DialogLayer> T.onOutsideTouch(onOutsideTouched: T.() -> Unit) = this.apply {
    this.setOnOutsideTouchListener { this@apply.onOutsideTouched() }
}

fun <T : DialogLayer> T.outsideTouchToDismiss(enable: Boolean) = this.apply {
    this.setOutsideTouchToDismiss(enable)
}