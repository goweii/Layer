package per.goweii.layer.ktx

import android.view.View
import androidx.annotation.*
import per.goweii.layer.toast.ToastLayer

fun <T : ToastLayer> T.contentView(contentView: View) = this.apply {
    this.setContentView(contentView)
}

fun <T : ToastLayer> T.contentView(@LayoutRes contentViewId: Int) = this.apply {
    this.setContentView(contentViewId)
}

fun <T : ToastLayer> T.removeOthers(removeOthers: Boolean) = this.apply {
    this.setRemoveOthers(removeOthers)
}

fun <T : ToastLayer> T.duration(duration: Long) = this.apply {
    this.setDuration(duration)
}

fun <T : ToastLayer> T.gravity(gravity: Int) = this.apply {
    this.setGravity(gravity)
}

fun <T : ToastLayer> T.marginLeft(margin: Int?) = this.apply {
    this.setMarginLeft(margin)
}

fun <T : ToastLayer> T.marginTop(margin: Int?) = this.apply {
    this.setMarginTop(margin)
}

fun <T : ToastLayer> T.marginRight(margin: Int?) = this.apply {
    this.setMarginRight(margin)
}

fun <T : ToastLayer> T.marginBottom(margin: Int?) = this.apply {
    this.setMarginBottom(margin)
}

fun <T : ToastLayer> T.alpha(alpha: Float) = this.apply {
    this.setAlpha(alpha)
}
