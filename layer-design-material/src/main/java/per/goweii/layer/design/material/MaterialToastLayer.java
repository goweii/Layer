package per.goweii.layer.design.material;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.toast.ToastLayer;

public class MaterialToastLayer extends ToastLayer {
    public MaterialToastLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialToastLayer(@NonNull Activity activity) {
        super(activity);
    }
}
