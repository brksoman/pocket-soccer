package rs.etf.ba150210d.soccer.view.play;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.widget.TextView;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.view.util.ImmersiveAppCompatActivity;
import rs.etf.ba150210d.soccer.view_model.PlayViewModel;

public class PlayActivity extends ImmersiveAppCompatActivity implements ScreenRefreshController.ViewOwner {

    private PlayViewModel mViewModel;

    private PlayImageView mImageView;
    private ScreenRefreshController mScreenRefreshController;
    private GestureController mGestureController;

    private TextView mLeftScoreView;
    private TextView mRightScoreView;
    private TextView mTimeView;
    private TextView mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mLeftScoreView = findViewById(R.id.play_leftScore);
        mRightScoreView = findViewById(R.id.play_rightScore);
        mTimeView = findViewById(R.id.play_time);
        mMessageView = findViewById(R.id.play_message);

        mViewModel = ViewModelProviders.of(this).get(PlayViewModel.class);

        mImageView = findViewById(R.id.play_imageView);

        mViewModel.setMetadata(new PlayMetadata(this, getIntent()));
        mViewModel.setData(mImageView.getData());
        mViewModel.getData().setSpeed(mViewModel.getMetadata().getSpeed());

        mImageView.setBackground(mViewModel.getMetadata().getField().getImage());

        int leftPlayerPoints = mViewModel.getMetadata().getLeftPlayerPoints();
        mLeftScoreView.setText(Integer.toString(leftPlayerPoints));
        int rightPlayerPoints = mViewModel.getMetadata().getRightPlayerPoints();
        mRightScoreView.setText(Integer.toString(rightPlayerPoints));

        mGestureController = new GestureController(mViewModel);
        mImageView.setOnTouchListener(mGestureController);

        mScreenRefreshController = new ScreenRefreshController(
                this, mViewModel);
        mImageView.invalidate();
    }

    @Override
    public void switchActivity(int activityId) {

    }

    @Override
    public void switchFragment(int fragmentId) {

    }

    @Override
    public void goBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean hasBack = fragmentManager.popBackStackImmediate();

        if (!hasBack) {
            SharedPreferences preferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            mViewModel.getMetadata().save(preferences);
            // TODO implement game exit
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public ViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void updateView() {
        int time = mViewModel.getMetadata().getElapsedTime();
        mTimeView.setText(Long.toString(time / 60) + " : " + Long.toString(time % 60));
        mImageView.invalidate();
    }

    @Override
    public void score(int playerSide) {
        if (playerSide == PlayMetadata.LEFT_PLAYER) {
            String name = mViewModel.getMetadata().getLeftPlayerName();
            int points = mViewModel.getMetadata().getLeftPlayerPoints();
            mMessageView.setText(getString(R.string.score_format, name));
            mLeftScoreView.setText(Integer.toString(points));
        } else {
            String name = mViewModel.getMetadata().getRightPlayerName();
            int points = mViewModel.getMetadata().getRightPlayerPoints();
            mMessageView.setText(getString(R.string.score_format, name));
            mRightScoreView.setText(Integer.toString(points));
        }
    }

    @Override
    public void win(int playerSide) {
        mViewModel.insertScore();
        if (playerSide == PlayMetadata.LEFT_PLAYER) {
            String name = mViewModel.getMetadata().getLeftPlayerName();
            int points = mViewModel.getMetadata().getLeftPlayerPoints();
            mMessageView.setText(getString(R.string.win_format, name));
            mLeftScoreView.setText(Integer.toString(points));
        } else {
            String name = mViewModel.getMetadata().getRightPlayerName();
            int points = mViewModel.getMetadata().getRightPlayerPoints();
            mMessageView.setText(getString(R.string.win_format, name));
            mLeftScoreView.setText(Integer.toString(points));
        }

        SharedPreferences preferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        PlayMetadata.deleteSave(preferences);
    }

    @Override
    public void finishGame() {
        Intent data = new Intent();
        mViewModel.getMetadata().pack(data);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void clearMessage() {
        mMessageView.setText("");
    }
}
