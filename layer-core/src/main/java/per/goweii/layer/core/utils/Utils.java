package per.goweii.layer.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Utils {

    @NonNull
    public static <T> T requireNonNull(@Nullable T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
        return obj;
    }

    @NonNull
    public static <T> T requireNonNull(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static float floatRange01(float value) {
        return floatRange(value, 0F, 1F);
    }

    public static float floatRange(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static int intRange(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getScreenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 从当前上下文获取Activity
     */
    @NonNull
    public static Activity requireActivity(@NonNull Context context) {
        Activity activity = getActivity(context);
        requireNonNull(activity, "无法从Context获取Activity，请确保传入的不是ApplicationContext或Service等");
        return activity;
    }

    /**
     * 从当前上下文获取Activity
     */
    @Nullable
    public static Activity getActivity(@NonNull Context context) {
        while (true) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            if (context instanceof ContextWrapper) {
                Context baseContext = ((ContextWrapper) context).getBaseContext();
                if (baseContext != context) {
                    context = baseContext;
                    continue;
                }
            }
            return null;
        }
    }

    public static void transparent(@NonNull Activity activity) {
        final Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void onViewPreDraw(@NonNull final View view, @NonNull final Runnable runnable) {
        if (view.getViewTreeObserver().isAlive()) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (view.getViewTreeObserver().isAlive()) {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    runnable.run();
                    return true;
                }
            });
        }
    }

    public static void onViewLayout(@NonNull final View view, @NonNull final Runnable runnable) {
        if (view.getViewTreeObserver().isAlive()) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (view.getViewTreeObserver().isAlive()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                    runnable.run();
                }
            });
        }
    }

    public static void getViewSize(@NonNull final View view, @NonNull Runnable runnable) {
        onViewLayout(view, runnable);
    }

    public static void getViewPadding(@NonNull View view, @NonNull Rect rect) {
        rect.setEmpty();
        rect.left = view.getPaddingLeft();
        rect.top = view.getPaddingTop();
        rect.right = view.getPaddingRight();
        rect.bottom = view.getPaddingBottom();
    }

    public static void getViewMargin(@NonNull View view, @NonNull Rect rect) {
        rect.setEmpty();
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        rect.left = params.leftMargin;
        rect.top = params.topMargin;
        rect.right = params.rightMargin;
        rect.bottom = params.bottomMargin;
    }

    public static int getViewMarginLeft(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.leftMargin;
    }

    public static int getViewMarginRight(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.rightMargin;
    }

    public static int getViewMarginTop(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.topMargin;
    }

    public static int getViewMarginBottom(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.bottomMargin;
    }

    public static void setViewPaddingLeft(@NonNull View view, int padding) {
        setViewPadding(view, padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingTop(@NonNull View view, int padding) {
        setViewPadding(view, view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingRight(@NonNull View view, int padding) {
        setViewPadding(view, view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    public static void setViewPaddingBottom(@NonNull View view, int padding) {
        setViewPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }

    public static void setViewPadding(@NonNull View view, int l, int t, int r, int b) {
        if (l != view.getPaddingLeft() || t != view.getPaddingTop() ||
                r != view.getPaddingRight() || b != view.getPaddingBottom()) {
            view.setPadding(l, t, r, b);
        }
    }

    public static void setViewPadding(@NonNull View view, @NonNull Rect padding) {
        setViewPadding(view, padding.left, padding.top, padding.right, padding.bottom);
    }

    public static void removeViewParent(@NonNull View view) {
        ViewParent parent = view.getParent();
        if (parent == null) return;
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    @SuppressWarnings("unchecked")
    public static <V extends View> V findViewByClass(@NonNull View view, @NonNull Class<V> cls) {
        if (cls.isInstance(view)) return (V) view;
        if (!(view instanceof ViewGroup)) return null;
        final ViewGroup viewGroup = (ViewGroup) view;
        final int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            V v = findViewByClass(viewGroup.getChildAt(i), cls);
            if (v != null) return v;
        }
        return null;
    }
}
