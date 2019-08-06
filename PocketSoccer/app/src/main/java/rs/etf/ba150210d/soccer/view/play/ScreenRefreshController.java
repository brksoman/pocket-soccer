package rs.etf.ba150210d.soccer.view.play;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

import rs.etf.ba150210d.soccer.view_model.PlayViewModel;

public class ScreenRefreshController {

    public interface ViewOwner {
        void updateView();
        void clearMessage();
        void score(int playerSide);
        void win(int playerSide);
        void finishGame();
    }

    private static final long WIN_ANIMATION_DURATION = 2000;
    private static final Handler THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private PlayActivity mActivity;
    private PlayViewModel mViewModel;
    private PlayMetadata mMetadata;
    private PlayData mData;

    Timer mRegularTimer = new Timer();
    Timer mScoreTimer = new Timer();

    public ScreenRefreshController(final Activity activity, PlayViewModel viewModel) {
        mActivity = (PlayActivity) activity;
        mViewModel = viewModel;
        mData = mViewModel.getData();
        mMetadata = mViewModel.getMetadata();

        startRegularAnimation();
    }

    private void regularRefresh() {
        mData.updateData();
        mMetadata.elapseTime();

        int scorer = mData.checkScoring();
        if (scorer != PlayMetadata.NO_PLAYER) {
            if (scorer == PlayMetadata.LEFT_PLAYER) {
                mMetadata.scoreLeftPlayer();
            } else {
                mMetadata.scoreRightPlayer();
            }

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

    private void scoringRefresh() {
        mData.updateData();
        mActivity.updateView();
    }

    private void startRegularAnimation() {
        mActivity.clearMessage();
        mScoreTimer.cancel();
        mRegularTimer = new Timer();
        mRegularTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        regularRefresh();
                    }
                });
            }
        }, 20, 20);
    }

    private void startScoringAnimation() {
        mRegularTimer.cancel();
        mScoreTimer = new Timer();
        mScoreTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoringRefresh();
                    }
                });
            }
        }, 20, 20);

        Runnable endAnimation = new Runnable() {
            @Override
            public void run() {
                mData.resetState();
                startRegularAnimation();
            }
        };

        THREAD_HANDLER.postDelayed(endAnimation, WIN_ANIMATION_DURATION);
    }

    private void startWinningAnimation() {
        mRegularTimer.cancel();
        mScoreTimer = new Timer();
        mScoreTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scoringRefresh();
                    }
                });
            }
        }, 20, 20);

        Runnable endAnimation = new Runnable() {
            @Override
            public void run() {
                mActivity.finishGame();
            }
        };

        THREAD_HANDLER.postDelayed(endAnimation, WIN_ANIMATION_DURATION);
    }
}