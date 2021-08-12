package per.goweii.layer.design.cupertino.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private boolean mFitArrowInsetByChildren = false;

    private final ViewOutlineProvider mOutlineProvider;
    private final Path mOutlinePath = new Path();
    private final Path mArrowPath = new Path();
    private final RectF mRoundRectF = new RectF();
    private final float[] mRoundRadii = new float[8];
    private final Rect mArrowInset = new Rect();
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
        setClipToPadding(true);
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(mOutlinePath);
                    } else {
                        outline.setConvexPath(mOutlinePath);
                    }
                }
            };
            setOutlineProvider(mOutlineProvider);
            setClipToOutline(true);
        } else {
            mOutlineProvider = null;
        }
        fitArrowInsetBySelfPadding();
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
    protected int getSuggestedMinimumWidth() {
        switch (mArrowSide) {
            case ARROW_SIDE_LEFT:
            case ARROW_SIDE_RIGHT:
                return mCornerRadius * 2 + mArrowHeight;
            case ARROW_SIDE_TOP:
            case ARROW_SIDE_BOTTOM:
                return (int) (mCornerRadius * 2 + calcRealHalfArrowWidth() * 2);
            default:
                return mCornerRadius * 2;
        }
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        switch (mArrowSide) {
            case ARROW_SIDE_LEFT:
            case ARROW_SIDE_RIGHT:
                return (int) (mCornerRadius * 2 + calcRealHalfArrowWidth() * 2);
            case ARROW_SIDE_TOP:
            case ARROW_SIDE_BOTTOM:
                return mCornerRadius * 2 + mArrowHeight;
            default:
                return mCornerRadius * 2;
        }
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
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSolidColor);
        canvas.drawPath(mArrowPath, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSolidColor);
        canvas.drawRoundRect(mRoundRectF, mCornerRadius, mCornerRadius, mPaint);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (mFitArrowInsetByChildren) {
            fitArrowInsetByChildrenPadding();
        }
    }

    public void setFitArrowInsetByChildren(boolean fitArrowInsetByChildren) {
        if (mFitArrowInsetByChildren != fitArrowInsetByChildren) {
            mFitArrowInsetByChildren = fitArrowInsetByChildren;
            rebuildOutlinePath();
        }
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
        if (mFitArrowInsetByChildren) {
            fitArrowInsetByChildrenPadding();
        } else {
            fitArrowInsetBySelfPadding();
        }
        invalidate();
    }

    private void fitArrowInsetBySelfPadding() {
        Rect inset = calcArrowInset();
        Utils.setViewPadding(this, inset);
    }

    private void fitArrowInsetByChildrenPadding() {
        Utils.setViewPadding(this, 0, 0, 0, 0);
        Rect inset = calcArrowInset();
        int childCount = getChildCount();
        int l, t, r, b;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            l = Math.max(inset.left, child.getPaddingLeft());
            t = Math.max(inset.top, child.getPaddingTop());
            r = Math.max(inset.right, child.getPaddingRight());
            b = Math.max(inset.bottom, child.getPaddingBottom());
            Utils.setViewPadding(child, l, t, r, b);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @NonNull
    private Rect calcArrowInset() {
        mArrowInset.setEmpty();
        switch (mArrowSide) {
            case ARROW_SIDE_TOP:
                mArrowInset.top = mArrowHeight;
                break;
            case ARROW_SIDE_LEFT:
                mArrowInset.left = mArrowHeight;
                break;
            case ARROW_SIDE_RIGHT:
                mArrowInset.right = mArrowHeight;
                break;
            case ARROW_SIDE_BOTTOM:
                mArrowInset.bottom = mArrowHeight;
                break;
            default:
                break;
        }
        return mArrowInset;
    }

    private void buildOutlinePath() {
        mRoundRectF.set(
                mArrowInset.left,
                mArrowInset.top,
                getWidth() - mArrowInset.right,
                getHeight() - mArrowInset.bottom
        );
        Arrays.fill(mRoundRadii, mCornerRadius);
        mOutlinePath.rewind();
        mOutlinePath.reset();
        mArrowPath.rewind();
        mArrowPath.reset();
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
        mOutlinePath.addRoundRect(mRoundRectF, mRoundRadii, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getOutlineProvider() != mOutlineProvider) {
                setOutlineProvider(mOutlineProvider);
                setClipToOutline(true);
            } else {
                invalidateOutline();
            }
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void buildLeftArrow() {
        final float arrowRadius = mArrowRadius;
        final float arrowHeight = mArrowHeight;
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        final float realHalfArrowWidth = calcRealHalfArrowWidth();
        final double vertexDegrees = calcVertexDegrees();

        final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
        final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
        final float a2 = b1;
        final float b2 = arrowHeight * a2 / halfArrowWidth;

        mArrowPath.moveTo(
                arrowHeight,
                realArrowOffset + realHalfArrowWidth
        );
        mArrowPath.quadTo(
                arrowHeight,
                realArrowOffset + halfArrowWidth,
                arrowHeight - arrowRadius + a1,
                realArrowOffset + realHalfArrowWidth - b1
        );
        mArrowPath.lineTo(
                b2,
                realArrowOffset + a2
        );
        mArrowPath.quadTo(
                0,
                realArrowOffset,
                b2,
                realArrowOffset - a2
        );
        mArrowPath.lineTo(
                arrowHeight - arrowRadius + a1,
                realArrowOffset - realHalfArrowWidth + b1
        );
        mArrowPath.quadTo(
                arrowHeight,
                realArrowOffset - halfArrowWidth,
                arrowHeight,
                realArrowOffset - realHalfArrowWidth
        );
        mArrowPath.close();
    }

    private void buildTopArrow() {
        final float arrowRadius = mArrowRadius;
        final float arrowHeight = mArrowHeight;
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        final float realHalfArrowWidth = calcRealHalfArrowWidth();
        final double vertexDegrees = calcVertexDegrees();

        final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
        final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
        final float a2 = b1;
        final float b2 = arrowHeight * a2 / halfArrowWidth;

        mArrowPath.moveTo(
                realArrowOffset - realHalfArrowWidth,
                arrowHeight
        );
        mArrowPath.quadTo(
                realArrowOffset - halfArrowWidth,
                arrowHeight,
                realArrowOffset - realHalfArrowWidth + b1,
                arrowHeight - arrowRadius + a1
        );
        mArrowPath.lineTo(
                realArrowOffset - a2,
                b2
        );
        mArrowPath.quadTo(
                realArrowOffset,
                0,
                realArrowOffset + a2,
                b2
        );
        mArrowPath.lineTo(
                realArrowOffset + realHalfArrowWidth - b1,
                arrowHeight - arrowRadius + a1
        );
        mArrowPath.quadTo(
                realArrowOffset + halfArrowWidth,
                arrowHeight,
                realArrowOffset + realHalfArrowWidth,
                arrowHeight
        );
        mArrowPath.close();
    }

    private void buildRightArrow() {
        final float arrowRadius = mArrowRadius;
        final float arrowHeight = mArrowHeight;
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        final float realHalfArrowWidth = calcRealHalfArrowWidth();
        final double vertexDegrees = calcVertexDegrees();

        final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
        final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
        final float a2 = b1;
        final float b2 = arrowHeight * a2 / halfArrowWidth;

        mArrowPath.moveTo(
                getWidth() - arrowHeight,
                realArrowOffset + realHalfArrowWidth
        );
        mArrowPath.quadTo(
                getWidth() - arrowHeight,
                realArrowOffset + halfArrowWidth,
                getWidth() - arrowHeight + arrowRadius - a1,
                realArrowOffset + realHalfArrowWidth - b1
        );
        mArrowPath.lineTo(
                getWidth() - b2,
                realArrowOffset + a2
        );
        mArrowPath.quadTo(
                getWidth(),
                realArrowOffset,
                getWidth() - b2,
                realArrowOffset - a2
        );
        mArrowPath.lineTo(
                getWidth() - arrowHeight + arrowRadius - a1,
                realArrowOffset - realHalfArrowWidth + b1
        );
        mArrowPath.quadTo(
                getWidth() - arrowHeight,
                realArrowOffset - halfArrowWidth,
                getWidth() - arrowHeight,
                realArrowOffset - realHalfArrowWidth
        );
        mArrowPath.close();
    }

    private void buildBottomArrow() {
        final float arrowRadius = mArrowRadius;
        final float arrowHeight = mArrowHeight;
        final float halfArrowWidth = getHalfArrowWidth();
        final float realArrowOffset = getRealArrowOffset();
        final float realHalfArrowWidth = calcRealHalfArrowWidth();
        final double vertexDegrees = calcVertexDegrees();

        final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
        final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
        final float a2 = b1;
        final float b2 = arrowHeight * a2 / halfArrowWidth;

        mArrowPath.moveTo(
                realArrowOffset - realHalfArrowWidth,
                getHeight() - arrowHeight
        );
        mArrowPath.quadTo(
                realArrowOffset - halfArrowWidth,
                getHeight() - arrowHeight,
                realArrowOffset - realHalfArrowWidth + b1,
                getHeight() - arrowHeight + arrowRadius - a1
        );
        mArrowPath.lineTo(
                realArrowOffset - a2,
                getHeight() - b2
        );
        mArrowPath.quadTo(
                realArrowOffset,
                getHeight(),
                realArrowOffset + a2,
                getHeight() - b2
        );
        mArrowPath.lineTo(
                realArrowOffset + realHalfArrowWidth - b1,
                getHeight() - arrowHeight + arrowRadius - a1
        );
        mArrowPath.quadTo(
                realArrowOffset + halfArrowWidth,
                getHeight() - arrowHeight,
                realArrowOffset + realHalfArrowWidth,
                getHeight() - arrowHeight
        );
        mArrowPath.close();
    }

    private float calcRealHalfArrowWidth() {
        double vertexDegrees = calcVertexDegrees();
        double d = (90.0 - vertexDegrees) / 2.0;
        float increase = (float) (Math.tan(Math.toRadians(d)) * mArrowRadius);
        return getHalfArrowWidth() + increase;
    }

    private double calcVertexDegrees() {
        if (mArrowHeight <= 0) return 180.0;
        if (mArrowWidth <= 0) return 0.0;
        double tan = (mArrowWidth / 2.0) / mArrowHeight;
        double d = Math.atan(tan);
        return Math.toDegrees(d);
    }

    private float getHalfArrowWidth() {
        return mArrowWidth / 2F;
    }

    public float getRealArrowOffset() {
        final float minArrowOffset = mCornerRadius + calcRealHalfArrowWidth();
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
