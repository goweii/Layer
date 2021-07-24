package per.goweii.layer.notification.ktx

import android.view.View
import androidx.annotation.*
import per.goweii.layer.notification.NotificationLayer
import per.goweii.layer.core.widget.SwipeLayout
import per.goweii.layer.notification.DefaultNotificationOnSwipeListener

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