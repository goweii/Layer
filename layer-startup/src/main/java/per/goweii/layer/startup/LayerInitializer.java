package per.goweii.layer.startup;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

import per.goweii.layer.Layers;

public class LayerInitializer implements Initializer<Layers> {
    @NonNull
    @Override
    public Layers create(@NonNull Context context) {
        return Layers.init((Application) context.getApplicationContext());
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
