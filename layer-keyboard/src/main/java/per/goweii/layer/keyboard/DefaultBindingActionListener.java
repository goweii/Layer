package per.goweii.layer.keyboard;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class DefaultBindingActionListener extends BindingActionListener {
    @Override
    protected boolean onBind(@NonNull KeyboardLayer layer, @NonNull TextView view, @NonNull KeyAction action) {
        if (action.getCode() == KeyCodes.KEYCODE_ENTER) {
            return onEnter(layer, view, action);
        }
        if (action.getCode() == KeyCodes.KEYCODE_DEL) {
            return onDelete(layer, view, action);
        }
        if (action.hasInput()) {
            return onInput(layer, view, action);
        }
        return false;
    }

    protected boolean onEnter(@NonNull KeyboardLayer layer, @NonNull TextView view, @NonNull KeyAction action) {
        view.clearFocus();
        layer.dismiss();
        return true;
    }

    protected boolean onDelete(@NonNull KeyboardLayer layer, @NonNull TextView view, @NonNull KeyAction action) {
        CharSequence text = view.getText();
        if (text.length() <= 0) {
            return false;
        }
        if (text instanceof Editable) {
            Editable editable = (Editable) text;
            editable.delete(text.length() - 1, text.length());
        } else {
            view.setText(text.subSequence(0, text.length() - 1));
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    protected boolean onInput(@NonNull KeyboardLayer layer, @NonNull TextView view, @NonNull KeyAction action) {
        if (TextUtils.isEmpty(action.getText())) {
            return false;
        }
        view.setText(view.getText() + action.getText());
        return true;
    }
}