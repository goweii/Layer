package per.goweii.layer.keyboard;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class KeyAction {
    @IdRes
    private int mCode;
    @Nullable
    private String mText;

    public KeyAction(@IdRes int code, @Nullable String text) {
        this.mCode = code;
        this.mText = text;
    }

    public boolean hasInput() {
        return KeyCodes.hasInput(mCode);
    }

    public boolean isLetter() {
        return KeyCodes.isLetter(mCode);
    }

    public boolean isNumber() {
        return KeyCodes.isNumber(mCode);
    }

    public boolean isSymbol() {
        return KeyCodes.isSymbol(mCode);
    }

    public void reset() {
        this.mCode = 0;
        this.mText = null;
    }

    public void recycle() {
        recycle(this);
    }

    @NonNull
    public KeyAction copy() {
        return obtain(mCode, mText);
    }

    @IdRes
    public int getCode() {
        return mCode;
    }

    @Nullable
    public String getText() {
        return mText;
    }

    private void setCode(@IdRes int code) {
        mCode = code;
    }

    private void setText(@Nullable String text) {
        mText = text;
    }

    private static final List<KeyAction> sPool = new LinkedList<>();

    public static KeyAction obtain(@IdRes int code, @Nullable String text) {
        if (sPool.isEmpty()) {
            return new KeyAction(code, text);
        }
        KeyAction action = sPool.remove(0);
        action.setCode(code);
        action.setText(text);
        return action;
    }

    public static void recycle(@NonNull KeyAction action) {
        action.reset();
        sPool.add(action);
    }
}