package per.goweii.layer.simple

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import per.goweii.layer.core.anim.DelayedZoomAnimatorCreator
import per.goweii.layer.core.ktx.cancelableOnClickKeyBack
import per.goweii.layer.design.material.MaterialDialogLayer
import per.goweii.layer.design.material.MaterialNotificationLayer
import per.goweii.layer.design.material.MaterialPopupLayer
import per.goweii.layer.design.material.MaterialToastLayer
import per.goweii.layer.dialog.ktx.contentAnimator
import per.goweii.layer.dialog.ktx.contentView

class MaterialSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_material_simple)
    }

    fun onBtnDialogClick(view: View) {
        MaterialDialogLayer(this)
                .setTitle(R.string.dialog_title)
                .setDesc(R.string.dialog_msg)
                .addAction(R.string.i_know) { layer, _ -> layer.dismiss() }
                .addAction(R.string.send) { layer, _ -> layer.dismiss() }
                .addAction(R.string.close) { layer, _ -> layer.dismiss() }
                .show()
    }

    fun onBtnPopupClick(view: View) {
        MaterialPopupLayer(view)
                .contentView(R.layout.popup_meun)
                .contentAnimator(DelayedZoomAnimatorCreator().setCenterPercentX(0.5F))
                .show()
    }

    fun onBtnToastClick(view: View) {
        MaterialToastLayer(this)
                .setIcon(R.drawable.ic_success)
                .setMessage(R.string.toast_msg)
                .show()
    }

    fun onBtnNotificationClick(view: View) {
        MaterialNotificationLayer(this)
                .setIcon(R.drawable.ic_notification)
                .setLabel(R.string.app_name)
                .setTitle(R.string.notification_title)
                .setDesc(R.string.notification_desc)
                .setTimePattern("yyyy-MM-dd")
                .setOnNotificationClickListener { layer, _ -> layer.dismiss() }
                .show()
    }
}