package per.goweii.layer.simple;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.layer.Layers;
import per.goweii.layer.Layer;
import per.goweii.layer.LayerActivity;
import per.goweii.layer.design.cupertino.CupertinoToastLayer;
import per.goweii.layer.dialog.DialogLayer;
import per.goweii.layer.ext.CircularRevealAnimatorCreator;
import per.goweii.layer.ext.SimpleAnimatorCreator;
import per.goweii.layer.guide.GuideLayer;
import per.goweii.layer.popup.PopupLayer;
import per.goweii.layer.popup.PopupLayer.Align;
import per.goweii.layer.utils.AnimatorHelper;
import per.goweii.layer.widget.SwipeLayout;
import per.goweii.statusbarcompat.StatusBarCompat;

public class FullScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private PopupLayer anyLayer_show_target_right = null;
    private PopupLayer anyLayer_show_target_bottom = null;
    private PopupLayer anyLayer_show_target_full = null;
    private DialogLayer anyLayer_show_menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.transparent(this);
        setContentView(R.layout.activity_full_screen);
        initView();
        showMenuGuide();
    }

    private void showMenuGuide() {
        TextView textView1 = new TextView(FullScreenActivity.this);
        textView1.setText("带动画的菜单效果");
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(20F);
        TextView textView3 = new TextView(FullScreenActivity.this);
        textView3.setText("下一个");
        textView3.setPadding(90, 30, 90, 30);
        textView3.setBackgroundResource(R.drawable.shape_icon);
        textView3.setTextColor(Color.WHITE);
        textView3.setTextSize(16F);
        new GuideLayer(FullScreenActivity.this)
                .addMapping(new GuideLayer.Mapping()
                        .setTargetView(findViewById(R.id.tv_show_menu))
                        .setCornerRadius(9999F)
                        .setPaddingLeft(30)
                        .setPaddingRight(30)
                        .setPaddingTop(-10)
                        .setPaddingBottom(-10)
                        .setGuideView(textView1)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.ALIGN_RIGHT)
                        .setVerticalAlign(GuideLayer.Align.Vertical.BELOW)
                        .setMarginTop(30)
                        .setMarginRight(30))
                .addMapping(new GuideLayer.Mapping()
                        .setGuideView(textView3)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER_PARENT)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ALIGN_PARENT_BOTTOM)
                        .setMarginBottom(60)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                layer.dismiss();
                                showBlurGuide();
                            }
                        }))
                .show();
    }

    private void showBlurGuide() {
        TextView textView1 = new TextView(FullScreenActivity.this);
        textView1.setText("高斯模糊背景的弹窗\n实现起来也很方便");
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(20F);
        TextView textView3 = new TextView(FullScreenActivity.this);
        textView3.setText("我知道了");
        textView3.setPadding(90, 30, 90, 30);
        textView3.setBackgroundResource(R.drawable.shape_icon);
        textView3.setTextColor(Color.WHITE);
        textView3.setTextSize(16F);
        new GuideLayer(FullScreenActivity.this)
                .addMapping(new GuideLayer.Mapping()
                        .setTargetView(findViewById(R.id.tv_show_blur_bg))
                        .setCornerRadius(8F)
                        .setPadding(-30)
                        .setGuideView(textView1)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ABOVE)
                        .setMarginBottom(30))
                .addMapping(new GuideLayer.Mapping()
                        .setGuideView(textView3)
                        .setHorizontalAlign(GuideLayer.Align.Horizontal.CENTER)
                        .setVerticalAlign(GuideLayer.Align.Vertical.ALIGN_PARENT_BOTTOM)
                        .setMarginBottom(60)
                        .addOnClickListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                layer.dismiss();
                            }
                        }))
                .show();
    }

    private void initView() {
        findViewById(R.id.ll_action_bar).setPadding(0, StatusBarCompat.getHeight(this), 0, 0);
        findViewById(R.id.tv_show_app_context).setOnClickListener(this);
        findViewById(R.id.tv_show_multi).setOnClickListener(this);
        findViewById(R.id.tv_show_input).setOnClickListener(this);
        findViewById(R.id.tv_show_edit).setOnClickListener(this);
        findViewById(R.id.tv_show_full).setOnClickListener(this);
        findViewById(R.id.tv_show_target_full).setOnClickListener(this);
        findViewById(R.id.tv_show_target_right).setOnClickListener(this);
        findViewById(R.id.tv_show_target_top).setOnClickListener(this);
        findViewById(R.id.tv_show_target_bottom).setOnClickListener(this);
        findViewById(R.id.tv_show_target_left).setOnClickListener(this);
        findViewById(R.id.tv_show_blur_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_tran_bg).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_in).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_in).setOnClickListener(this);
        findViewById(R.id.tv_show_top_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_in).setOnClickListener(this);
        findViewById(R.id.tv_show_left_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_in).setOnClickListener(this);
        findViewById(R.id.tv_show_right_alpha_in).setOnClickListener(this);
        findViewById(R.id.tv_show_reveal).setOnClickListener(this);
        findViewById(R.id.tv_show_menu).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_title:
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
            case R.id.tv_show_multi:
                showMulti();
                break;
            case R.id.tv_show_input:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_input)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return ObjectAnimator.ofInt(content, "scrollY", -content.getHeight(), 0)
                                        .setDuration(200);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return ObjectAnimator.ofInt(content, "scrollY", 0, -content.getHeight())
                                        .setDuration(200);
                            }
                        })
                        .addSoftInputCompat(true)
                        .addOnSoftInputListener(new DialogLayer.OnSoftInputListener() {
                            @Override
                            public void onOpen(@NonNull DialogLayer layer, int height) {
                                new CupertinoToastLayer(FullScreenActivity.this)
                                        .setMessage("输入法打开->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }

                            @Override
                            public void onClose(@NonNull DialogLayer layer, int height) {
                                layer.dismiss();
                                new CupertinoToastLayer(FullScreenActivity.this)
                                        .setMessage("输入法关闭->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }

                            @Override
                            public void onHeightChange(@NonNull DialogLayer layer, int height) {
                                new CupertinoToastLayer(FullScreenActivity.this)
                                        .setMessage("输入法高度改变->当前高度" + height)
                                        .setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                                        .setMarginTop(300)
                                        .show();
                            }
                        })
                        .addOnShowListener(new Layer.OnShowListener() {
                            @Override
                            public void onPreShow(@NonNull Layer layer) {
                                EditText et = layer.requireView(R.id.et_input);
                                et.setFocusable(true);
                                et.setFocusableInTouchMode(true);
                                et.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et, InputMethodManager.SHOW_FORCED);
                            }

                            @Override
                            public void onPostShow(@NonNull Layer layer) {
                            }
                        })
                        .addOnDismissListener(new Layer.OnDismissListener() {
                            @Override
                            public void onPreDismiss(@NonNull Layer layer) {
                                EditText et = layer.requireView(R.id.et_input);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                            }

                            @Override
                            public void onPostDismiss(@NonNull Layer layer) {
                            }
                        })
                        .addOnClickToDismissListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View v) {
                                EditText et = layer.requireView(R.id.et_input);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.tv_send)
                        .show();
                break;
            case R.id.tv_show_edit:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_edit)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .addSoftInputCompat(false)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .addOnClickToDismissListener(new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                                EditText et = anyLayer.findView(R.id.et_dialog_content);
                                Toast.makeText(FullScreenActivity.this, et.getText().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }, R.id.fl_dialog_yes)
                        .show();
                break;
            case R.id.tv_show_full:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_fullscreen)
                        .addOnClickToDismissListener(R.id.iv_1)
                        .show();
                break;
            case R.id.tv_show_target_full:
                if (anyLayer_show_target_full == null) {
                    anyLayer_show_target_full = (PopupLayer) Layers.popup(findViewById(R.id.tv_show_target_full))
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.dialog_fullscreen)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopOutAnim(content);
                                }
                            });
                }
                if (anyLayer_show_target_full.isShown()) {
                    anyLayer_show_target_full.dismiss();
                } else {
                    anyLayer_show_target_full.show();
                }
                break;
            case R.id.tv_show_target_right:
                if (anyLayer_show_target_right == null) {
                    anyLayer_show_target_right = (PopupLayer) Layers.popup(findViewById(R.id.tv_show_target_right))
                            .setDirection(Align.Direction.HORIZONTAL)
                            .setHorizontal(Align.Horizontal.TO_RIGHT)
                            .setVertical(Align.Vertical.CENTER)
                            .setInside(true)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_normal)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createLeftOutAnim(content);
                                }
                            });
                    anyLayer_show_target_right.setUpdateLocationInterceptor(new PopupLayer.UpdateLocationInterceptor() {
                        @Override
                        public void interceptor(@NonNull float[] popupXY, int popupWidth, int popupHeight, int targetX, int targetY, int targetWidth, int targetHeight, int parentX, int parentY, int parentWidth, int parentHeight) {
                            popupXY[1] = Math.max(100, popupXY[1]);
                        }
                    });
                }
                if (anyLayer_show_target_right.isShown()) {
                    anyLayer_show_target_right.dismiss();
                } else {
                    anyLayer_show_target_right.show();
                }
                break;
            case R.id.tv_show_target_left:
                Layers.popup(findViewById(R.id.tv_show_target_left))
                        .setAlign(Align.Direction.HORIZONTAL, Align.Horizontal.TO_LEFT, Align.Vertical.CENTER, true)
                        .setContentView(R.layout.popup_normal)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_top:
                Layers.popup(findViewById(R.id.tv_show_target_top))
                        .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.ABOVE, true)
                        .setContentView(R.layout.popup_match_width)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                        .setContentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(@NonNull View content) {
                                return AnimatorHelper.createBottomOutAnim(content);
                            }
                        })
                        .show();
                break;
            case R.id.tv_show_target_bottom:
                if (anyLayer_show_target_bottom == null) {
                    anyLayer_show_target_bottom = (PopupLayer) Layers.popup(findViewById(R.id.tv_show_target_bottom))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.CENTER, Align.Vertical.BELOW, true)
                            .setOutsideInterceptTouchEvent(false)
                            .setBackgroundDimDefault()
                            .setContentView(R.layout.popup_match_width)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopInAnim(content);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createTopOutAnim(content);
                                }
                            });
                }
                if (anyLayer_show_target_bottom.isShown()) {
                    anyLayer_show_target_bottom.dismiss();
                } else {
                    anyLayer_show_target_bottom.show();
                }
                break;
            case R.id.tv_show_blur_bg:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_icon)
                        .setBackgroundBlurPercent(0.05f)
                        .setBackgroundColorInt(Color.parseColor("#33ffffff"))
                        .show();
                break;
            case R.id.tv_show_tran_bg:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_alpha_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom_zoom_alpha_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.BOTTOM_ZOOM_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_top_alpha_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.TOP_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_left_alpha_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.LEFT_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_right_alpha_in:
                Layers.dialog(FullScreenActivity.this)
                        .setContentView(R.layout.dialog_normal)
                        .setBackgroundDimDefault()
                        .setContentAnimator(new SimpleAnimatorCreator(SimpleAnimatorCreator.AnimStyle.RIGHT_ALPHA))
                        .addOnClickToDismissListener(R.id.fl_dialog_yes, R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_reveal:
                Layers.dialog(FullScreenActivity.this)
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
            case R.id.tv_show_menu:
                if (anyLayer_show_menu == null) {
                    anyLayer_show_menu = Layers.popup(findViewById(R.id.tv_show_menu))
                            .setAlign(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_RIGHT, Align.Vertical.BELOW, false)
                            .setOffsetYdp(15)
                            .setOutsideTouchToDismiss(true)
                            .setOutsideInterceptTouchEvent(false)
                            .setContentView(R.layout.popup_meun)
                            .setContentAnimator(new DialogLayer.AnimatorCreator() {
                                @Override
                                public Animator createInAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomInAnim(content, 1F, 0F);
                                }

                                @Override
                                public Animator createOutAnimator(@NonNull View content) {
                                    return AnimatorHelper.createDelayedZoomOutAnim(content, 1F, 0F);
                                }
                            });
                }
                if (anyLayer_show_menu.isShown()) {
                    anyLayer_show_menu.dismiss();
                } else {
                    anyLayer_show_menu.show();
                }
                break;
        }
    }

    private void showMulti() {
        Layers.dialog(FullScreenActivity.this)
                .setContentView(R.layout.dialog_more)
                .setBackgroundDimDefault()
                .setGravity(Gravity.CENTER)
                .setCancelableOnTouchOutside(false)
                .setCancelableOnClickKeyBack(false)
                .setContentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View content) {
                        return AnimatorHelper.createZoomAlphaOutAnim(content);
                    }
                })
                .addOnClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        anyLayer.dismiss();
                    }
                }, R.id.fl_dialog_no)
                .addOnClickListener(new Layer.OnClickListener() {
                    @Override
                    public void onClick(@NonNull Layer anyLayer, @NonNull View v) {
                        showMulti();
                    }
                }, R.id.fl_dialog_yes)
                .show();
    }
}

