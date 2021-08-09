package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.dialog.DialogLayer;

public class CupertinoModalityLayer extends DialogLayer {
    public CupertinoModalityLayer(@NonNull Context context) {
        super(context);
        init();
    }

    public CupertinoModalityLayer(@NonNull Activity activity) {
        super(activity);
        init();
    }

    private void init() {
    }
}
