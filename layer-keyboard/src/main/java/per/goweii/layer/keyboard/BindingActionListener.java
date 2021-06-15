package per.goweii.layer.keyboard;

import android.widget.TextView;

import androidx.annotation.NonNull;

public abstract class BindingActionListener implements KeyboardLayer.OnActionListener {
    @Override
    public boolean onAction(@NonNull KeyboardLayer layer, @NonNull KeyAction action) {
        final TextView textView = layer.getViewHolder().getAttachedTextView();
        if (textView == null) return false;
        return onBind(layer, textView, action);
    }

    protected abstract boolean onBind(
            @NonNull KeyboardLayer layer,
            @NonNull TextView view,
            @NonNull KeyAction action
    );
}