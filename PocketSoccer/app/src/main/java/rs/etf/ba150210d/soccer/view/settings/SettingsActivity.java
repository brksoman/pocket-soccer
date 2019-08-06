package rs.etf.ba150210d.soccer.view.settings;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.view.util.ImmersiveAppCompatActivity;

public class SettingsActivity extends ImmersiveAppCompatActivity {

    private MainSettingsFragment mMainSettingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        mMainSettingsFragment = new MainSettingsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.settings_fragment, mMainSettingsFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void switchActivity(int activityId) {
        // No switching activity
    }

    @Override
    public void switchFragment(int fragmentId) {
        // No more fragments in this activity
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
        // No view model for this activity
        return null;
    }
}
