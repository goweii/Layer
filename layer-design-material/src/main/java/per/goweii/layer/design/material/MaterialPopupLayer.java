package per.goweii.layer.design.material;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import per.goweii.layer.popup.PopupLayer;

public class MaterialPopupLayer extends PopupLayer {
    public MaterialPopupLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialPopupLayer(@NonNull Activity activity) {
        super(activity);
    }

    public MaterialPopupLayer(@NonNull View targetView) {
        super(targetView);
    }
}
