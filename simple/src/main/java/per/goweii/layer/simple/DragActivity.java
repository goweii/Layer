package per.goweii.layer.simple;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.layer.core.Layers;
import per.goweii.layer.core.Layer;
import per.goweii.layer.core.widget.SwipeLayout;
import per.goweii.layer.dialog.DialogLayer;

public class DragActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        initView();
    }

    private void initView() {
        findViewById(R.id.tv_show_left).setOnClickListener(this);
        findViewById(R.id.tv_show_right).setOnClickListener(this);
        findViewById(R.id.tv_show_top).setOnClickListener(this);
        findViewById(R.id.tv_show_bottom).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_left:
                new DialogLayer(DragActivity.this)
                        .setContentView(R.layout.dialog_drag_h)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.LEFT)
                        .setSwipeDismiss(SwipeLayout.Direction.LEFT)
                        .addOnClickToDismissListener(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_right:
                new DialogLayer(DragActivity.this)
                        .setContentView(R.layout.dialog_drag_h)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.RIGHT)
                        .setSwipeDismiss(SwipeLayout.Direction.RIGHT)
                        .addOnClickToDismissListener(R.id.dialog_drag_h_tv_close)
                        .show();
                break;
            case R.id.tv_show_top:
                new DialogLayer(DragActivity.this)
                        .setContentView(R.layout.dialog_list)
                        .setBackgroundDimDefault()
                        .setAvoidStatusBar(true)
                        .setGravity(Gravity.TOP)
                        .setSwipeDismiss(SwipeLayout.Direction.TOP)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .show();
                break;
            case R.id.tv_show_bottom:
                new DialogLayer(DragActivity.this)
                        .setContentView(R.layout.dialog_list)
                        .setBackgroundDimDefault()
                        .setGravity(Gravity.BOTTOM)
                        .setSwipeDismiss(SwipeLayout.Direction.BOTTOM)
                        .addOnClickToDismissListener(R.id.fl_dialog_no)
                        .addOnBindDataListener(new Layer.OnBindDataListener() {
                            @Override
                            public void onBindData(@NonNull Layer layer) {
                                layer.findView(R.id.tv_dialog_title).setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        findViewById(R.id.tv_show_top).performClick();
                                        return false;
                                    }
                                });
                            }
                        })
                        .show();
                break;
        }
    }
}

