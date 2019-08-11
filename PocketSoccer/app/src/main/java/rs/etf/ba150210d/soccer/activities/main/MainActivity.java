package rs.etf.ba150210d.soccer.activities.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.activities.play.PlayActivity;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.activities.score.ScoreActivity;
import rs.etf.ba150210d.soccer.activities.settings.SettingsActivity;
import rs.etf.ba150210d.soccer.util.FragmentOwner;
import rs.etf.ba150210d.soccer.util.ImmersiveAppCompatActivity;

public class MainActivity extends FragmentOwner {

    public static final int PLAY_REQUEST = 200;

    private MainFragment mMainFragment;
    private NewGameFragment mNewGameFragment;
    private LoadGameFragment mLoadGameFragment;

    private MainViewModel mViewModel;

    private PlayMetadata mSelectedPlayMetadata = null;
    private PlayMetadata mLoadedPlayMetadata = null;

    private boolean mIsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainFragment = new MainFragment();
        mNewGameFragment = new NewGameFragment();
        mLoadGameFragment = new LoadGameFragment();

        initMetadata();
        setFragment(mMainFragment, false);
    }

    private void initMetadata() {
        SharedPreferences preferences = getPreferences();
        //PlayMetadata.deleteSave(preferences);

        /* If saved game exists */
        if (preferences.getLong("save_playerPairId", -1L) != -1L) {
            mLoadedPlayMetadata = new PlayMetadata(this, preferences);
            mLoadGameFragment.setPlayMetadata(mLoadedPlayMetadata);
            mMainFragment.setLoadedPlayMetadata(mLoadedPlayMetadata);
        }
        mSelectedPlayMetadata = new PlayMetadata(this);
        mNewGameFragment.setPlayMetadata(mSelectedPlayMetadata);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.main_fragment;
    }

    @Override
    public void switchActivity(int activityId) {
        Intent intent;

        switch (activityId) {
            case MAIN_ACTIVITY:
                // Already in this activity
                break;

            case SCORE_ACTIVITY:
                intent = new Intent(this, ScoreActivity.class);
                startActivity(intent);
                break;

            case SETTINGS_ACTIVITY:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case PLAY_ACTIVITY:
                intent = new Intent(this, PlayActivity.class);
                if (mIsLoaded) {
                    mLoadedPlayMetadata.pack(intent);
                } else {
                    mSelectedPlayMetadata.pack(intent);
                }
                startActivityForResult(intent, PLAY_REQUEST);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLAY_REQUEST) {
            /* Game was played but not finished, display new saved game in the 'load game' menu */
            if (resultCode == RESULT_CANCELED) {
                initMetadata();
            }
            /* Game was played and finished, go to the 'scores' menu and display the specific score */
            if (resultCode == RESULT_OK) {
                cleanFragments();
                PlayMetadata metadata = new PlayMetadata(this, data);

                Intent intent = new Intent(this, ScoreActivity.class);
                metadata.pack(intent);
                intent.putExtra("isEndgame", true);

                startActivity(intent);
            }
        }
    }

    private void cleanFragments() {
        mLoadedPlayMetadata = null;
        mLoadGameFragment.setPlayMetadata(null);
        mSelectedPlayMetadata = new PlayMetadata(this);
        mNewGameFragment.setPlayMetadata(mSelectedPlayMetadata);

        FragmentManager manager = getSupportFragmentManager();
        FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(0);
        if (entry != null) {
            goBack();
        }
    }

    public void reportLoad() {
        mIsLoaded = true;
    }

    public void reportNew() {
        mIsLoaded = false;
    }

    @Override
    public void switchFragment(int fragmentId) {
        switch (fragmentId) {
            case MAIN_FRAGMENT:
                setFragment(mMainFragment, false);
                break;

            case NEW_GAME_FRAGMENT:
                setFragment(mNewGameFragment, true);
                break;

            case LOAD_GAME_FRAGMENT:
                setFragment(mLoadGameFragment, true);
                break;
        }
    }

    @Override
    protected void exitActivity() {
        super.exitActivity();
        // TODO implement app exit
    }

    @Override
    public MainViewModel getViewModel() {
        return mViewModel;
    }
}
