package rs.etf.ba150210d.soccer.activities.score;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.util.FragmentOwner;
import rs.etf.ba150210d.soccer.util.ImmersiveAppCompatActivity;

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
            PlayMetadata metadata = new PlayMetadata(this, getIntent());
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
