package rs.etf.ba150210d.soccer.activities.settings;

import android.content.Context;
import android.content.SharedPreferences;
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
import rs.etf.ba150210d.soccer.datastructures.Condition;
import rs.etf.ba150210d.soccer.datastructures.GameMetadata;

public class MainSettingsFragment extends Fragment {

    private SettingsActivity mOwner;

    private TextView mSpeedText;
    private SeekBar mSpeedSeekBar;
    private ViewPager mFieldPager;

    private TextView mConditionText;
    private TextView mConditionTypeText;
    private SeekBar mConditionSeekBar;

    private int mSpeed;
    private Condition mCondition;
    private int mConditionGoals;
    private int mConditionTime;

    public MainSettingsFragment() {
        /* Required empty public constructor */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_settings, container, false);

        initButtons(view);

        Context context = getActivity();
        SharedPreferences preferences = mOwner.getPreferences();

        initSpeed(view, preferences);
        initCondition(view, preferences);
        initRadioButtons(view, preferences);
        initFieldPager(view, preferences);

        return view;
    }

    private void initButtons(View view) {

        FloatingActionButton backButton = view.findViewById(R.id.settings_fab_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.goBack();
            }
        });

        FloatingActionButton saveButton = view.findViewById(R.id.settings_fab_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(
                        R.string.settings_saved_message), Toast.LENGTH_SHORT).show();
                saveState();
                mOwner.goBack();
            }
        });

        FloatingActionButton restoreButton = view.findViewById(R.id.settings_fab_restoreDefault);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.settings_restored_message,
                        Toast.LENGTH_SHORT).show();
                GameMetadata.restoreDefaultSettings(mOwner.getPreferences());
                mOwner.goBack();
            }
        });
    }

    private void initSpeed(View view, SharedPreferences preferences) {
        mSpeedText = view.findViewById(R.id.settings_text_speed);
        mSpeedSeekBar = view.findViewById(R.id.settings_seekBar_speed);

        mSpeed = GameMetadata.getSettingsAttribute(preferences, "speed");

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
        mConditionTypeText = view.findViewById(R.id.settings_text_conditionType);
        mConditionText = view.findViewById(R.id.settings_text_condition);
        mConditionSeekBar = view.findViewById(R.id.settings_seekBar_condition);

        int conditionType = GameMetadata.getSettingsAttribute(preferences, "conditionType");
        mConditionGoals = GameMetadata.getSettingsAttribute(preferences, "conditionGoals");
        mConditionTime = GameMetadata.getSettingsAttribute(preferences, "conditionTime");

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
        RadioGroup radioGroup = view.findViewById(R.id.settings_radioGroup_condition);

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
        mFieldPager = view.findViewById(R.id.settings_pager_field);
        mFieldPager.setAdapter(new FieldPagerAdapter(getContext()));
        mFieldPager.setCurrentItem(GameMetadata.getSettingsAttribute(preferences, "field"));
    }

    private void updateConditionType(int conditionType) {
        mCondition.setType(conditionType);

        mCondition.setAppropriateValue(mConditionGoals, mConditionTime);

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
        SharedPreferences.Editor editor = mOwner.getPreferences().edit();

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
        if (context instanceof SettingsActivity) {
            mOwner = (SettingsActivity) context;
        } else {
            throw new RuntimeException("Owner must be SettingsActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwner = null;
    }
}
