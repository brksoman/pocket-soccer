package rs.etf.ba150210d.soccer.activities.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.util.FragmentOwnerInterface;

public class MainFragment extends Fragment {

    private FragmentOwnerInterface mFragmentOwner;
    private MainViewModel mViewModel;

    private PlayMetadata mLoadedPlayMetadata = null;
    private Button mLoadGameButton;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.main_button_newGame:
                        mFragmentOwner.switchFragment(FragmentOwnerInterface.NEW_GAME_FRAGMENT);
                        break;

                    case R.id.main_button_loadGame:
                        mFragmentOwner.switchFragment(FragmentOwnerInterface.LOAD_GAME_FRAGMENT);
                        break;

                    case R.id.main_button_settings:
                        mFragmentOwner.switchActivity(FragmentOwnerInterface.SETTINGS_ACTIVITY);
                        break;

                    case R.id.main_button_scores:
                        mFragmentOwner.switchActivity(FragmentOwnerInterface.SCORE_ACTIVITY);

                    case R.id.main_fab_exit:
                        mFragmentOwner.goBack();
                        break;

                    // TODO implement rest of options
                }
            }
        };

        Button newGameButton = view.findViewById(R.id.main_button_newGame);
        newGameButton.setOnClickListener(buttonListener);

        mLoadGameButton = view.findViewById(R.id.main_button_loadGame);
        mLoadGameButton.setOnClickListener(buttonListener);

        Button settingsButton = view.findViewById(R.id.main_button_settings);
        settingsButton.setOnClickListener(buttonListener);

        Button scoresButton = view.findViewById(R.id.main_button_scores);
        scoresButton.setOnClickListener(buttonListener);

        FloatingActionButton exitButton = view.findViewById(R.id.main_fab_exit);
        exitButton.setOnClickListener(buttonListener);

        if (mLoadedPlayMetadata == null) {
            mLoadGameButton.setClickable(false);
        } else {
            mLoadGameButton.setClickable(true);
        }

        return view;
    }

    public void setLoadedPlayMetadata(PlayMetadata playMetadata) {
        mLoadedPlayMetadata = playMetadata;
        if (mLoadGameButton != null) {
            if (mLoadedPlayMetadata == null) {
                mLoadGameButton.setClickable(false);
                mLoadGameButton.setBackgroundColor(Color.DKGRAY);
                mLoadGameButton.setTextColor(Color.WHITE);
            } else {
                mLoadGameButton.setClickable(true);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentOwnerInterface) {
            mFragmentOwner = (FragmentOwnerInterface) context;
            mViewModel = (MainViewModel) mFragmentOwner.getViewModel();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentOwnerInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentOwner = null;
    }
}
