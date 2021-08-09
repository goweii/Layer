package per.goweii.layer.design.cupertino.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

import per.goweii.layer.core.utils.Utils;

public class PopoverContainer extends FrameLayout {
    public static final int ARROW_SIDE_NONE = 0;
    public static final int ARROW_SIDE_TOP = 1;
    public static final int ARROW_SIDE_LEFT = 2;
    public static final int ARROW_SIDE_RIGHT = 3;
    public static final int ARROW_SIDE_BOTTOM = 4;

    public static final int ARROW_CENTER = -1;

    @ArrowSide
    private int mArrowSide = ARROW_SIDE_NONE;
    private int mArrowOffset = ARROW_CENTER;
    private int mArrowRadius = 0;
    private int mArrowWidth = 0;
    private int mArrowHeight = 0;
    private int mCornerRadius = 0;
    private int mSolidColor = Color.TRANSPARENT;

    private CornerPathEffect mPathEffect = null;
    private final Path mOutlinePath = new Path();
    private final Path mArrowPath = new Path();
    private final RectF mRectF = new RectF();
    private final float[] mRadii = new float[8];
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private boolean mOutlineDirty = true;

    public PopoverContainer(@NonNull Context context) {
        this(context, null);
    }

    public PopoverContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopoverContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        super.setClipToPadding(true);
        super.setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setClipToOutline(true);
            super.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    //noinspection deprecation
                    outline.setConvexPath(mOutlinePath);
                }
            });
        }
        fitPaddingByArrowHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOutlineDirty = true;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mOutlineDirty) {
            buildOutlinePath();
        }
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.dispatchDraw(canvas);
        } else {
            canvas.save();
            canvas.clipPath(mOutlinePath);
            super.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSolidColor);
        mPaint.setPathEffect(mPathEffect);
        canvas.drawPath(mArrowPath, mPaint);
        mPaint.setColor(mSolidColor);
        mPaint.setPathEffect(null);
        canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaint);
    }

    @Override
    public void setOutlineProvider(ViewOutlineProvider provider) {
    }

    @Override
    public void setClipToOutline(boolean clipToOutline) {
    }

    @Override
    public void setWillNotDraw(boolean willNotDraw) {
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
    }

    public void setArrowSide(@ArrowSide int arrowSide) {
        if (mArrowSide != arrowSide) {
            mArrowSide = arrowSide;
            rebuildOutlinePath();
        }
    }

    public void setArrowOffset(int arrowOffset) {
        if (mArrowOffset != arrowOffset) {
            mArrowOffset = arrowOffset;
            rebuildOutlinePath();
        }
    }

    public void setArrowRadius(int arrowRadius) {
        if (mArrowRadius != arrowRadius) {
            mArrowRadius = arrowRadius;
            mPathEffect = new CornerPathEffect(mArrowRadius);
            rebuildOutlinePath();
        }
    }

    public void setCornerRadius(int cornerRadius) {
        if (mCornerRadius != cornerRadius) {
            mCornerRadius = cornerRadius;
            rebuildOutlinePath();
        }
    }

    public void setArrowWidth(int arrowWidth) {
        if (mArrowWidth != arrowWidth) {
            mArrowWidth = arrowWidth;
            rebuildOutlinePath();
        }
    }

    public void setArrowHeight(int arrowHeight) {
        if (mArrowHeight != arrowHeight) {
            mArrowHeight = arrowHeight;
            rebuildOutlinePath();
        }
    }

    public void setSolidColor(int solidColor) {
        if (mSolidColor != solidColor) {
            mSolidColor = solidColor;
            invalidate();
        }
    }

    private void rebuildOutlinePath() {
        mOutlineDirty = true;
        fitPaddingByArrowHeight();
        invalidate();
    }

    private void fitPaddingByArrowHeight() {
        int pl = 0, pt = 0, pr = 0, pb = 0;
        switch (mArrowSide) {
            case ARROW_SIDE_TOP:
                pt = mArrowHeight;
                break;
            case ARROW_SIDE_LEFT:
                pl = mArrowHeight;
                break;
            case ARROW_SIDE_RIGHT:
                pr = mArrowHeight;
                break;
            case ARROW_SIDE_BOTTOM:
                pb = mArrowHeight;
                break;
            default:
                break;
        }
        if (pl != getPaddingLeft() || pt != getPaddingTop() ||
                pr != getPaddingRight() || pb != getPaddingBottom()) {
            super.setPadding(pl, pt, pr, pb);
        }
    }

    private void buildOutlinePath() {
        mRectF.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        Arrays.fill(mRadii, mCornerRadius);
        mOutlinePath.reset();
        mOutlinePath.rewind();
        mArrowPath.reset();
        mArrowPath.rewind();
        switch (mArrowSide) {
            case ARROW_SIDE_LEFT:
                buildLeftArrow();
                break;
            case ARROW_SIDE_TOP:
                buildTopArrow();
                break;
            case ARROW_SIDE_RIGHT:
                buildRightArrow();
                break;
            case ARROW_SIDE_BOTTOM:
                buildBottomArrow();
                break;
            default:
                break;
        }
        mOutlinePath.addPath(mArrowPath);
        mOutlinePath.addRoundRect(mRectF, mRadii, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            invalidateOutline();
        }
    }

    private void buildLeftArrow() {
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        mArrowPath.moveTo(mRectF.left, realArrowOffset + halfArrowWidth);
        mArrowPath.lineTo(mRectF.left, realArrowOffset + halfArrowWidth - mArrowRadius);
        mArrowPath.lineTo(0, realArrowOffset);
        mArrowPath.lineTo(mRectF.left, realArrowOffset - halfArrowWidth + mArrowRadius);
        mArrowPath.lineTo(mRectF.left, realArrowOffset - halfArrowWidth);
        mArrowPath.close();
    }

    private void buildTopArrow() {
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        mArrowPath.moveTo(realArrowOffset - halfArrowWidth, mRectF.top);
        mArrowPath.lineTo(realArrowOffset - halfArrowWidth + mArrowRadius, mRectF.top);
        mArrowPath.lineTo(realArrowOffset, 0);
        mArrowPath.lineTo(realArrowOffset + halfArrowWidth - mArrowRadius, mRectF.top);
        mArrowPath.lineTo(realArrowOffset + halfArrowWidth, mRectF.top);
        mArrowPath.close();
    }

    private void buildRightArrow() {
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        mArrowPath.moveTo(mRectF.right, realArrowOffset + halfArrowWidth);
        mArrowPath.lineTo(mRectF.right, realArrowOffset + halfArrowWidth - mArrowRadius);
        mArrowPath.lineTo(getWidth(), realArrowOffset);
        mArrowPath.lineTo(mRectF.right, realArrowOffset - halfArrowWidth + mArrowRadius);
        mArrowPath.lineTo(mRectF.right, realArrowOffset - halfArrowWidth);
        mArrowPath.close();
    }

    private void buildBottomArrow() {
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        mArrowPath.moveTo(realArrowOffset - halfArrowWidth, mRectF.bottom);
        mArrowPath.lineTo(realArrowOffset - halfArrowWidth + mArrowRadius, mRectF.bottom);
        mArrowPath.lineTo(realArrowOffset, getHeight());
        mArrowPath.lineTo(realArrowOffset + halfArrowWidth - mArrowRadius, mRectF.bottom);
        mArrowPath.lineTo(realArrowOffset + halfArrowWidth, mRectF.bottom);
        mArrowPath.close();
    }

    private float getHalfArrowWidth() {
        return mArrowWidth / 2F;
    }

    public float getRealArrowOffset() {
        final float minArrowOffset = mCornerRadius + mArrowWidth / 2F + mArrowRadius;
        switch (mArrowSide) {
            case ARROW_SIDE_LEFT:
            case ARROW_SIDE_RIGHT:
                if (mArrowOffset == ARROW_CENTER) {
                    return getHeight() / 2F;
                }
                return Utils.floatRange(mArrowOffset, minArrowOffset, getHeight() - minArrowOffset);
            case ARROW_SIDE_TOP:
            case ARROW_SIDE_BOTTOM:
                if (mArrowOffset == ARROW_CENTER) {
                    return getWidth() / 2F;
                }
                return Utils.floatRange(mArrowOffset, minArrowOffset, getWidth() - minArrowOffset);
            default:
                return mArrowOffset;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ARROW_SIDE_TOP, ARROW_SIDE_LEFT, ARROW_SIDE_RIGHT, ARROW_SIDE_BOTTOM})
    public @interface ArrowSide {
    }
}
