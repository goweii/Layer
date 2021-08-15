package per.goweii.layer.visualeffectview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class RoundedLayout extends FrameLayout {
    private final Paint mSolidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float[] mCornerRadius = new float[8];
    private final RectF mClipRect = new RectF();
    private final Path mClipPath = new Path();

    private boolean mUseOutlineProvider = true;
    private int mSolidColor = Color.TRANSPARENT;

    private boolean mClipPathDirty = true;

    public RoundedLayout(Context context) {
        this(context, null);
    }

    public RoundedLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedLayout);
        mSolidColor = typedArray.getColor(R.styleable.RoundedLayout_solidColor, mSolidColor);
        float cornerRadius = typedArray.getDimension(R.styleable.RoundedLayout_cornerRadius, 0F);
        float cornerRadiusTopLeft = typedArray.getDimension(R.styleable.RoundedLayout_cornerRadiusTopLeft, cornerRadius);
        float cornerRadiusTopRight = typedArray.getDimension(R.styleable.RoundedLayout_cornerRadiusTopRight, cornerRadius);
        float cornerRadiusBottomRight = typedArray.getDimension(R.styleable.RoundedLayout_cornerRadiusBottomRight, cornerRadius);
        float cornerRadiusBottomLeft = typedArray.getDimension(R.styleable.RoundedLayout_cornerRadiusBottomLeft, cornerRadius);
        typedArray.recycle();
        setCornerRadius(cornerRadiusTopLeft, cornerRadiusTopRight, cornerRadiusBottomRight, cornerRadiusBottomLeft);
        setupOutlineProvider();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        mClipPathDirty = true;
        super.setPadding(left, top, right, bottom);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int radiusLeft = (int) Math.max(getTopLeftCornerRadius(), getBottomLeftCornerRadius());
        int radiusRight = (int) Math.max(getTopRightCornerRadius(), getBottomRightCornerRadius());
        return getPaddingLeft() + radiusLeft + radiusRight + getPaddingRight();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        int radiusTop = (int) Math.max(getTopLeftCornerRadius(), getTopRightCornerRadius());
        int radiusBottom = (int) Math.max(getBottomLeftCornerRadius(), getBottomRightCornerRadius());
        return getPaddingTop() + radiusTop + radiusBottom + getPaddingBottom();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mClipPathDirty = true;
        mClipRect.set(
                getPaddingLeft(),
                getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom()
        );
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Path clipPath = getClipPath();
        mSolidPaint.setStyle(Paint.Style.FILL);
        mSolidPaint.setColor(mSolidColor);
        canvas.drawPath(clipPath, mSolidPaint);
        if (canUseOutlineProvider()) {
            super.draw(canvas);
        } else {
            canvas.save();
            canvas.clipPath(clipPath);
            super.draw(canvas);
            canvas.restore();
        }
    }

    public void setUseOutlineProvider(boolean useOutlineProvider) {
        if (mUseOutlineProvider != useOutlineProvider) {
            mUseOutlineProvider = useOutlineProvider;
            setupOutlineProvider();
        }
    }

    public void setSolidColor(int solidColor) {
        if (mSolidColor != solidColor) {
            mSolidColor = solidColor;
            invalidate();
        }
    }

    public float getTopLeftCornerRadius() {
        return Math.max(getTopLeftCornerRadiusX(), getTopLeftCornerRadiusY());
    }

    public float getTopRightCornerRadius() {
        return Math.max(getTopRightCornerRadiusX(), getTopRightCornerRadiusY());
    }

    public float getBottomRightCornerRadius() {
        return Math.max(getBottomRightCornerRadiusX(), getBottomRightCornerRadiusY());
    }

    public float getBottomLeftCornerRadius() {
        return Math.max(getBottomLeftCornerRadiusX(), getBottomLeftCornerRadiusY());
    }

    public float getMaxCornerRadius() {
        return Math.max(
                Math.max(getTopLeftCornerRadius(), getTopRightCornerRadius()),
                Math.max(getBottomRightCornerRadius(), getBottomLeftCornerRadius())
        );
    }

    public boolean hasRoundedCorner() {
        return getMaxCornerRadius() > 0;
    }

    public boolean areCornersRadiusSame() {
        return mCornerRadius[0] == mCornerRadius[1] &&
                mCornerRadius[0] == mCornerRadius[2] &&
                mCornerRadius[0] == mCornerRadius[3] &&
                mCornerRadius[0] == mCornerRadius[4] &&
                mCornerRadius[0] == mCornerRadius[5] &&
                mCornerRadius[0] == mCornerRadius[6] &&
                mCornerRadius[0] == mCornerRadius[7];
    }

    public void setCornerRadius(float cornerRadius) {
        setCornerRadius(cornerRadius, cornerRadius, cornerRadius, cornerRadius);
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        topLeft = Math.max(topLeft, 0F);
        topRight = Math.max(topRight, 0F);
        bottomRight = Math.max(bottomRight, 0F);
        bottomLeft = Math.max(bottomLeft, 0F);
        boolean changed = false;
        if (getTopLeftCornerRadius() != topLeft) {
            changed = true;
            setTopLeftCornerRadiusX(topLeft);
            setTopLeftCornerRadiusY(topLeft);
        }
        if (getTopRightCornerRadius() != topRight) {
            changed = true;
            setTopRightCornerRadiusX(topRight);
            setTopRightCornerRadiusY(topRight);
        }
        if (getBottomRightCornerRadius() != bottomRight) {
            changed = true;
            setBottomRightCornerRadiusX(bottomRight);
            setBottomRightCornerRadiusY(bottomRight);
        }
        if (getBottomLeftCornerRadius() != bottomLeft) {
            changed = true;
            setBottomLeftCornerRadiusX(bottomLeft);
            setBottomLeftCornerRadiusY(bottomLeft);
        }
        if (changed) {
            mClipPathDirty = true;
            if (getSuggestedMinimumWidth() > getWidth() || getSuggestedMinimumHeight() > getHeight()) {
                requestLayout();
            } else {
                invalidate();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    invalidateOutline();
                }
            }
        }
    }

    @NonNull
    protected final Path getClipPath() {
        rebuildClipPath();
        return mClipPath;
    }

    private void rebuildClipPath() {
        if (!mClipPathDirty) return;
        mClipPathDirty = false;
        mClipPath.rewind();
        mClipPath.reset();
        mClipPath.addRoundRect(mClipRect, mCornerRadius, Path.Direction.CW);
    }

    private void setupOutlineProvider() {
        if (canUseOutlineProvider()) {
            if (!getClipToOutline()) {
                setClipToOutline(true);
            }
            if (!(getOutlineProvider() instanceof RoundedOutlineProvider)) {
                setOutlineProvider(new RoundedOutlineProvider());
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setClipToOutline(false);
                setOutlineProvider(null);
            }
        }
    }

    private boolean canUseOutlineProvider() {
        return mUseOutlineProvider && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && areCornersRadiusSame();
    }

    private float getTopLeftCornerRadiusX() {
        return mCornerRadius[0];
    }

    private float getTopLeftCornerRadiusY() {
        return mCornerRadius[1];
    }

    private float getTopRightCornerRadiusX() {
        return mCornerRadius[2];
    }

    private float getTopRightCornerRadiusY() {
        return mCornerRadius[3];
    }

    private float getBottomRightCornerRadiusX() {
        return mCornerRadius[4];
    }

    private float getBottomRightCornerRadiusY() {
        return mCornerRadius[5];
    }

    private float getBottomLeftCornerRadiusX() {
        return mCornerRadius[6];
    }

    private float getBottomLeftCornerRadiusY() {
        return mCornerRadius[7];
    }

    private void setTopLeftCornerRadiusX(float cornerRadius) {
        mCornerRadius[0] = cornerRadius;
    }

    private void setTopLeftCornerRadiusY(float cornerRadius) {
        mCornerRadius[1] = cornerRadius;
    }

    private void setTopRightCornerRadiusX(float cornerRadius) {
        mCornerRadius[2] = cornerRadius;
    }

    private void setTopRightCornerRadiusY(float cornerRadius) {
        mCornerRadius[3] = cornerRadius;
    }

    private void setBottomRightCornerRadiusX(float cornerRadius) {
        mCornerRadius[4] = cornerRadius;
    }

    private void setBottomRightCornerRadiusY(float cornerRadius) {
        mCornerRadius[5] = cornerRadius;
    }

    private void setBottomLeftCornerRadiusX(float cornerRadius) {
        mCornerRadius[6] = cornerRadius;
    }

    private void setBottomLeftCornerRadiusY(float cornerRadius) {
        mCornerRadius[7] = cornerRadius;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class RoundedOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            if (hasRoundedCorner() && areCornersRadiusSame()) {
                outline.setRoundRect(
                        getPaddingLeft(),
                        getPaddingTop(),
                        getWidth() - getPaddingRight(),
                        getHeight() - getPaddingBottom(),
                        getMaxCornerRadius()
                );
            } else {
                outline.setRect(
                        getPaddingLeft(),
                        getPaddingTop(),
                        getWidth() - getPaddingRight(),
                        getHeight() - getPaddingBottom()
                );
            }
        }
    }
}
