package per.goweii.layer.simple

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import per.goweii.layer.core.anim.AnimStyle
import per.goweii.layer.core.anim.DelayedZoomAnimatorCreator
import per.goweii.layer.core.anim.NullAnimatorCreator
import per.goweii.layer.core.ktx.onClickToDismiss
import per.goweii.layer.core.ktx.onPreDismiss
import per.goweii.layer.core.ktx.onPreShow
import per.goweii.layer.core.widget.SwipeLayout
import per.goweii.layer.design.cupertino.CupertinoNotificationLayer
import per.goweii.layer.design.material.MaterialNotificationLayer
import per.goweii.layer.design.material.MaterialToastLayer
import per.goweii.layer.dialog.DialogLayer
import per.goweii.layer.dialog.ktx.*
import per.goweii.layer.guide.GuideLayer
import per.goweii.layer.guide.ktx.onClick
import per.goweii.layer.keyboard.KeyboardLayer
import per.goweii.layer.overlay.OverlayLayer
import per.goweii.layer.overlay.ktx.*
import per.goweii.layer.popup.PopupLayer
import per.goweii.layer.toast.ToastLayer

class LayersSimpleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layers_simple)
        OverlayLayer(this)
                .overlayView(R.layout.overlay)
                .snapEdge(OverlayLayer.Edge.ALL)
                .normalAlpha(1F)
                .normalScale(1F)
                .defAlpha(0.95F)
                .defPercentX(1F)
                .defPercentY(0.618F)
                .lowProfileAlpha(0.6F)
                .lowProfileDelay(3000)
                .lowProfileIndent(0.2F)
                .lowProfileScale(0.9F)
                .show()
    }

    fun onBtnDialogClick(view: View) {
        DialogLayer(this)
                .contentView(R.layout.dialog_normal)
                .backgroundDimDefault()
                .onClickToDismiss(R.id.fl_dialog_no)
                .onClickToDismiss(R.id.fl_dialog_yes)
                .show()
    }

    fun onBtnBottomSheetClick(view: View) {
        DialogLayer(this)
                .contentView(R.layout.bottom_sheet)
                .gravity(Gravity.BOTTOM)
                .swipeDismiss(SwipeLayout.Direction.BOTTOM)
                .backgroundDimDefault()
                .animStyle(AnimStyle.BOTTOM)
                .onClickToDismiss(R.id.bottom_sheet_fl_close)
                .show()
    }

    fun onBtnSlidePanelClick(view: View) {
        DialogLayer(this)
                .contentView(R.layout.slide_panel)
                .gravity(Gravity.LEFT)
                .swipeDismiss(SwipeLayout.Direction.LEFT)
                .backgroundDimDefault()
                .animStyle(AnimStyle.LEFT)
                .onClickToDismiss(R.id.slide_panel_tv_close)
                .show()
    }

    fun onBtnInputClick(view: View) {
        DialogLayer(this)
                .contentView(R.layout.dialog_input)
                .contentAnimator(NullAnimatorCreator())
                .backgroundDimDefault()
                .addSoftInputCompat(true)
                .onPreShow {
                    val et = requireView<EditText>(R.id.et_input)
                    et.requestFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(et, InputMethodManager.SHOW_FORCED)
                }
                .onPreDismiss {
                    val et = requireView<EditText>(R.id.et_input)
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(et.windowToken, 0)
                }
                .show()
    }

    fun onBtnPopupClick(view: View) {
        PopupLayer(view)
                .contentView(R.layout.popup_meun)
                .show()
    }

    fun onBtnToastClick(view: View) {
        ToastLayer(this)
                .setContentView(R.layout.toast)
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

    fun onBtnGuideClick(view: View) {
        GuideLayer(this)
                .addMapping(GuideLayer.Mapping()
                        .setTargetView(view)
                        .setGuideView(R.layout.guide_content)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ABOVE)
                        .setCornerRadius(24F)
                )
                .addMapping(GuideLayer.Mapping()
                        .setGuideView(R.layout.guide_i_know)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER_PARENT)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ALIGN_PARENT_BOTTOM)
                        .onClick(R.id.guide_btn_i_know) { dismiss() }
                        .setMarginBottom(30)
                )
                .show()
    }

    fun onBtnKeyboardClick(view: View) {
        KeyboardLayer(this)
                .setInputType(KeyboardLayer.InputType.LATTER)
                .show()
    }
}