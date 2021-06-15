package per.goweii.layer.design.cupertino;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import per.goweii.layer.notification.NotificationLayer;

public class CupertinoNotificationLayer extends NotificationLayer {
    public CupertinoNotificationLayer(@NonNull Context context) {
        super(context);
    }

    public CupertinoNotificationLayer(@NonNull Activity activity) {
        super(activity);
    }
}
