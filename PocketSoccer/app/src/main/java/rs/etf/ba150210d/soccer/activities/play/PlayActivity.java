package rs.etf.ba150210d.soccer.activities.play;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.PlayData;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.datastructures.Team;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class PlayActivity extends FragmentOwner implements ScreenRefreshController.ViewOwner {

    private PlayViewModel mViewModel;

    private PlayImageView mImageView;
    private ScreenRefreshController mScreenRefreshController;
    private PlayController mPlayController;

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
        mViewModel.setMetadata(new PlayMetadata(this, getIntent()));

        Team leftTeam = mViewModel.getMetadata().getLeftTeam();
        Team rightTeam = mViewModel.getMetadata().getRightTeam();
        mViewModel.setData(new PlayData(0, 0,
                BitmapFactory.decodeResource(getResources(), leftTeam.getId()),
                BitmapFactory.decodeResource(getResources(), rightTeam.getId()),
                BitmapFactory.decodeResource(getResources(), R.drawable.ball)));
        mViewModel.getData().setSpeed(mViewModel.getMetadata().getSpeed());

        mImageView = findViewById(R.id.play_imageView);
        mImageView.setData(mViewModel.getData());
        mImageView.setBackground(mViewModel.getMetadata().getField().getImage());

        int leftPlayerPoints = mViewModel.getMetadata().getLeftPlayerPoints();
        mLeftScoreView.setText(Integer.toString(leftPlayerPoints));
        int rightPlayerPoints = mViewModel.getMetadata().getRightPlayerPoints();
        mRightScoreView.setText(Integer.toString(rightPlayerPoints));

        mPlayController = new PlayController(mViewModel);
        mImageView.setOnTouchListener(mPlayController);

        mScreenRefreshController = new ScreenRefreshController(
                this, mViewModel);
        mImageView.invalidate();
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected void exitActivity() {
        mViewModel.getMetadata().save(getPreferences());
        mScreenRefreshController.stop();
        setResult(RESULT_CANCELED);
        super.exitActivity();
    }

    @Override
    public PlayViewModel getViewModel() {
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
            mRightScoreView.setText(Integer.toString(points));
        }

        PlayMetadata.deleteSave(getPreferences());
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
