package rs.etf.ba150210d.soccer.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

import rs.etf.ba150210d.soccer.R;

public class SoundManager {
    private static SoundManager instance = null;

    private SoundPool mPool;

    private int mBounceId;
    private int mCrowdId;

    public static SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    private SoundManager(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createSoundPool();
        } else {
            createDeprecatedSoundPool();
        }

        mBounceId = mPool.load(context, R.raw.bounce, 1);
        mCrowdId = mPool.load(context, R.raw.crowd, 1);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        mPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(5)
                .build();
    }

    @SuppressWarnings("deprecation")
    private void createDeprecatedSoundPool(){
        mPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }

    public void playBounce() {
        mPool.play(mBounceId, 0.75f, 0.75f, 0, 0, 1);
    }

    public void playCrowd() {
        mPool.play(mCrowdId, 1, 1, 0, 0, 1);
    }

    public void release() {
        mPool.release();
    }
}
