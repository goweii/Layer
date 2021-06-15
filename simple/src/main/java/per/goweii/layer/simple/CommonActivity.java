package per.goweii.layer.simple;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
        new KeyboardLayer(this).attachTextView(findViewById(R.id.tv_show_input));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}

