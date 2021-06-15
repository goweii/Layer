package per.goweii.layer.design.material;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.dialog.DialogLayer;

public class MaterialDialogLayer extends DialogLayer {
    public MaterialDialogLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialDialogLayer(@NonNull Activity activity) {
        super(activity);
    }
}
