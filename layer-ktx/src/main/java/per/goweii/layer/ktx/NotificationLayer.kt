package per.goweii.layer.ktx

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import per.goweii.layer.ext.DefaultNotificationOnSwipeListener
import per.goweii.layer.notification.NotificationLayer
import per.goweii.layer.widget.SwipeLayout

fun <T : NotificationLayer> T.contentView(contentView: View) = this.apply {
    this.setContentView(contentView)
}

fun <T : NotificationLayer> T.contentView(@LayoutRes contentViewId: Int) = this.apply {
    this.setContentView(contentViewId)
}

fun <T : NotificationLayer> T.maxWidth(maxWidth: Int) = this.apply {
    this.setMaxWidth(maxWidth)
}

fun <T : NotificationLayer> T.maxHeight(maxHeight: Int) = this.apply {
    this.setMaxHeight(maxHeight)
}

fun <T : NotificationLayer> T.contentBlurRadius(@FloatRange(from = 0.0) radius: Float) = this.apply {
    this.setContentBlurRadius(radius)
}

fun <T : NotificationLayer> T.contentBlurPercent(@FloatRange(from = 0.0) percent: Float) = this.apply {
    this.setContentBlurPercent(percent)
}

fun <T : NotificationLayer> T.contentBlurScale(@FloatRange(from = 1.0) scale: Float) = this.apply {
    this.setContentBlurSimple(scale)
}

fun <T : NotificationLayer> T.contentBlurCornerRadius(@FloatRange(from = 0.0) radius: Float, unit: Int) = this.apply {
    this.setContentBlurCornerRadius(radius, unit)
}

fun <T : NotificationLayer> T.contentBlurCornerRadiusPx(@FloatRange(from = 0.0) radius: Float) = this.apply {
    this.setContentBlurCornerRadiusPx(radius)
}

fun <T : NotificationLayer> T.contentBlurCornerRadiusDp(@FloatRange(from = 0.0) radius: Float) = this.apply {
    this.setContentBlurCornerRadiusDp(radius)
}

fun <T : NotificationLayer> T.contentBlurColorInt(@ColorInt colorInt: Int) = this.apply {
    this.setContentBlurColorInt(colorInt)
}

fun <T : NotificationLayer> T.contentBlurColorRes(@ColorRes colorRes: Int) = this.apply {
    this.setContentBlurColorRes(colorRes)
}

fun <T : NotificationLayer> T.icon(@DrawableRes icon: Int) = this.apply {
    this.setIcon(icon)
}

fun <T : NotificationLayer> T.icon(icon: Drawable) = this.apply {
    this.setIcon(icon)
}

fun <T : NotificationLayer> T.label(label: CharSequence) = this.apply {
    this.setLabel(label)
}

fun <T : NotificationLayer> T.label(@StringRes label: Int) = this.apply {
    this.setLabel(label)
}

fun <T : NotificationLayer> T.time(time: CharSequence) = this.apply {
    this.setTime(time)
}

fun <T : NotificationLayer> T.timePattern(pattern: String) = this.apply {
    this.setTimePattern(pattern)
}

fun <T : NotificationLayer> T.title(title: CharSequence) = this.apply {
    this.setTitle(title)
}

fun <T : NotificationLayer> T.title(@StringRes title: Int) = this.apply {
    this.setTitle(title)
}

fun <T : NotificationLayer> T.desc(desc: CharSequence) = this.apply {
    this.setDesc(desc)
}

fun <T : NotificationLayer> T.desc(@StringRes desc: Int) = this.apply {
    this.setDesc(desc)
}

fun <T : NotificationLayer> T.duration(duration: Long) = this.apply {
    this.setDuration(duration)
}

fun <T : NotificationLayer> T.onNotificationClick(onNotificationClick: T.(view: View) -> Unit) = this.apply {
    this.setOnNotificationClickListener { _, view -> this.onNotificationClick(view) }
}

fun <T : NotificationLayer> T.onNotificationLongClick(onNotificationClick: T.(view: View) -> Boolean) = this.apply {
    this.setOnNotificationLongClickListener { _, view -> this.onNotificationClick(view) }
}

fun <T : NotificationLayer> T.autoDismiss(autoDismiss: Boolean) = this.apply {
    this.setAutoDismiss(autoDismiss)
}

fun <T : NotificationLayer> T.swipeTransformer(swipeTransformer: NotificationLayer.SwipeTransformer) = this.apply {
    this.setSwipeTransformer(swipeTransformer)
}

fun <T : NotificationLayer> T.onSwipeStart(onStart: T.() -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultNotificationOnSwipeListener() {
        override fun onStart(layer: NotificationLayer) {
            this@apply.onStart()
        }
    })
}

fun <T : NotificationLayer> T.onSwiping(onSwiping: T.(direction: Int, fraction: Float) -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultNotificationOnSwipeListener() {
        override fun onSwiping(layer: NotificationLayer,
                               @SwipeLayout.Direction direction: Int,
                               @FloatRange(from = 0.0, to = 1.0) fraction: Float) {
            this@apply.onSwiping(direction, fraction)
        }
    })
}

fun <T : NotificationLayer> T.onSwipeEnd(onEnd: T.(direction: Int) -> Unit) = this.apply {
    this.addOnSwipeListener(object : DefaultNotificationOnSwipeListener() {
        override fun onEnd(layer: NotificationLayer, @SwipeLayout.Direction direction: Int) {
            this@apply.onEnd(direction)
        }
    })
}