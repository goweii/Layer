package per.goweii.layer.keyboard;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class KeyboardVibratorEffect {
    private final Context mContext;
    private final Vibrator mVibrator;

    public KeyboardVibratorEffect(Context context) {
        this.mContext = context;
        this.mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void release() {
        mVibrator.cancel();
    }

    public void performClick() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            VibrationEffect effect = VibrationEffect.createOneShot(1, 1);
            mVibrator.vibrate(effect);
        } else {
            mVibrator.vibrate(1);
        }
    }
}
