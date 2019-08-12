package rs.etf.ba150210d.soccer.activities.play;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import rs.etf.ba150210d.soccer.datastructures.PlayData;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
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

    private static final long WIN_ANIMATION_DURATION = 2000;
    private static final Handler THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private PlayActivity mActivity;
    private SoundManager mSoundManager;

    private PlayViewModel mViewModel;
    private PlayMetadata mMetadata;
    private PlayData mData;

    public ScreenRefreshController(final Activity activity, PlayViewModel viewModel) {
        mActivity = (PlayActivity) activity;
        mSoundManager = SoundManager.getInstance(mActivity);

        mViewModel = viewModel;
        mData = mViewModel.getData();
        mMetadata = mViewModel.getMetadata();

        startRegularAnimation();
    }

    public void stop() {
        mSoundManager.release();
        THREAD_HANDLER.removeCallbacks(mRegularRefreshTask);
        THREAD_HANDLER.removeCallbacks(mScoreRefreshTask);
        THREAD_HANDLER.removeCallbacks(mContinueTask);
    }

    private void startRegularAnimation() {
        mActivity.clearMessage();
        THREAD_HANDLER.removeCallbacks(mScoreRefreshTask);

        mActivity.informBots();
        THREAD_HANDLER.post(mRegularRefreshTask);
    }

    private void startScoringAnimation() {
        THREAD_HANDLER.removeCallbacks(mRegularRefreshTask);
        THREAD_HANDLER.post(mScoreRefreshTask);
        THREAD_HANDLER.postDelayed(mContinueTask, WIN_ANIMATION_DURATION);
    }

    private void startWinningAnimation() {
        THREAD_HANDLER.removeCallbacks(mRegularRefreshTask);
        THREAD_HANDLER.post(mScoreRefreshTask);
        THREAD_HANDLER.postDelayed(mExitTask, WIN_ANIMATION_DURATION);
    }

    private Runnable mRegularRefreshTask = new Runnable() {
        @Override
        public void run() {
            THREAD_HANDLER.postDelayed(this, 20);

            if (mData.updateData()) {
                mSoundManager.playBounce();
            }
            mMetadata.elapseTime();

            int scorer = mData.checkScoring();
            if (scorer != PlayMetadata.NO_PLAYER) {
                mSoundManager.playCrowd();
                mMetadata.scorePlayer(scorer);

                int winner = mMetadata.checkWin();
                if (winner != PlayMetadata.NO_PLAYER) {
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
            THREAD_HANDLER.postDelayed(this, 20);

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
