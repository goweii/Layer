package per.goweii.layer.core.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LayerContainer extends FrameLayout {
    private boolean mForceFocusInside = false;

    public LayerContainer(@NonNull Context context) {
        this(context, null);
    }

    public LayerContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayerContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
    }

    public void setForceFocusInside(boolean forceFocusInside) {
        mForceFocusInside = forceFocusInside;
        if (forceFocusInside) {
            if (!hasFocus()) {
                requestFocus();
            }
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (!mForceFocusInside) {
            return super.focusSearch(focused, direction);
        }
        FocusFinder focusFinder = FocusFinder.getInstance();
        View nextFocus = focusFinder.findNextFocus(this, focused, direction);
        if (nextFocus != null) {
            return nextFocus;
        }
        if (hasFocus()) {
            return focused;
        }
        if (!isFocusable()) {
            return focused;
        }
        return this;
    }
}
