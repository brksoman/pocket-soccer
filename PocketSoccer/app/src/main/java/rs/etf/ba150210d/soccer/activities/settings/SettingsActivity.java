package rs.etf.ba150210d.soccer.activities.settings;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.util.FragmentOwner;
import rs.etf.ba150210d.soccer.util.ImmersiveAppCompatActivity;

public class SettingsActivity extends FragmentOwner {

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
    protected int getFragmentContainerId() {
        return R.id.settings_fragment;
    }

    @Override
    public ViewModel getViewModel() {
        /* No view model for this activity */
        return null;
    }
}
