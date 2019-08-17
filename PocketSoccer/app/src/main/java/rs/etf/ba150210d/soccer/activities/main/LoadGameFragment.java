package rs.etf.ba150210d.soccer.activities.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;
import rs.etf.ba150210d.soccer.datastructures.Condition;
import rs.etf.ba150210d.soccer.datastructures.Field;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class LoadGameFragment extends Fragment {

    private MainActivity mOwner;
    private MainViewModel mViewModel;

    private GameMetadata mGameMetadata = null;

    public LoadGameFragment() {
        /* Required empty public constructor */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_game, container, false);

        if (mGameMetadata != null) {
            displayGameMetadata(view);
        }

        initButtons(view);

        return view;
    }

    private void initButtons(View view) {

        FloatingActionButton acceptButton = view.findViewById(R.id.loadGame_fab_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.reportLoad();
                SharedPreferences preferences = mOwner.getPreferences();

                mGameMetadata.setSpeed(
                        preferences.getInt("speed", mGameMetadata.getSpeed()));
                mGameMetadata.setField(new Field(getContext(),
                        preferences.getInt("field", 0)));
                mOwner.switchActivity(FragmentOwner.PLAY_ACTIVITY);
            }
        });

        FloatingActionButton backButton = view.findViewById(R.id.loadGame_fab_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.goBack();
            }
        });
    }

    private void displayGameMetadata(View view) {

        TextView leftPlayer = view.findViewById(R.id.loadGame_text_leftPlayerName);
        leftPlayer.setText(mGameMetadata.getLeftPlayerName());

        TextView rightPlayer = view.findViewById(R.id.loadGame_text_rightPlayerName);
        rightPlayer.setText(mGameMetadata.getRightPlayerName());

        TextView leftPoints = view.findViewById(R.id.loadGame_text_leftPoints);
        leftPoints.setText(Integer.toString(mGameMetadata.getLeftPlayerPoints()));

        TextView rightPoints = view.findViewById(R.id.loadGame_text_rightPoints);
        rightPoints.setText(Integer.toString(mGameMetadata.getRightPlayerPoints()));

        TextView leftTeam = view.findViewById(R.id.loadGame_text_leftTeamName);
        leftTeam.setText(mGameMetadata.getLeftTeam().getName());

        TextView rightTeam = view.findViewById(R.id.loadGame_text_rightTeamName);
        rightTeam.setText(mGameMetadata.getRightTeam().getName());

        ImageView leftTeamFlag = view.findViewById(R.id.loadGame_image_leftTeamFlag);
        leftTeamFlag.setImageResource(mGameMetadata.getLeftTeam().getId());

        ImageView rightTeamFlag = view.findViewById(R.id.loadGame_image_rightTeamFlag);
        rightTeamFlag.setImageResource(mGameMetadata.getRightTeam().getId());

        TextView elapsedTime = view.findViewById(R.id.loadGame_text_elapsedTime);
        String elapsedTimeString = getString(R.string.time_format,
                mGameMetadata.getElapsedTime() / 60,
                mGameMetadata.getElapsedTime() % 60);
        elapsedTime.setText(elapsedTimeString);

        TextView conditionTextView = view.findViewById(R.id.loadGame_text_condition);
        Condition condition = mGameMetadata.getCondition();

        if (condition.getType() == Condition.CONDITION_GOALS) {
            String conditionString = getString(
                    R.string.condition_goals_format, condition.getValue());
            conditionTextView.setText(conditionString);
        } else {
            String conditionString = getString(R.string.condition_time_format,
                    condition.getValue() / 60, condition.getValue() % 60);
            conditionTextView.setText(conditionString);
        }
    }

    public void setGameMetadata(GameMetadata gameMetadata) {
        mGameMetadata = gameMetadata;
        if (mGameMetadata != null && isAdded()) {
            displayGameMetadata(getView());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mOwner = (MainActivity) context;
            mViewModel = mOwner.getViewModel();
        } else {
            throw new RuntimeException("Owner must be MainActivity!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwner = null;
    }
}
