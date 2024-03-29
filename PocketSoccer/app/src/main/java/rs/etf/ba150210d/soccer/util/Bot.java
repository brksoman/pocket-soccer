package rs.etf.ba150210d.soccer.util;

import android.graphics.PointF;
import android.os.Handler;
import android.os.Looper;

import rs.etf.ba150210d.soccer.activities.play.PlayViewModel;
import rs.etf.ba150210d.soccer.datastructures.PlayData;
import rs.etf.ba150210d.soccer.datastructures.Puck;

public class Bot {
    private static final long THINK_MIN = 2000;
    private static final long THINK_MAX = 4000;
    private static final Handler sThreadHandler = new Handler(Looper.getMainLooper());

    private PlayViewModel mViewModel;
    private int mSide;
    private PlayData mData;
    private float mMaxAcc;

    public Bot(PlayViewModel viewModel, float maxAcc, int side) {
        mViewModel = viewModel;
        mMaxAcc = maxAcc;
        mSide = side;
        mData = mViewModel.getData();
    }

    /** Start 'thinking', then deploy strategy. */
    public void play() {
        long thinkTime = (long)(THINK_MIN + (THINK_MAX - THINK_MIN) * Math.random());

        sThreadHandler.postDelayed(mStrategy, thinkTime);
    }

    /** Stop 'thinking' */
    public void cancel() {
        sThreadHandler.removeCallbacks(mStrategy);
    }

    private Runnable mStrategy = new Runnable() {
        @Override
        public void run() {
            // TODO implement better strategy

            Puck puck = mData.getClosestPuck(mSide);
            PointF distance = puck.getDistance(mData.getBall());
            float absDistance = (float) Math.sqrt(
                    distance.x * distance.x + distance.y * distance.y);

            puck.accelerate(
                    mMaxAcc * (distance.x / absDistance),
                    mMaxAcc * (distance.y / absDistance));
            mViewModel.switchNextPlayer();
        }
    };
}
