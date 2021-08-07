package per.goweii.layer.visualeffectview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class BackdropIgnoreView extends FrameLayout {
    private WeakReference<BackdropBlurView> mBackdropBlurViewRef = null;

    public BackdropIgnoreView(@NonNull Context context) {
        super(context);
    }

    public BackdropIgnoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackdropIgnoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackdropBlurView(@Nullable BackdropBlurView backdropBlurView) {
        if (backdropBlurView == null) {
            if (mBackdropBlurViewRef != null) {
                mBackdropBlurViewRef.clear();
                mBackdropBlurViewRef = null;
            }
        } else {
            if (mBackdropBlurViewRef == null) {
                mBackdropBlurViewRef = new WeakReference<>(backdropBlurView);
            } else {
                if (mBackdropBlurViewRef.get() != backdropBlurView) {
                    mBackdropBlurViewRef.clear();
                    mBackdropBlurViewRef = new WeakReference<>(backdropBlurView);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mBackdropBlurViewRef != null
                && mBackdropBlurViewRef.get() != null
                && mBackdropBlurViewRef.get().isRendering()) {
            return;
        }
        super.draw(canvas);
    }
}
