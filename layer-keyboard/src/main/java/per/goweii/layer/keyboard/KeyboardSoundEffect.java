package per.goweii.layer.keyboard;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;

public class KeyboardSoundEffect {
    private static final int SOUND_CLICK = R.raw.layer_keyboard_click;

    private final Context mContext;
    private final SoundPool mSoundPool;

    private int mIdClick = 0;

    public KeyboardSoundEffect(@NonNull Context context) {
        mContext = context.getApplicationContext();
        mSoundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
        load();
    }

    public void release() {
        unload();
        mSoundPool.release();
    }

    public void performClick() {
        play(mIdClick);
    }

    private void load() {
        mIdClick = mSoundPool.load(mContext, SOUND_CLICK, 1);
    }

    private void unload() {
        if (mIdClick != 0) mSoundPool.unload(mIdClick);
    }

    private int play(int id) {
        if (id == 0) return 0;
        return mSoundPool.play(id, 0.5F, 0.5F, 0, 0, 1F);
    }
}
