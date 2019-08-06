package rs.etf.ba150210d.soccer.view.score;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.view.play.PlayMetadata;
import rs.etf.ba150210d.soccer.view.util.ImmersiveAppCompatActivity;
import rs.etf.ba150210d.soccer.view_model.ScoreViewModel;

public class ScoreActivity extends ImmersiveAppCompatActivity {

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
    public void switchActivity(int activityId) {
        /* Cannot switch activity in score activity */
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
    public void goBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean hasBack = fragmentManager.popBackStackImmediate();

        if (!hasBack) {
            finish();
        }
    }

    @Override
    public ViewModel getViewModel() {
        return mViewModel;
    }

    private void setFragment(Fragment fragment, boolean isChildFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isChildFragment) {
            fragmentTransaction.replace(R.id.score_fragment, fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        } else {
            fragmentTransaction.add(R.id.score_fragment, fragment);
        }
        fragmentTransaction.commit();
    }
}
