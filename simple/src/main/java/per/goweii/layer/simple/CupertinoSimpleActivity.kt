package per.goweii.layer.simple

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import per.goweii.layer.design.cupertino.CupertinoAlertLayer
import per.goweii.layer.design.cupertino.CupertinoNotificationLayer
import per.goweii.layer.design.cupertino.CupertinoToastLayer
import per.goweii.layer.design.material.MaterialNotificationLayer

class CupertinoSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cupertino_simple)
    }

    fun onBtnDialogClick(view: View) {
        CupertinoAlertLayer(this)
                .setTitle(R.string.dialog_title)
                .setDesc(R.string.dialog_msg)
                .addAction(R.string.i_know) { layer, _ ->
                    layer.dismiss()
                }
                .addAction(R.string.send) { layer, _ ->
                    layer.dismiss()
                }
                .addAction(R.string.close) { layer, _ ->
                    layer.dismiss()
                }
                .show()
    }

    fun onBtnPopoverClick(view: View) {
    }

    fun onBtnModalityClick(view: View) {
    }

    fun onBtnToastClick(view: View) {
        CupertinoToastLayer(this)
                .setIcon(R.drawable.ic_warning)
                .setMessage(R.string.toast_msg)
                .show()
    }

    fun onBtnNotificationClick(view: View) {
        CupertinoNotificationLayer(this)
                .setIcon(R.drawable.ic_notification)
                .setLabel(R.string.app_name)
                .setTitle(R.string.notification_title)
                .setDesc(R.string.notification_desc)
                .setTimePattern("yyyy-MM-dd")
                .setOnNotificationClickListener { layer, _ -> layer.dismiss() }
                .show()
    }
}