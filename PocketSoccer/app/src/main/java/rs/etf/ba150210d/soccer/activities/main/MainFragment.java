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
import android.widget.Toast;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class MainFragment extends Fragment {

    private MainActivity mOwner;
    private MainViewModel mViewModel;

    private GameMetadata mLoadGameMetadata = null;
    private Button mLoadGameButton;

    public MainFragment() {
        /* Required empty public constructor */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.main_button_newGame:
                        mOwner.switchFragment(FragmentOwner.NEW_GAME_FRAGMENT);
                        break;

                    case R.id.main_button_loadGame:
                        if (mLoadGameMetadata == null) {
                            Toast.makeText(getContext(), R.string.no_saved_message,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mOwner.switchFragment(FragmentOwner.LOAD_GAME_FRAGMENT);
                        }
                        break;

                    case R.id.main_button_settings:
                        mOwner.switchActivity(FragmentOwner.SETTINGS_ACTIVITY);
                        break;

                    case R.id.main_button_scores:
                        mOwner.switchActivity(FragmentOwner.SCORE_ACTIVITY);
                        break;

                    case R.id.main_fab_exit:
                        mOwner.goBack();
                        break;
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

        /*if (mLoadGameMetadata == null) {
            mLoadGameButton.setClickable(false);
        } else {
            mLoadGameButton.setClickable(true);
        }*/

        return view;
    }

    public void setLoadGameMetadata(GameMetadata gameMetadata) {
        mLoadGameMetadata = gameMetadata;
        if (mLoadGameButton != null) {
            /*if (mLoadGameMetadata == null) {
                mLoadGameButton.setClickable(false);
            } else {
                mLoadGameButton.setClickable(true);
            }*/
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mOwner = (MainActivity) context;
            mViewModel = mOwner.getViewModel();
        } else {
            throw new RuntimeException("Owner must be MainActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwner = null;
    }
}
