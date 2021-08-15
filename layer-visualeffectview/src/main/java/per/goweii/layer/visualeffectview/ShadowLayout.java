package per.goweii.layer.visualeffectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

public class ShadowLayout extends RoundedLayout {
    private boolean mShadowSymmetry = true;
    private int mShadowColor = Color.argb(100, 0, 0, 0);
    private float mShadowRadius = 0F;
    private float mShadowOffsetX = 0F;
    private float mShadowOffsetY = 0F;

    private final Paint mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUseOutlineProvider(false);
        setWillNotDraw(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor, mShadowColor);
        mShadowSymmetry = typedArray.getBoolean(R.styleable.ShadowLayout_shadowSymmetry, mShadowSymmetry);
        mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, mShadowRadius);
        mShadowOffsetX = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetX, mShadowOffsetX);
        mShadowOffsetY = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetY, mShadowOffsetY);
        typedArray.recycle();
        resetPadding();
        resetShadow();
    }

    public void setShadowColor(int shadowColor) {
        if (mShadowColor != shadowColor) {
            mShadowColor = shadowColor;
            resetShadow();
        }
    }

    public void setShadowRadius(float shadowRadius) {
        if (mShadowRadius != shadowRadius) {
            mShadowRadius = shadowRadius;
            resetPadding();
            resetShadow();
        }
    }

    public void setShadowSymmetry(boolean shadowSymmetry) {
        if (mShadowSymmetry != shadowSymmetry) {
            mShadowSymmetry = shadowSymmetry;
            resetPadding();
            resetShadow();
        }
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        if (mShadowOffsetX != shadowOffsetX) {
            mShadowOffsetX = shadowOffsetX;
            resetPadding();
            resetShadow();
        }
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        if (mShadowOffsetY != shadowOffsetY) {
            mShadowOffsetY = shadowOffsetY;
            resetPadding();
            resetShadow();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Path clipPath = getClipPath();
        canvas.save();
        canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
        canvas.drawPath(clipPath, mShadowPaint);
        canvas.restore();
        super.draw(canvas);
    }

    private void resetPadding() {
        float l = Math.max(0F, mShadowRadius - mShadowOffsetX);
        float r = Math.max(0F, mShadowRadius + mShadowOffsetX);
        float t = Math.max(0F, mShadowRadius - mShadowOffsetY);
        float b = Math.max(0F, mShadowRadius + mShadowOffsetY);
        if (mShadowSymmetry) {
            float h = Math.max(l, r);
            float v = Math.max(t, b);
            setPadding(h, v, h, v);
        } else {
            setPadding(l, t, r, b);
        }
    }

    private void resetShadow() {
        mShadowPaint.setColor(mShadowColor);
        mShadowPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        mShadowPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }

    private void setPadding(float l, float t, float r, float b) {
        super.setPadding((int) (l + 0.5F), (int) (t + 0.5F), (int) (r + 0.5F), (int) (b + 0.5F));
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
    }
}
