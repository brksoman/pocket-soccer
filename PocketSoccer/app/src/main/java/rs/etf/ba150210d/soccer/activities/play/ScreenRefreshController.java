package rs.etf.ba150210d.soccer.activities.play;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.datastructures.PlayData;
import rs.etf.ba150210d.soccer.util.SoundManager;

public class ScreenRefreshController {

    public interface ViewOwner {
        void updateView();
        void clearMessage();
        void score(int playerSide);
        void win(int playerSide);
        void finishGame();

        void informBots();
    }

    private static final long CELEBRATION_DURATION = 2000;
    private static final Handler sThreadHandler = new Handler(Looper.getMainLooper());

    private PlayActivity mActivity;

    private SoundManager mSoundManager;

    private GameMetadata mMetadata;
    private PlayData mData;

    public ScreenRefreshController(final Activity activity, PlayViewModel viewModel) {
        mActivity = (PlayActivity) activity;
        mSoundManager = SoundManager.getInstance(mActivity);

        mData = viewModel.getData();
        mMetadata = viewModel.getMetadata();

        startRegularAnimation();
    }

    /** Freeze game and release SoundManager */
    public void stop() {
        mSoundManager.release();
        sThreadHandler.removeCallbacks(mRegularRefreshTask);
        sThreadHandler.removeCallbacks(mScoreRefreshTask);
        sThreadHandler.removeCallbacks(mContinueTask);
    }

    private void startRegularAnimation() {
        mActivity.clearMessage();
        sThreadHandler.removeCallbacks(mScoreRefreshTask);

        mActivity.informBots();
        sThreadHandler.post(mRegularRefreshTask);
    }

    private void startScoringAnimation() {
        sThreadHandler.removeCallbacks(mRegularRefreshTask);
        sThreadHandler.post(mScoreRefreshTask);
        sThreadHandler.postDelayed(mContinueTask, CELEBRATION_DURATION);
    }

    private void startWinningAnimation() {
        sThreadHandler.removeCallbacks(mRegularRefreshTask);
        sThreadHandler.post(mScoreRefreshTask);
        sThreadHandler.postDelayed(mExitTask, CELEBRATION_DURATION);
    }

    private Runnable mRegularRefreshTask = new Runnable() {
        @Override
        public void run() {
            sThreadHandler.postDelayed(this, 20);

            if (mData.updateData()) {
                mSoundManager.playBounce();
            }
            mMetadata.elapseTime();

            int scorer = mData.checkScoring();
            if (scorer != GameMetadata.NO_PLAYER) {
                mSoundManager.playCrowd();
                mMetadata.scorePlayer(scorer);
                mData.setNextPlayer(GameMetadata.otherSide(scorer));
                int winner = mMetadata.checkWin();
                if (winner != GameMetadata.NO_PLAYER) {
                    mActivity.win(scorer);
                    startWinningAnimation();
                } else {
                    mActivity.score(scorer);
                    startScoringAnimation();
                }
            }
            mActivity.updateView();
        }
    };

    private Runnable mScoreRefreshTask = new Runnable() {
        @Override
        public void run() {
            sThreadHandler.postDelayed(this, 20);

            mData.updateData();
            mActivity.updateView();
        }
    };

    private Runnable mContinueTask = new Runnable() {
        @Override
        public void run() {
            mData.resetState();
            startRegularAnimation();
        }
    };

    private Runnable mExitTask = new Runnable() {
        @Override
        public void run() {
            mActivity.finishGame();
        }
    };
}
