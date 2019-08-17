package rs.etf.ba150210d.soccer.activities.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.activities.play.PlayActivity;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.activities.score.ScoreActivity;
import rs.etf.ba150210d.soccer.activities.settings.SettingsActivity;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class MainActivity extends FragmentOwner {

    public static final int PLAY_REQUEST = 200;

    private MainFragment mMainFragment;
    private NewGameFragment mNewGameFragment;
    private LoadGameFragment mLoadGameFragment;

    private MainViewModel mViewModel;

    private GameMetadata mNewGameMetadata = null;
    private GameMetadata mLoadGameMetadata = null;

    private boolean mIsLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainFragment = new MainFragment();
        mNewGameFragment = new NewGameFragment();
        mLoadGameFragment = new LoadGameFragment();

        initGameMetadata();

        setFragment(mMainFragment, false);
    }

    private void initGameMetadata() {
        SharedPreferences preferences = getPreferences();
        //GameMetadata.deleteSave(preferences);

        /* If saved game exists */
        if (preferences.getLong("save_playerPairId", -1L) != -1L) {
            mLoadGameMetadata = new GameMetadata(this, preferences);
            mLoadGameFragment.setGameMetadata(mLoadGameMetadata);
            mMainFragment.setLoadGameMetadata(mLoadGameMetadata);
        }
        mNewGameMetadata = new GameMetadata(this);
        mNewGameFragment.setGameMetadata(mNewGameMetadata);
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
                /* Already in this activity */
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
                if (mIsLoad) {
                    mLoadGameMetadata.pack(intent);
                } else {
                    mNewGameMetadata.pack(intent);
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
                initGameMetadata();
            }
            /* Game was played and finished, go to the 'scores' menu and display the specific score */
            if (resultCode == RESULT_OK) {
                cleanFragments();
                GameMetadata metadata = new GameMetadata(this, data);

                Intent intent = new Intent(this, ScoreActivity.class);
                metadata.pack(intent);
                intent.putExtra("isEndgame", true);

                startActivity(intent);
            }
        }
    }

    private void cleanFragments() {
        mLoadGameMetadata = null;
        mLoadGameFragment.setGameMetadata(null);
        mNewGameMetadata = new GameMetadata(this);
        mNewGameFragment.setGameMetadata(mNewGameMetadata);

        FragmentManager manager = getSupportFragmentManager();
        FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(0);
        if (entry != null) {
            goBack();
        }
    }

    public void reportLoad() {
        mIsLoad = true;
    }

    public void reportNew() {
        mIsLoad = false;
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
    public MainViewModel getViewModel() {
        return mViewModel;
    }
}
