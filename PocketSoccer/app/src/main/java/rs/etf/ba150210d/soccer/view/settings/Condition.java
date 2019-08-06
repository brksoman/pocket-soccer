package rs.etf.ba150210d.soccer.view.settings;

import android.content.Context;

import rs.etf.ba150210d.soccer.R;

/*
    A class which describes the condition for winning a match - either by scoring a set number of
    goals, or if the set amount of time passes.
 */

public class Condition {

    public static final int CONDITION_GOALS = 1;
    public static final int CONDITION_TIME = 2;

    private int mType;
    private Context mContext;

    private int mMin;
    private int mMax;
    private int mIncrement;
    private int mValue;

    public Condition(Context context) {
        mContext = context;
        mType = -1;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        if (mType != type) {
            mType = type;

            if (mType == CONDITION_GOALS) {
                mMin = mContext.getResources().getInteger(R.integer.min_goals);
                mMax = mContext.getResources().getInteger(R.integer.max_goals);
                mIncrement = mContext.getResources().getInteger(R.integer.goals_increment);
            } else {
                mMin = mContext.getResources().getInteger(R.integer.min_time);
                mMax = mContext.getResources().getInteger(R.integer.max_time);
                mIncrement = mContext.getResources().getInteger(R.integer.time_increment);
            }
        }
    }

    public int getNormalizedMax() {
        return (mMax - mMin) / mIncrement;
    }

    public int getValue() {
        return mMin + mIncrement * mValue;
    }

    public int getNormalizedValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (value != 0) {
            mValue = (value - mMin) / mIncrement;
        } else {
            mValue = 0;
        }
    }

    public void setNormalizedValue(int value) {
        mValue = value;
    }

    public void setAppropriateNormalizedValue(
            int goalsValue, int timeValue) {
        if (mType == CONDITION_GOALS) {
            mValue = goalsValue;
        } else {
            mValue = timeValue;
        }
    }

    public void setAppropriateValue(int goalsValue, int timeValue) {
        if (mType == CONDITION_GOALS) {
            setValue(goalsValue);
        } else {
            setValue(timeValue);
        }
    }

}
