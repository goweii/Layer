package per.goweii.layer.notification.ktx

import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import per.goweii.layer.core.widget.SwipeLayout
import per.goweii.layer.notification.DefaultNotificationOnSwipeListener
import per.goweii.layer.notification.NotificationLayer
import per.goweii.layer.notification.NotificationLayer.SwipeTransformer

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

fun <T : NotificationLayer> T.removeOthers(removeOthers: Boolean) = this.apply {
    this.setRemoveOthers(removeOthers)
}

fun <T : NotificationLayer> T.swipeDirection(@SwipeLayout.Direction direction: Int) = this.apply {
    this.setSwipeDirection(direction)
}

fun <T : NotificationLayer> T.swipeTransformer(swipeTransformer: SwipeTransformer) = this.apply {
    this.setSwipeTransformer(swipeTransformer)
}

fun <T : NotificationLayer> T.duration(duration: Long) = this.apply {
    this.setDuration(duration)
}

fun <T : NotificationLayer> T.onNotificationClick(onClick: T.(view: View) -> Unit) =
    this.apply {
        this.setOnNotificationClickListener { _, view -> this.onClick(view) }
    }

fun <T : NotificationLayer> T.onNotificationLongClick(onLongClick: T.(view: View) -> Boolean) =
    this.apply {
        this.setOnNotificationLongClickListener { _, view -> this.onLongClick(view) }
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

fun <T : NotificationLayer> T.onSwiping(onSwiping: T.(direction: Int, fraction: Float) -> Unit) =
    this.apply {
        this.addOnSwipeListener(object : DefaultNotificationOnSwipeListener() {
            override fun onSwiping(
                layer: NotificationLayer,
                @SwipeLayout.Direction direction: Int,
                @FloatRange(from = 0.0, to = 1.0) fraction: Float
            ) {
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