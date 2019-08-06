package rs.etf.ba150210d.soccer.view.util;

import android.arch.lifecycle.ViewModel;
import android.widget.Spinner;

/*
    Interface through which fragments interact with each other. The activity which contains the
    fragments is supposed to implement this interface. The fragments then communicate with each
    other through the activity using this interface.
 */
public interface FragmentOwnerInterface {
    int MAIN_FRAGMENT = 1;
    int NEW_GAME_FRAGMENT = 2;
    int LOAD_GAME_FRAGMENT = 3;

    int ALL_SCORE_FRAGMENT = 4;
    int PAIR_SCORE_FRAGMENT = 5;

    int MAIN_ACTIVITY = 1;
    int SCORE_ACTIVITY = 2;
    int SETTINGS_ACTIVITY = 3;
    int PLAY_ACTIVITY = 4;

    void switchActivity(int activityId);

    void switchFragment(int fragmentId);

    void goBack();

    ViewModel getViewModel();

    void avoidUiWithSpinner(Spinner spinner);

    void avoidUiWithEditText(EditTextBackEvent editText);
}
