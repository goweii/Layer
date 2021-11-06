package per.goweii.layer.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import per.goweii.layer.core.Layer;
import per.goweii.layer.core.utils.Utils;

public class DialogLayerActivity extends Activity implements Layer.OnVisibleChangedListener {

    @Nullable
    private static WeakReference<OnLayerCreatedCallback> sOnLayerCreatedCallback = null;

    static void start(@NonNull Context context, @NonNull OnLayerCreatedCallback callback) {
        sOnLayerCreatedCallback = new WeakReference<>(callback);
        Intent intent = new Intent(context, DialogLayerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        Utils.transparent(this);
        DialogLayer dialogLayer = new DialogLayer(this);
        dialogLayer.addOnVisibleChangeListener(this);
        if (sOnLayerCreatedCallback != null) {
            OnLayerCreatedCallback callback = sOnLayerCreatedCallback.get();
            if (callback != null) {
                callback.onLayerCreated(dialogLayer);
            }
            sOnLayerCreatedCallback.clear();
            sOnLayerCreatedCallback = null;
        }
    }

    @Override
    public void onShow(@NonNull Layer layer) {
    }

    @Override
    public void onDismiss(@NonNull Layer layer) {
        finish();
        overridePendingTransition(0, 0);
    }

    public interface OnLayerCreatedCallback {
        /**
         * 浮层已创建，可在这里进行浮层的初始化和数据绑定
         */
        void onLayerCreated(@NonNull DialogLayer dialogLayer);
    }

}
