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
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.datastructures.Condition;
import rs.etf.ba150210d.soccer.datastructures.Field;
import rs.etf.ba150210d.soccer.util.FragmentOwnerInterface;

public class LoadGameFragment extends Fragment {

    private FragmentOwnerInterface mFragmentOwner;
    private MainViewModel mViewModel;

    private PlayMetadata mPlayMetadata = null;

    public LoadGameFragment() {
        /* Required empty public constructor */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_game, container, false);

        if (mPlayMetadata != null) {
            TextView leftPlayer = view.findViewById(R.id.loadGame_leftPlayer);
            leftPlayer.setText(mPlayMetadata.getLeftPlayerName());

            TextView rightPlayer = view.findViewById(R.id.loadGame_rightPlayer);
            rightPlayer.setText(mPlayMetadata.getRightPlayerName());

            TextView leftPoints = view.findViewById(R.id.loadGame_leftPoints);
            leftPoints.setText(Integer.toString(mPlayMetadata.getLeftPlayerPoints()));

            TextView rightPoints = view.findViewById(R.id.loadGame_rightPoints);
            rightPoints.setText(Integer.toString(mPlayMetadata.getRightPlayerPoints()));

            TextView leftTeam = view.findViewById(R.id.loadGame_leftTeamName);
            leftTeam.setText(mPlayMetadata.getLeftTeam().getName());

            TextView rightTeam = view.findViewById(R.id.loadGame_rightTeamName);
            rightTeam.setText(mPlayMetadata.getRightTeam().getName());

            ImageView leftTeamFlag = view.findViewById(R.id.loadGame_leftTeamFlag);
            leftTeamFlag.setImageResource(mPlayMetadata.getLeftTeam().getId());

            ImageView rightTeamFlag = view.findViewById(R.id.loadGame_rightTeamFlag);
            rightTeamFlag.setImageResource(mPlayMetadata.getRightTeam().getId());

            TextView elapsedTime = view.findViewById(R.id.loadGame_elapsedTime);
            String elapsedTimeString = getString(R.string.time_format,
                    mPlayMetadata.getElapsedTime() / 60,
                    mPlayMetadata.getElapsedTime() % 60);
            elapsedTime.setText(elapsedTimeString);

            TextView conditionTextView = view.findViewById(R.id.loadGame_condition);
            Condition condition = mPlayMetadata.getCondition();

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

        FloatingActionButton loadButton = view.findViewById(R.id.loadGame_fab_load);
        loadButton.setOnClickListener(mButtonOnClickListener);

        FloatingActionButton backButton = view.findViewById(R.id.loadGame_fab_back);
        backButton.setOnClickListener(mButtonOnClickListener);

        return view;
    }

    public void setPlayMetadata(PlayMetadata playMetadata) {
        mPlayMetadata = playMetadata;
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

    private View.OnClickListener mButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.loadGame_fab_load:
                    ((MainActivity) mFragmentOwner).reportLoad();
                    SharedPreferences preferences = getActivity().getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    mPlayMetadata.setSpeed(preferences.getInt("speed", mPlayMetadata.getSpeed()));
                    mPlayMetadata.setField(new Field(getContext(), preferences.getInt("field", 0)));
                    mFragmentOwner.switchActivity(FragmentOwnerInterface.PLAY_ACTIVITY);
                    break;

                case R.id.loadGame_fab_back:
                    mFragmentOwner.goBack();
                    break;
            }
        }
    };
}
