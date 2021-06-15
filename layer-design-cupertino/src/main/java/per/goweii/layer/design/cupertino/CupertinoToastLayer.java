package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.toast.ToastLayer;

public class CupertinoToastLayer extends ToastLayer {
    public CupertinoToastLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoToastLayer(@NonNull Activity activity) {
        super(activity);
    }
}
