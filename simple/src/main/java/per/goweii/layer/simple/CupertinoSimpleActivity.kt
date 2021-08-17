package per.goweii.layer.simple

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import per.goweii.actionbarex.common.ActionBarCommon
import per.goweii.layer.core.ktx.onBindData
import per.goweii.layer.core.ktx.onClickToDismiss
import per.goweii.layer.design.cupertino.*
import per.goweii.layer.dialog.ktx.contentView

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
        CupertinoPopoverLayer(view)
                .contentView(R.layout.popup_meun)
                .setUseDefaultConfig()
                .setSolidColor(resources.getColor(R.color.colorPopupBg))
                .show()
    }

    fun onBtnModalityClick(view: View) {
        CupertinoModalityLayer(this)
                .contentView(R.layout.dialog_fullscreen)
                .onBindData {
                    val actionBar = requireView<ActionBarCommon>(R.id.dialog_actionbar)
                    actionBar.leftTextView.setOnClickListener { dismiss() }
                    actionBar.rightTextView.setOnClickListener { dismiss() }
                }
                .show()
    }

    fun onBtnToastClick(view: View) {
        CupertinoToastLayer(this)
                .setIcon(R.drawable.ic_success_big)
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