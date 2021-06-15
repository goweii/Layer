package per.goweii.layer.design.material;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.notification.NotificationLayer;

public class MaterialNotificationLayer extends NotificationLayer {
    public MaterialNotificationLayer(@NonNull Context context) {
        super(context);
    }

    public MaterialNotificationLayer(@NonNull Activity activity) {
        super(activity);
    }
}
