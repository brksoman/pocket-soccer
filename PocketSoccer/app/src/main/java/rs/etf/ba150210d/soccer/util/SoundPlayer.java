package rs.etf.ba150210d.soccer.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundPlayer {
    private MediaPlayer mPlayer;

    private AssetFileDescriptor mAsset;

    public SoundPlayer(Context context, String name) {
        AssetManager assets = context.getAssets();
        Resources resources = context.getResources();

        String resName = name + "_sound";
        int uriId = resources.getIdentifier(resName, "string", context.getPackageName());
        String uri = resources.getString(uriId);

        try {
            mAsset = assets.openFd(uri);
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mAsset.getFileDescriptor(), mAsset.getStartOffset(),
                    mAsset.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        try {
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
