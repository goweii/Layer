package per.goweii.layer.simple;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Random;

import per.goweii.layer.Layers;
import per.goweii.layer.Layer;
import per.goweii.layer.LayerActivity;
import per.goweii.layer.design.cupertino.CupertinoNotificationLayer;
import per.goweii.layer.design.cupertino.CupertinoToastLayer;
import per.goweii.layer.dialog.DialogLayer;
import per.goweii.layer.ext.CircularRevealAnimatorCreator;
import per.goweii.layer.ext.SimpleAnimatorCreator;
import per.goweii.layer.notification.NotificationLayer;
import per.goweii.layer.overlay.OverlayLayer;
import per.goweii.layer.popup.PopupLayer.Align;
import per.goweii.layer.utils.AnimatorHelper;

public class NormalActivity extends AppCompatActivity implements View.OnClickListener {
    private final Random mRandom = new Random();

    private DialogLayer anyLayer_show_target_right = null;
    private boolean anyLayer_show_target_right_shown = false;
    private DialogLayer anyLayer_show_target_bottom = null;
    private Layer layer_show_blur_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_show_toast).setOnClickListener(this);
        findViewById(R.id.tv_show_notification).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_no_context).setOnClickListener(this);
        findViewById(R.id.tv_show_target_full).setOnClickListener(this);
        findViewById(R.id.tv_show_target_right).setOnClickListener(this);
        findViewById(R.id.tv_show_target_top).setOnClickListener(this);
        findViewById(R.id.tv_show_target_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_target_left).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_content).setOnClickListener(this);
        findViewById(R.id.tv_show_tran_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_in).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_zoom_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_reveal).setOnClickListener(this);
        CardView floatCardView = new CardView(this);
        ImageView floatIconView = new ImageView(this);
        floatIconView.setImageResource(R.mipmap.ic_launcher_foreground);
        floatIconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        floatIconView.setBackgroundResource(R.color.colorPrimary);
        floatCardView.addView(floatIconView);
        floatCardView.setCardBackgroundColor(Color.TRANSPARENT);
        floatCardView.setRadius(90);
        floatCardView.setLayoutParams(new ViewGroup.LayoutParams(180, 180));
        new OverlayLayer(this)
                .setOverlayView(floatCardView)
                .setSnapEdge(OverlayLayer.Edge.ALL)
                .setOutside(true)
                .setDefPercentX(1)
                .setDefPercentY(0.6F)
                .setDefAlpha(0F)
                .setDefScale(0F)
                .setNormalAlpha(0.9F)
                .setNormalScale(1)
                .setLowProfileDelay(3000)
                .setLowProfileAlpha(0.6F)
                .setLowProfileScale(0.9F)
                .setLowProfileIndent(0.5F)
                .setPaddingLeft(45)
                .setPaddingTop(45)
                .setPaddingRight(45)
                .setPaddingBottom(45)
                .setMarginLeft(0)
                .setMarginTop(0)
                .setMarginRight(0)
                .setMarginBottom(0)
                .addOnOverlayClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer layer, @NonNull View v) {
                        new CupertinoToastLayer(NormalActivity.this)
                                .setBackgroundColorInt(Color.parseColor("#f5f5f5"))
                                .setMessage("点击了悬浮按钮")
                                .setGravity(Gravity.CENTER)
                                .show();
                    }
                })
                .show();
        Layer dialog = Layers.dialog(this)
                .setContentView(R.layout.dialog_normal)
                .setBackgroundDimDefault()
                .setGravity(Gravity.CENTER)
                .setCancelableOnTouchOutside(true)
                .setCancelableOnClickKeyBack(true)
                .addOnClickToDismissListener(R.id.fl_dialog_no);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                    }
                });
            }
        }).start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_toast:
                boolean isSucc = mRandom.nextBoolean();
                new CupertinoToastLayer(this)
                        .setIcon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
                        .setMessage(isSucc ? "哈哈，成功了" : "哎呀，失败了")
                        .setTextColorInt(Color.WHITE)
                        .setBackgroundColorRes(isSucc ? R.color.colorPrimary : R.color.colorAccent)
                        .setGravity(Gravity.CENTER)
                        .show();
                break;
            case R.id.tv_show_notification:
                new CupertinoNotificationLayer(this)
                        .setContentBlurSimple(8)
                        .setContentBlurRadius(20)
                        .setContentBlurColorInt(Color.parseColor("#aaffffff"))
                        .setContentBlurCornerRadiusDp(10)
                        .setTitle("这是一个通知")
                        .setDesc(R.string.dialog_msg)
                        .setTimePattern("HH:mm")
                        .setIcon(R.drawable.ic_notificstion)
                        .setLabel(R.string.app_name)
                        .setOnNotificationClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View view) {
                                findViewById(R.id.tv_show_toast).performLongClick();
                                layer.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_full:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_fullscreen)
                        .addOnClickToDismissListener(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_app_context:
                Layers.dialog(new LayerActivity.OnLayerCreatedCallback() {
                    @Override
                    public void onLayerCreated(@NonNull DialogLayer anyLayer) {
                        anyLayer.setContentView(R.layout.dialog_normal)
                                .setBackgroundDimDefault()
                                .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                                .show();
                    }
                });
                break;
            case R.id.tv_show_no_context:
                Layers.dialog()
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_target_full:
                Layers.popup(findViewById(R.id.tv_show_target_full))
                        .setContentView(R.layout.dialog_fullscreen)
                        .setAnimStyle(DialogLayer.AnimStyle.TOP)
                        .show();
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = Layers.popup(findViewById(R.id.tv_show_target_right))
                            .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_RIGHT, Align.Vertical.CENTER, false)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_normal)
                            .setAnimStyle(DialogLayer.AnimStyle.LEFT);
                }
                if (anyLayer_show_target_right_shown) {
                    anyLayer_show_target_right_shown = false;
                    anyLayer_show_target_right.dismiss();
                } else {
                    anyLayer_show_target_right_shown = true;
                    anyLayer_show_target_right.show();
                }
                break;
            case R.id.tv_show_target_left:
                Layers.popup(findViewById(R.id.tv_show_target_left))
                        .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .setContentView(R.layout.popup_normal)
                        .setAnimStyle(DialogLayer.AnimStyle.RIGHT)
                        .show();
                break;
            case R.id.tv_show_target_top:
                Layers.popup(findViewById(R.id.tv_show_target_top))
                        .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .setContentView(R.layout.popup_match_width)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setAnimStyle(DialogLayer.AnimStyle.BOTTOM)
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                if (anyLayer_show_target_bottom == null) {
                    anyLayer_show_target_bottom = Layers.popup(findViewById(R.id.tv_show_target_bottom))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, false)
                            .setContentClip(false)
                            .setOutsideInterceptTouchEvent(false)
                            .setOutsideTouchToDismiss(true)
                            .setContentView(R.layout.popup_meun)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomInAnim(content, 0.5F, 0F);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomOutAnim(content, 0.5F, 0F);
                                }
                            });
                }
                if (anyLayer_show_target_bottom.isShown()) {
//                    anyLayer_show_target_bottom.dismiss();
                } else {
                    anyLayer_show_target_bottom.show();
                }
                break;
            case R.id.tv_show_blur_bg:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_icon)
                        .setBackgroundBlurRadius(8F)
                        .setBackgroundBlurSimple(8F)
                        .setBackgroundColorInt(Color.parseColor("#33ffffff"))
                        .show();
                break;
            case R.id.tv_show_blur_content:
                if (layer_show_blur_content == null) {
                    layer_show_blur_content = Layers.dialog(NormalActivity.this)
                            .setContentView(R.layout.dialog_content_blur)
                            .setBackgroundColorInt(Color.parseColor("#33000000"))
                            .setContentBlurRadius(8F)
                            .setContentBlurSimple(8F)
                            .setContentBlurCornerRadiusDp(10F)
                            .setContentBlurColorInt(Color.parseColor("#66ffffff"))
                            .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                            .addOnInitializeListener(new Layer.OnInitializeListener() {
                                @Override
                                public void onInitialize(@NonNull Layer layer) {
                                    TextView tv_dialog_content = layer.findView(R.id.tv_dialog_content);
                                    tv_dialog_content.setText("这是第一次初始化时绑定的随机数\n" + Math.random());
                                }
                            })
                            .addOnBindDataListener(new Layer.OnBindDataListener() {
                                @Override
                                public void onBindData(@NonNull Layer layer) {
                                    DialogLayer dialogLayer = (DialogLayer) layer;
                                    TextView tv_dialog_title = layer.findView(R.id.tv_dialog_title);
                                    tv_dialog_title.setText("主体View哈希地址" + System.identityHashCode(dialogLayer.getViewHolder().getContent()));
                                }
                            })
                            .addOnClickListener(new Layer.OnClickListener() {
                                @Override
                                public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                    anyLayer.dismiss();
                                }
                            }, R.id.fl_dialog_yes);
                }
                layer_show_blur_content.show();
                break;
            case R.id.tv_show_tran_bg:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_zoom_alpha_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ZOOM_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_reveal:
                Layers.dialog(NormalActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                                        ? new CircularRevealAnimatorCreator()
                                        : new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.ALPHA)
                        )
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
        }
    }
}

