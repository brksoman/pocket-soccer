package rs.etf.ba150210d.soccer.activities.score;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class ScoreActivity extends FragmentOwner {

    private AllScoreFragment mAllScoreFragment;
    private PairScoreFragment mPairScoreFragment;

    private ScoreViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_score);

        mAllScoreFragment = new AllScoreFragment();
        mPairScoreFragment = new PairScoreFragment();

        mViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);

        setFragment(mAllScoreFragment, false);

        if (getIntent().getBooleanExtra("isEndgame", false)) {
            GameMetadata metadata = new GameMetadata(this, getIntent());
            PlayerPair playerPair = metadata.getPlayerPair();

            mPairScoreFragment.setSelectedPlayerPair(playerPair);
            setFragment(mPairScoreFragment, true);
        }
    }

    @Override
    public void switchFragment(int fragmentId) {
        switch (fragmentId) {
            case ALL_SCORE_FRAGMENT:
                setFragment(mAllScoreFragment, false);
                break;

            case PAIR_SCORE_FRAGMENT:
                PlayerPair playerPair = mAllScoreFragment.getSelectedPlayerPair();
                mPairScoreFragment.setSelectedPlayerPair(playerPair);
                setFragment(mPairScoreFragment, true);
                break;
        }
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.score_fragment;
    }

    @Override
    public ScoreViewModel getViewModel() {
        return mViewModel;
    }
}
