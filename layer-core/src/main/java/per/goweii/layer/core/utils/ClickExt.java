package per.goweii.layer.core.utils;

import android.view.View;
import androidx.annotation.NonNull;
import per.goweii.layer.core.R;

/**
 * 防止重复点击
 */
public class ClickExt{

    public static boolean clickEnable(@NonNull View view) {
        boolean clickable = false;
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - getTriggerLastTime(view) >= getTriggerDelay(view)) {
            clickable = true;
        }
        setTriggerLastTime(view, currentClickTime);
        return clickable;
    }

    public static void setTriggerDelay(@NonNull View view, long delay) {
        view.setTag(R.id.triggerDelayKey, delay);
    }

    private static long getTriggerDelay(@NonNull View view) {
        if (view.getTag(R.id.triggerDelayKey) != null) return (long) view.getTag(R.id.triggerDelayKey);
        else return -1;
    }

    private static void setTriggerLastTime(@NonNull View view, long delay) {
        view.setTag(R.id.triggerLastTimeKey, delay);
    }

    private static long getTriggerLastTime(@NonNull View view) {
        if (view.getTag(R.id.triggerLastTimeKey) != null) return (long) view.getTag(R.id.triggerLastTimeKey);
        else return 0;
    }

}
