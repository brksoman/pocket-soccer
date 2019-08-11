package rs.etf.ba150210d.soccer.util;

import android.os.Handler;
import android.os.Looper;

import rs.etf.ba150210d.soccer.activities.play.PlayViewModel;
import rs.etf.ba150210d.soccer.datastructures.PlayData;

public class Bot {
    private static final long THINK_MIN = 2000;
    private static final long THINK_MAX = 4000;
    private static final Handler THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private PlayViewModel mViewModel;
    private int mSide;
    private PlayData mData;

    public Bot(PlayViewModel viewModel, int side) {
        mViewModel = viewModel;
        mSide = side;
        mData = mViewModel.getData();

    }

    public void play() {
        long thinkTime = (long)(THINK_MIN + (THINK_MAX - THINK_MIN) * Math.random());

        Runnable endAnimation = new Runnable() {
            @Override
            public void run() {
                // TODO implement bot's move
            }
        };

        THREAD_HANDLER.postDelayed(endAnimation, thinkTime);
    }
}