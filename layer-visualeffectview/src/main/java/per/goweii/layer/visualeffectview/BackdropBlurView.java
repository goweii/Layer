package per.goweii.layer.visualeffectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;

import per.goweii.visualeffect.blur.BlurEffect;
import per.goweii.visualeffect.blur.RSBlurEffect;
import per.goweii.visualeffect.view.BackdropVisualEffectFrameLayout;

public class BackdropBlurView extends BackdropVisualEffectFrameLayout {
    private Path mClipPath = null;
    private RectF mClipRect = null;

    private float mCornerRadius = 0F;
    private float mBlurRadius = 8F;
    private float mBlurPercent = 0F;

    public BackdropBlurView(Context context) {
        this(context, null);
    }

    public BackdropBlurView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackdropBlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setShowDebugInfo(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    float minSide = Math.min(view.getWidth(), view.getHeight());
                    float radii = Math.min(mCornerRadius, minSide / 2F);
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radii);
                }
            });
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float simple = getSimpleSize();
        float radius;
        if (mBlurPercent > 0) {
            radius = Math.min(getWidth(), getHeight()) * mBlurPercent;
        } else {
            radius = mBlurRadius;
        }
        if (radius > 25) {
            simple = simple * (radius / 25);
            radius = 25;
        }
        if (radius < 0) {
            radius = 0;
        }
        if (getSimpleSize() != simple) {
            setSimpleSize(simple);
        }
        if (!(getVisualEffect() instanceof BlurEffect) || ((BlurEffect) getVisualEffect()).getRadius() != radius) {
            setVisualEffect(new RSBlurEffect(getContext(), radius));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            canvas.save();
            if (mCornerRadius > 0) {
                if (mClipRect == null) {
                    mClipRect = new RectF();
                }
                mClipRect.set(0F, 0F, getWidth(), getHeight());
                if (mClipPath == null) {
                    mClipPath = new Path();
                }
                mClipPath.rewind();
                mClipPath.reset();
                float minSide = Math.min(mClipRect.width(), mClipRect.height());
                float radii = Math.min(mCornerRadius, minSide / 2F);
                mClipPath.addRoundRect(mClipRect, radii, radii, Path.Direction.CW);
                canvas.clipPath(mClipPath);
            } else {
                mClipRect = null;
                mClipPath = null;
            }
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    public void setCornerRadius(float cornerRadius) {
        if (mCornerRadius != cornerRadius) {
            mCornerRadius = cornerRadius;
            invalidate();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                invalidateOutline();
            }
        }
    }

    public void setBlurPercent(float blurPercent) {
        if (mBlurPercent != blurPercent) {
            mBlurPercent = blurPercent;
            invalidate();
        }
    }

    public void setBlurRadius(float blurRadius) {
        if (mBlurRadius != blurRadius) {
            mBlurRadius = blurRadius;
            invalidate();
        }
    }
}
