package per.goweii.layer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import per.goweii.layer.dialog.DialogLayer;
import per.goweii.layer.guide.GuideLayer;
import per.goweii.layer.notification.NotificationLayer;
import per.goweii.layer.overlay.OverlayLayer;
import per.goweii.layer.popup.PopupLayer;
import per.goweii.layer.toast.ToastLayer;
import per.goweii.layer.utils.Utils;

public final class Layers {
    private static Layers sLayers = null;

    private final Application mApplication;
    private final ActivityHolder mActivityHolder;

    private Layers(Application application) {
        mApplication = application;
        mActivityHolder = new ActivityHolder(application);
    }

    public static Layers init(@NonNull Application application) {
        if (sLayers == null) {
            sLayers = new Layers(application);
        }
        return sLayers;
    }

    @NonNull
    public static Layers getInstance() {
        return Utils.requireNonNull(sLayers, "需要先在Application中初始化");
    }

    @NonNull
    public static ActivityHolder getActivityHolder() {
        return getInstance().mActivityHolder;
    }

    @NonNull
    public static Application getApplication() {
        return getInstance().mApplication;
    }

    public static void dialog(@NonNull LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(getApplication(), callback);
    }

    @NonNull
    public static DialogLayer dialog() {
        return new DialogLayer(getActivityHolder().requireCurrentActivity());
    }

    @NonNull
    public static DialogLayer dialog(@NonNull Class<Activity> clazz) {
        return new DialogLayer(getActivityHolder().requireActivity(clazz));
    }

    @NonNull
    public static DialogLayer dialog(@NonNull Context context) {
        return new DialogLayer(context);
    }

    @NonNull
    public static PopupLayer popup(@NonNull Context context) {
        return new PopupLayer(context);
    }

    @NonNull
    public static PopupLayer popup(@NonNull View targetView) {
        return new PopupLayer(targetView);
    }

    @NonNull
    public static ToastLayer toast() {
        return new ToastLayer(getActivityHolder().requireCurrentActivity());
    }

    @NonNull
    public static ToastLayer toast(@NonNull Context context) {
        return new ToastLayer(context);
    }

    @NonNull
    public static OverlayLayer overlay(@NonNull Context context) {
        return new OverlayLayer(context);
    }

    @NonNull
    public static GuideLayer guide(@NonNull Context context) {
        return new GuideLayer(context);
    }

    @NonNull
    public static NotificationLayer notification(@NonNull Context context) {
        return new NotificationLayer(context);
    }

}
