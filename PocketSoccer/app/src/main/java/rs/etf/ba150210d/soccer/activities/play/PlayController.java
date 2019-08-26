package rs.etf.ba150210d.soccer.activities.play;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import rs.etf.ba150210d.soccer.datastructures.PlayData;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.datastructures.Puck;
import rs.etf.ba150210d.soccer.util.Bot;

public class PlayController implements View.OnTouchListener {

    private static final float ACC_SCREEN_RATIO = 0.5f;
    private static final float[] MAX_ACCELERATIONS = new float[] {
            16, 20, 24, 28, 32, 36, 40, 44, 48, 52
    };

    private PlayViewModel mViewModel;
    private GameMetadata mMetadata;
    private PlayData mData;

    private Puck mPuck;

    private PointF mAcc = new PointF();
    private float mMaxAcc;

    private Bot mLeftBot = null;
    private Bot mRightBot = null;

    public PlayController(PlayViewModel viewModel) {
        mViewModel = viewModel;
        mMetadata = mViewModel.getMetadata();
        mData = mViewModel.getData();
        mMaxAcc = MAX_ACCELERATIONS[mMetadata.getSpeed()];

        if (mMetadata.isLeftBot()) {
            mLeftBot = new Bot(mViewModel, mMaxAcc, GameMetadata.LEFT_PLAYER);
        }
        if (mMetadata.isRightBot()) {
            mRightBot = new Bot(mViewModel, mMaxAcc, GameMetadata.RIGHT_PLAYER);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!mMetadata.isCurrentBot()) {
            /* Ignore gestures if bot is playing */
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mMetadata.getNextPlayer() == GameMetadata.LEFT_PLAYER) {
                        mPuck = mData.getLeftTeamPressedPuck(event.getX(), event.getY());
                    } else {
                        mPuck = mData.getRightTeamPressedPuck(event.getX(), event.getY());
                    }

                    if (mPuck != null) {
                        mAcc.set(-event.getX(), -event.getY());
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (mPuck != null) {
                        mAcc.offset(event.getX(), event.getY());
                        float length = (float) Math.sqrt(mAcc.x * mAcc.x + mAcc.y * mAcc.y);
                        float maxLength = mData.getViewDims().x * ACC_SCREEN_RATIO;

                        if (length <= maxLength) {
                            mAcc.set(
                                    mMaxAcc * (mAcc.x / maxLength),
                                    mMaxAcc * (mAcc.y / maxLength));
                        } else {
                            mAcc.set(
                                    mMaxAcc * (mAcc.x / length),
                                    mMaxAcc * (mAcc.y / length));
                        }
                        mPuck.accelerate(mAcc);
                        mPuck = null;
                        mViewModel.switchNextPlayer();

                        informBots();
                    }
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    /* Illegal - Cancel move */
                    mPuck = null;
                    break;
            }

        }
        return true;
    }

    /** Tell bots to check if it is their turn, and to play if it is. */
    public void informBots() {
        if (mMetadata.isCurrentBot()) {
            if (mMetadata.getNextPlayer() == GameMetadata.LEFT_PLAYER) {
                mLeftBot.play();
            } else {
                mRightBot.play();
            }
        }
    }

    /** Stop the turn of any of the bots. */
    public void stopBots() {
        if (mLeftBot != null) {
            mLeftBot.cancel();
        }
        if (mRightBot != null) {
            mRightBot.cancel();
        }
    }
}
