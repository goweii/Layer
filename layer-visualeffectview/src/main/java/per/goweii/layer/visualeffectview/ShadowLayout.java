package per.goweii.layer.visualeffectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class ShadowLayout extends FrameLayout {
    private final Paint mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mShadowOutline = new Path();
    private final RectF mShadowInsets = new RectF();

    private boolean mShadowSymmetry = true;
    private int mShadowColor = Color.argb(100, 0, 0, 0);
    private float mShadowRadius = 0F;
    private float mShadowOffsetX = 0F;
    private float mShadowOffsetY = 0F;

    private boolean mClipToShadowOutline = true;

    private boolean mShadowOutlineInvalidate = false;
    private ShadowOutlineProvider mShadowOutlineProvider = null;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor, mShadowColor);
        mShadowSymmetry = typedArray.getBoolean(R.styleable.ShadowLayout_shadowSymmetry, mShadowSymmetry);
        mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, mShadowRadius);
        mShadowOffsetX = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetX, mShadowOffsetX);
        mShadowOffsetY = typedArray.getDimension(R.styleable.ShadowLayout_shadowOffsetY, mShadowOffsetY);
        typedArray.recycle();
        resetShadowInsets();
        resetShadowPaint();
    }

    public void setShadowOutlineProvider(@Nullable ShadowOutlineProvider shadowOutlineProvider) {
        if (shadowOutlineProvider == null) {
            if (mShadowOutlineProvider != null) {
                mShadowOutlineProvider.detachFromShadowLayout();
                mShadowOutlineProvider = null;
                invalidateShadowOutline();
            }
        } else {
            if (mShadowOutlineProvider != shadowOutlineProvider) {
                mShadowOutlineProvider = shadowOutlineProvider;
                mShadowOutlineProvider.attachToShadowLayout(this);
                invalidateShadowOutline();
            }
        }
    }

    @Nullable
    public ShadowOutlineProvider getShadowOutlineProvider() {
        return mShadowOutlineProvider;
    }

    public void setClipToShadowOutline(boolean clipToShadowOutline) {
        if (mClipToShadowOutline != clipToShadowOutline) {
            mClipToShadowOutline = clipToShadowOutline;
            resetShadowInsets();
        }
    }

    public boolean isClipToShadowOutline() {
        return mClipToShadowOutline;
    }

    public void setShadowColor(int shadowColor) {
        if (mShadowColor != shadowColor) {
            mShadowColor = shadowColor;
            resetShadowPaint();
        }
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public void setShadowRadius(float shadowRadius) {
        if (mShadowRadius != shadowRadius) {
            mShadowRadius = shadowRadius;
            resetShadowInsets();
            resetShadowPaint();
        }
    }

    public float getShadowRadius() {
        return mShadowRadius;
    }

    public void setShadowSymmetry(boolean shadowSymmetry) {
        if (mShadowSymmetry != shadowSymmetry) {
            mShadowSymmetry = shadowSymmetry;
            resetShadowInsets();
            resetShadowPaint();
        }
    }

    public boolean isShadowSymmetry() {
        return mShadowSymmetry;
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        if (mShadowOffsetX != shadowOffsetX) {
            mShadowOffsetX = shadowOffsetX;
            resetShadowInsets();
            resetShadowPaint();
        }
    }

    public float getShadowOffsetX() {
        return mShadowOffsetX;
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        if (mShadowOffsetY != shadowOffsetY) {
            mShadowOffsetY = shadowOffsetY;
            resetShadowInsets();
            resetShadowPaint();
        }
    }

    public float getShadowOffsetY() {
        return mShadowOffsetY;
    }

    public void invalidateShadowOutline() {
        mShadowOutlineInvalidate = true;
        invalidate();
    }

    public boolean isShadowOutlineInvalidate() {
        return mShadowOutlineInvalidate;
    }

    public RectF getShadowInsets() {
        return mShadowInsets;
    }

    public Path getShadowOutline() {
        return mShadowOutline;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int padding = getPaddingLeft() + getPaddingRight();
        int insides = (int) (mShadowInsets.left + mShadowInsets.right + 0.5F);
        return Math.max(padding, insides);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int padding = getPaddingTop() + getPaddingBottom();
        int insides = (int) (mShadowInsets.top + mShadowInsets.bottom + 0.5F);
        return Math.max(padding, insides);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        invalidateShadowOutline();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (mShadowOutlineInvalidate) {
            rebuildOutlinePath();
        }
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mShadowOutline, Region.Op.DIFFERENCE);
        canvas.drawPath(mShadowOutline, mShadowPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mClipToShadowOutline) {
            canvas.save();
            canvas.clipPath(mShadowOutline);
            super.dispatchDraw(canvas);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }

    private void rebuildOutlinePath() {
        mShadowOutline.reset();
        mShadowOutline.rewind();
        if (mShadowOutlineProvider != null) {
            mShadowOutlineProvider.buildShadowOutline(this, mShadowOutline, mShadowInsets);
        }
    }

    private void resetShadowInsets() {
        float l = Math.max(0F, mShadowRadius - mShadowOffsetX);
        float r = Math.max(0F, mShadowRadius + mShadowOffsetX);
        float t = Math.max(0F, mShadowRadius - mShadowOffsetY);
        float b = Math.max(0F, mShadowRadius + mShadowOffsetY);
        if (mShadowSymmetry) {
            float h = Math.max(l, r);
            float v = Math.max(t, b);
            mShadowInsets.set(h, v, h, v);
        } else {
            mShadowInsets.set(l, t, r, b);
        }
        if (mClipToShadowOutline) {
            super.setPadding(
                    (int) (mShadowInsets.left + 0.5F),
                    (int) (mShadowInsets.top + 0.5F),
                    (int) (mShadowInsets.right + 0.5F),
                    (int) (mShadowInsets.bottom + 0.5F)
            );
        }
        invalidateShadowOutline();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        if (!mClipToShadowOutline) {
            super.setPadding(left, top, right, bottom);
        }
    }

    private void resetShadowPaint() {
        mShadowPaint.setColor(mShadowColor);
        mShadowPaint.setShadowLayer(mShadowRadius, mShadowOffsetX, mShadowOffsetY, mShadowColor);
        mShadowPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }

    public static abstract class ShadowOutlineProvider {
        private WeakReference<ShadowLayout> mShadowLayoutRef = null;

        private void attachToShadowLayout(@NonNull ShadowLayout shadowLayout) {
            if (mShadowLayoutRef == null || mShadowLayoutRef.get() != shadowLayout) {
                mShadowLayoutRef = new WeakReference<>(shadowLayout);
            }
        }

        private void detachFromShadowLayout() {
            if (mShadowLayoutRef != null) {
                mShadowLayoutRef.clear();
                mShadowLayoutRef = null;
            }
        }

        public void invalidateShadowOutline() {
            if (mShadowLayoutRef != null && mShadowLayoutRef.get() != null) {
                mShadowLayoutRef.get().invalidateShadowOutline();
            }
        }

        public abstract void buildShadowOutline(
                @NonNull ShadowLayout shadowLayout,
                @NonNull Path shadowOutline,
                @NonNull RectF shadowInsets
        );
    }
}
