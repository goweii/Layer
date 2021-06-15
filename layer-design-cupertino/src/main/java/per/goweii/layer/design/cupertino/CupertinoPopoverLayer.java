package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import per.goweii.layer.popup.PopupLayer;

public class CupertinoPopoverLayer extends PopupLayer {
    public CupertinoPopoverLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoPopoverLayer(@NonNull Activity activity) {
        super(activity);
    }

    public CupertinoPopoverLayer(@NonNull View targetView) {
        super(targetView);
    }
}
