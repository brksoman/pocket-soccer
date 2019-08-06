package rs.etf.ba150210d.soccer.view.play;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import rs.etf.ba150210d.soccer.view_model.PlayViewModel;

public class GestureController implements View.OnTouchListener {

    private static final float ACC_SCREEN_RATIO = 0.5f;
    private static final float[] MAX_ACCELERATIONS = new float[] {
            15, 18, 21, 24, 27, 30, 33, 36, 39, 42
    };

    private PlayViewModel mViewModel;
    private PlayMetadata mMetadata;
    private PlayData mData;
    private Puck mPuck;

    private PointF mAcc = new PointF();

    public GestureController(PlayViewModel viewModel) {
        mViewModel = viewModel;
        mMetadata = mViewModel.getMetadata();
        mData = mViewModel.getData();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (mMetadata.getNextPlayer() == PlayMetadata.LEFT_PLAYER) {
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
                                MAX_ACCELERATIONS[mMetadata.getSpeed()] * (mAcc.x / maxLength),
                                MAX_ACCELERATIONS[mMetadata.getSpeed()] * (mAcc.y / maxLength));
                    } else {
                        mAcc.set(
                                MAX_ACCELERATIONS[mMetadata.getSpeed()] * (mAcc.x / length),
                                MAX_ACCELERATIONS[mMetadata.getSpeed()] * (mAcc.y / length));
                    }
                    mPuck.accelerate(mAcc);
                    mPuck = null;
                    mMetadata.switchNextPlayer();
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                /* Illegal - Cancel move */
                mPuck = null;
                break;
        }

        return true;
    }
}
