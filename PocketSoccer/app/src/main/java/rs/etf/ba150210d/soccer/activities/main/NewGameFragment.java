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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.datastructures.PlayMetadata;
import rs.etf.ba150210d.soccer.datastructures.Condition;
import rs.etf.ba150210d.soccer.util.FragmentOwnerInterface;
import rs.etf.ba150210d.soccer.util.EditTextBackEvent;

public class NewGameFragment extends Fragment {

    private FragmentOwnerInterface mFragmentOwner;
    private MainViewModel mViewModel;

    private List<String> mTeamNames;
    private PlayMetadata mPlayMetadata;

    private EditTextBackEvent mEditTextLeftName;
    private EditTextBackEvent mEditTextRightName;

    private ViewPager mLeftTeamPager;
    private ViewPager mRightTeamPager;

    public NewGameFragment() { /* Required empty public constructor */ }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        mEditTextLeftName = view.findViewById(R.id.newGame_editText_leftName);
        mFragmentOwner.avoidUiWithEditText(mEditTextLeftName);

        mEditTextRightName = view.findViewById(R.id.newGame_editText_rightName);
        mFragmentOwner.avoidUiWithEditText(mEditTextRightName);

        initButtons(view);

        String[] teamNamesResource = getResources().getStringArray(R.array.country_names);
        mTeamNames = new ArrayList<>(Arrays.asList(teamNamesResource));

        initTeamPagers(view);
        initSpinners(view);

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
                    startGame();
                }
            }
        });

        FloatingActionButton backButton = view.findViewById(R.id.newGame_fab_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentOwner.goBack();
            }
        });
    }

    private void initSpinners(View view) {
        Spinner leftSpinner = view.findViewById(R.id.newGame_spinner_left);
        initSpinner(leftSpinner);

        mPlayMetadata.setLeftTeamName(mTeamNames.get(0));
        leftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPlayMetadata.setLeftTeamName(mTeamNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner rightSpinner = view.findViewById(R.id.newGame_spinner_right);
        initSpinner(rightSpinner);

        mPlayMetadata.setRightTeamName(mTeamNames.get(0));
        rightSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPlayMetadata.setRightTeamName(mTeamNames.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initSpinner(Spinner spinner) {
        SpinnerAdapter adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_item,
                mTeamNames);
        ((ArrayAdapter) adapter).setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        mFragmentOwner.avoidUiWithSpinner(spinner);
    }

    private void initTeamPagers(View view) {
        mLeftTeamPager = view.findViewById(R.id.newGame_pager_left);

        mRightTeamPager = view.findViewById(R.id.newGame_pager_right);
    }

    public void setPlayMetadata(PlayMetadata playMetadata) {
        mPlayMetadata = playMetadata;
    }

    private void startGame() {
        mPlayMetadata.setLeftPlayerName(mEditTextLeftName.getText().toString());
        mPlayMetadata.setRightPlayerName(mEditTextRightName.getText().toString());

        SharedPreferences preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        mPlayMetadata.loadSettings(getContext(), preferences);

        mViewModel.setNewlyInsertedPlayerPair(mPlayMetadata.getPlayerPair());
        mViewModel.getNewlyInsertedPlayerPair().observe(this, new Observer<PlayerPair>() {
            @Override
            public void onChanged(@Nullable PlayerPair playerPair) {
                mPlayMetadata.rotateMaybe(playerPair);
                ((MainActivity)mFragmentOwner).reportNew();
                mFragmentOwner.switchActivity(FragmentOwnerInterface.PLAY_ACTIVITY);
            }
        });
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
