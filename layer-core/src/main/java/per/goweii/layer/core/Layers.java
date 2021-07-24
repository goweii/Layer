package per.goweii.layer.core;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import per.goweii.layer.core.utils.Utils;

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

    @NonNull
    public static Activity requireCurrentActivity() {
        return getActivityHolder().requireCurrentActivity();
    }
}
