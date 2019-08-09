package rs.etf.ba150210d.soccer.view.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.view.util.FragmentOwnerInterface;

public class MainSettingsFragment extends Fragment {

    private FragmentOwnerInterface mFragmentOwner;

    private TextView mSpeedText;
    private SeekBar mSpeedSeekBar;
    private ViewPager mFieldPager;
    private FieldPagerAdapter mFieldPagerAdapter;

    private TextView mConditionText;
    private TextView mConditionTypeText;
    private SeekBar mConditionSeekBar;

    private int mSpeed;
    private Condition mCondition;
    private int mConditionGoals;
    private int mConditionTime;

    public MainSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_settings, container, false);
        view.setBackgroundColor(Color.rgb(50, 150, 50));

        initButtons(view);

        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        initSpeed(view, preferences);
        initCondition(view, preferences);
        initRadioButtons(view, preferences);
        initFieldPager(view, preferences);

        return view;
    }

    private void initButtons(View view) {
        FloatingActionButton.OnClickListener fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.settings_fab_back:
                        mFragmentOwner.goBack();
                        break;

                    case R.id.settings_fab_save:
                        Toast.makeText(getContext(), getString(
                                R.string.settings_saved_message), Toast.LENGTH_SHORT).show();
                        saveState();
                        mFragmentOwner.goBack();
                }
            }
        };

        FloatingActionButton backButton = view.findViewById(R.id.settings_fab_back);
        backButton.setOnClickListener(fabListener);

        FloatingActionButton saveButton = view.findViewById(R.id.settings_fab_save);
        saveButton.setOnClickListener(fabListener);
    }

    private void initSpeed(View view, SharedPreferences preferences) {
        mSpeedText = view.findViewById(R.id.settings_textView_speed);
        mSpeedSeekBar = view.findViewById(R.id.settings_seekBar_speed);

        mSpeed = preferences.getInt("speed", 0);

        mSpeedText.setText(Integer.toString(mSpeed + 1));
        mSpeedSeekBar.setProgress(mSpeed);

        mSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSpeed = progress;
                mSpeedText.setText(Integer.toString(mSpeed + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void initCondition(View view, SharedPreferences preferences) {
        mConditionTypeText = view.findViewById(R.id.settings_textView_conditionType);
        mConditionText = view.findViewById(R.id.settings_textView_condition);
        mConditionSeekBar = view.findViewById(R.id.settings_seekBar_condition);

        int conditionType = preferences.getInt("conditionType", Condition.CONDITION_GOALS);
        mConditionGoals = preferences.getInt("conditionGoals", 0);
        mConditionTime = preferences.getInt("conditionTime", 0);

        mCondition = new Condition(getContext());
        updateConditionType(conditionType);

        mConditionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mCondition.setNormalizedValue(progress);

                    if (mCondition.getType() == Condition.CONDITION_GOALS) {
                        mConditionGoals = mCondition.getValue();
                    } else {
                        mConditionTime = mCondition.getValue();
                    }

                    mConditionText.setText(Integer.toString(mCondition.getValue()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void initRadioButtons(View view, SharedPreferences preferences) {
        RadioGroup radioGroup = view.findViewById(R.id.settings_radioGroup_conditionChoice);

        if (mCondition.getType() == Condition.CONDITION_GOALS) {
            radioGroup.check(R.id.settings_radioButton_goals);
        } else {
            radioGroup.check(R.id.settings_radioButton_time);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.settings_radioButton_goals:
                        updateConditionType(Condition.CONDITION_GOALS);
                        break;

                    case R.id.settings_radioButton_time:
                        updateConditionType(Condition.CONDITION_TIME);
                        break;
                }
            }
        });
    }

    private void initFieldPager(View view, SharedPreferences preferences) {
        mFieldPager = view.findViewById(R.id.settings_viewPager_field);
        mFieldPagerAdapter = new FieldPagerAdapter();

        mFieldPagerAdapter.setContext(getContext());
        mFieldPager.setAdapter(mFieldPagerAdapter);

        mFieldPager.setCurrentItem(preferences.getInt("field", 0));
    }

    private void updateConditionType(int conditionType) {
        mCondition.setType(conditionType);

        mCondition.setAppropriateValue(
                mConditionGoals, mConditionTime);

        mConditionText.setText(Integer.toString(mCondition.getValue()));
        mConditionSeekBar.setMax(mCondition.getNormalizedMax());
        mConditionSeekBar.setProgress(mCondition.getNormalizedValue());

        if (conditionType == Condition.CONDITION_GOALS) {
            mConditionTypeText.setText(R.string.goals);
        } else {
            mConditionTypeText.setText(R.string.time);
        }
    }

    private void saveState() {
        SharedPreferences preferences = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("speed", mSpeed);
        editor.putInt("conditionType", mCondition.getType());
        editor.putInt("conditionGoals", mConditionGoals);
        editor.putInt("conditionTime", mConditionTime);
        editor.putInt("field", mFieldPager.getCurrentItem());

        editor.apply();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentOwnerInterface) {
            mFragmentOwner = (FragmentOwnerInterface) context;
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
