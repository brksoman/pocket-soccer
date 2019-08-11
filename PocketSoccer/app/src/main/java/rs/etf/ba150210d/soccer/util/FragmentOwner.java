package rs.etf.ba150210d.soccer.util;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Spinner;

import rs.etf.ba150210d.soccer.R;

/*
    Interface through which fragments interact with each other. The activity which contains the
    fragments is supposed to implement this interface. The fragments then communicate with each
    other through the activity using this interface.
 */
public abstract class FragmentOwner extends ImmersiveAppCompatActivity {
    public static final int MAIN_FRAGMENT = 1;
    public static final int NEW_GAME_FRAGMENT = 2;
    public static final int LOAD_GAME_FRAGMENT = 3;

    public static final int ALL_SCORE_FRAGMENT = 4;
    public static final int PAIR_SCORE_FRAGMENT = 5;

    public static final int MAIN_ACTIVITY = 1;
    public static final int SCORE_ACTIVITY = 2;
    public static final int SETTINGS_ACTIVITY = 3;
    public static final int PLAY_ACTIVITY = 4;

    public void switchActivity(int activityId) {
        /* Some FragmentOwners cannot switch activities */
    }

    public void switchFragment(int fragmentId) {
        /* Some FragmentOwners have only one Fragment */
    }

    protected void setFragment(Fragment fragment, boolean isChildFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isChildFragment) {
            fragmentTransaction.replace(getFragmentContainerId(), fragment);
            fragmentTransaction.addToBackStack(fragment.getClass().getName());
        } else {
            fragmentTransaction.add(getFragmentContainerId(), fragment);
        }
        fragmentTransaction.commit();
    }

    protected abstract int getFragmentContainerId();

    public void goBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean hasBack = fragmentManager.popBackStackImmediate();

        if (!hasBack) {
            exitActivity();
        }
    }

    protected void exitActivity() {
        finish();
    }

    public abstract ViewModel getViewModel();

    @Override
    public void onBackPressed() {
        goBack();
        super.onBackPressed();
    }

    public SharedPreferences getPreferences() {
        return getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }
}
