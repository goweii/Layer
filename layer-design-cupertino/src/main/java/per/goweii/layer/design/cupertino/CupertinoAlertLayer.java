package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.dialog.DialogLayer;

public class CupertinoAlertLayer extends DialogLayer {
    public CupertinoAlertLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoAlertLayer(@NonNull Activity activity) {
        super(activity);
    }
}
