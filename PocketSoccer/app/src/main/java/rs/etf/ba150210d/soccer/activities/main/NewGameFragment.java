package rs.etf.ba150210d.soccer.activities.main;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.datastructures.Team;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.util.EditTextBackEvent;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class NewGameFragment extends Fragment {

    private MainActivity mOwner;
    private MainViewModel mViewModel;

    private List<String> mTeamNames;
    private PlayMetadata mPlayMetadata;

    private EditTextBackEvent mEditTextLeftName;
    private ViewPager mLeftTeamPager;
    private TextView mLeftTeamName;

    private EditTextBackEvent mEditTextRightName;
    private ViewPager mRightTeamPager;
    private TextView mRightTeamName;

    public NewGameFragment() { /* Required empty public constructor */ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        mEditTextLeftName = view.findViewById(R.id.newGame_editText_leftName);
        mOwner.avoidUiWithEditText(mEditTextLeftName);

        mEditTextRightName = view.findViewById(R.id.newGame_editText_rightName);
        mOwner.avoidUiWithEditText(mEditTextRightName);

        initButtons(view);

        String[] teamNamesResource = getResources().getStringArray(R.array.country_names);
        mTeamNames = new ArrayList<>(Arrays.asList(teamNamesResource));

        initTeamPagers(view);

        return view;
    }

    private void initButtons(View view) {
        FloatingActionButton acceptButton = view.findViewById(R.id.newGame_fab_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String leftName = mEditTextLeftName.getText().toString();
                String rightName = mEditTextRightName.getText().toString();

                if (leftName.equals("") || rightName.equals("")) {
                    Toast.makeText(getContext(), getString(
                            R.string.enter_player_name_message), Toast.LENGTH_SHORT).show();
                } else {
                    mPlayMetadata.setLeftPlayerName(leftName);
                    mPlayMetadata.setRightPlayerName(rightName);
                    startGame();
                }
            }
        });

        FloatingActionButton backButton = view.findViewById(R.id.newGame_fab_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.goBack();
            }
        });
    }

    private void initTeamPagers(View view) {
        mLeftTeamPager = view.findViewById(R.id.newGame_pager_left);
        mLeftTeamPager.setAdapter(new TeamPagerAdapter(getContext()));

        mLeftTeamName = view.findViewById(R.id.newGame_textView_leftTeamName);
        mLeftTeamName.setText(new Team(getContext(), mLeftTeamPager.getCurrentItem()).getName());
        mLeftTeamPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                Team team = new Team(getContext(), i);
                mLeftTeamName.setText(team.getName());
                mPlayMetadata.setLeftTeam(team);
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });

        mRightTeamPager = view.findViewById(R.id.newGame_pager_right);
        mRightTeamPager.setAdapter(new TeamPagerAdapter(getContext()));

        mRightTeamName = view.findViewById(R.id.newGame_textView_rightTeamName);
        mRightTeamName.setText(new Team(getContext(), mRightTeamPager.getCurrentItem()).getName());
        mRightTeamPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                Team team = new Team(getContext(), i);
                mRightTeamName.setText(team.getName());
                mPlayMetadata.setRightTeam(team);
            }

            @Override
            public void onPageScrolled(int i, float v, int i1) { }
            @Override
            public void onPageScrollStateChanged(int i) { }
        });
    }

    public void setPlayMetadata(PlayMetadata playMetadata) {
        mPlayMetadata = playMetadata;
    }

    private void startGame() {
        SharedPreferences preferences = mOwner.getPreferences();
        mPlayMetadata.loadSettings(getContext(), preferences);

        mViewModel.setNewlyInsertedPlayerPair(mPlayMetadata.getPlayerPair());
        mViewModel.getNewlyInsertedPlayerPair().observe(this, new Observer<PlayerPair>() {
            @Override
            public void onChanged(@Nullable PlayerPair playerPair) {
                mPlayMetadata.rotateMaybe(playerPair);
                mOwner.reportNew();
                mOwner.switchActivity(FragmentOwner.PLAY_ACTIVITY);
            }
        });
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
