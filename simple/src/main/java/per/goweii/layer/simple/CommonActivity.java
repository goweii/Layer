package per.goweii.layer.simple;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import per.goweii.layer.core.Layer;
import per.goweii.layer.design.cupertino.CupertinoAlertLayer;
import per.goweii.layer.keyboard.KeyboardLayer;
import per.goweii.statusbarcompat.StatusBarCompat;

public class CommonActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.transparent(this);
        setContentView(R.layout.activity_common);
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_action_bar).setPadding(0, StatusBarCompat.getHeight(this), 0, 0);
        findViewById(R.id.tv_show_tip).setOnClickListener(this);
        new KeyboardLayer(this).attachTextView(findViewById(R.id.tv_show_input));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_tip:
                new CupertinoAlertLayer(this)
                        .setTitle(R.string.app_name)
                        .setDesc(R.string.dialog_msg)
                        .addAction("确定", new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View view) {
                                layer.dismiss();
                            }
                        })
                        .addAction("取消", new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View view) {
                                layer.dismiss();
                            }
                        })
                        .addAction("忽略", new Layer.OnClickListener() {
                            @Override
                            public void onClick(@NonNull Layer layer, @NonNull View view) {
                                layer.dismiss();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
    }
}

