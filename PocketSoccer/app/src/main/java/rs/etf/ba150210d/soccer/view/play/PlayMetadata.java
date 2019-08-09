package rs.etf.ba150210d.soccer.view.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;
import rs.etf.ba150210d.soccer.view.settings.Condition;
import rs.etf.ba150210d.soccer.view.settings.Field;

/*
    Class containing info such as player names, team names, win condition, elapsed time, whose turn
    is next, etc. Also implements loading and saving the metadata for a saved game (from and to
    shared preferences), as well as packing and unpacking the metadata to and from an Intent object.
 */

public class PlayMetadata {
    public static final int UNDEFINED_PLAYER = -1;
    public static final int NO_PLAYER = 0;
    public static final int LEFT_PLAYER = 1;
    public static final int RIGHT_PLAYER = 2;

    private PlayerPair mPlayerPair;
    private Score mScore;
    private boolean mIsSameOrder = true;

    private String mLeftTeamName;
    private String mRightTeamName;

    private long mCurrentTime = 0;
    private long mElapsedTime = 0;

    private Condition mCondition;
    private int mSpeed;
    private Field mField;

    private int mNextPlayer = LEFT_PLAYER;

    public PlayMetadata(Context context) {
        mPlayerPair = new PlayerPair("", "");
        mScore = new Score(-1, -1, 0, 0, 0);
        mCondition = new Condition(context);
    }

    public PlayMetadata(Context context, Intent intent) {
        mPlayerPair = new PlayerPair(
                intent.getStringExtra("player1Name"),
                intent.getStringExtra("player2Name"));
        mPlayerPair.setId(
                intent.getLongExtra("playerPairId", 0));

        mScore = new Score(
                mPlayerPair.getId(), -1,
                intent.getIntExtra("player1Points", 0),
                intent.getIntExtra("player2Points", 0),
                intent.getLongExtra("date", 0));
        mScore.setId(
                intent.getLongExtra("scoreId", 0));

        mIsSameOrder = intent.getBooleanExtra("isSameOrder", true);
        mLeftTeamName = intent.getStringExtra("leftTeamName");
        mRightTeamName = intent.getStringExtra("rightTeamName");

        mElapsedTime = intent.getLongExtra("elapsedTime", 0);

        mCondition = new Condition(context);
        mCondition.setType(
                intent.getIntExtra("conditionType", Condition.CONDITION_GOALS));
        mCondition.setValue(
                intent.getIntExtra("conditionValue", 0));
        mSpeed = intent.getIntExtra("speed", 0);
        mNextPlayer = intent.getIntExtra("nextPlayer", LEFT_PLAYER);

        int fieldIndex = intent.getIntExtra("field", 0);
        mField = new Field(context, fieldIndex);
    }

    public PlayMetadata(Context context, SharedPreferences preferences) {
        mPlayerPair = new PlayerPair(
                preferences.getString("save_player1Name", ""),
                preferences.getString("save_player2Name", ""));
        mPlayerPair.setId(
                preferences.getLong("save_playerPairId", 0));

        mScore = new Score(
                mPlayerPair.getId(), -1,
                preferences.getInt("save_player1Points", 0),
                preferences.getInt("save_player2Points", 0),
                preferences.getLong("save_date", 0));
        mScore.setId(
                preferences.getLong("save_scoreId", 0));

        mIsSameOrder = preferences.getBoolean("save_isSameOrder", true);
        mLeftTeamName = preferences.getString("save_leftTeamName", "");
        mRightTeamName = preferences.getString("save_rightTeamName", "");

        mElapsedTime = preferences.getLong("save_elapsedTime", 0);

        mCondition = new Condition(context);
        mCondition.setType(
                preferences.getInt("save_conditionType", Condition.CONDITION_GOALS));
        mCondition.setValue(
                preferences.getInt("save_conditionValue", 0));
        mSpeed = preferences.getInt("speed", 0);
        mNextPlayer = preferences.getInt("save_nextPlayer", LEFT_PLAYER);

        int fieldIndex = preferences.getInt("field", 0);
        mField = new Field(context, fieldIndex);
    }

    public void rotateMaybe(PlayerPair playerPair) {
        if (mPlayerPair.getName1().equals(playerPair.getName2()) &&
                mPlayerPair.getName2().equals(playerPair.getName1())) {
            mIsSameOrder = !mIsSameOrder;
        }
        mPlayerPair = playerPair;
    }

    public void pack(Intent intent) {
        intent.putExtra("playerPairId", mPlayerPair.getId());
        intent.putExtra("player1Name", mPlayerPair.getName1());
        intent.putExtra("player2Name", mPlayerPair.getName2());

        intent.putExtra("scoreId", mScore.getId());
        intent.putExtra("player1Points", mScore.getPlayer1Points());
        intent.putExtra("player2Points", mScore.getPlayer2Points());
        intent.putExtra("date", mScore.getDate());

        intent.putExtra("isSameOrder", mIsSameOrder);
        intent.putExtra("leftTeamName", mLeftTeamName);
        intent.putExtra("rightTeamName", mRightTeamName);

        intent.putExtra("elapsedTime", mElapsedTime);

        intent.putExtra("conditionType", mCondition.getType());
        intent.putExtra("conditionValue", mCondition.getValue());
        intent.putExtra("speed", mSpeed);
        intent.putExtra("field", mField.getIndex());

        intent.putExtra("nextPlayer", mNextPlayer);
    }

    public int checkWin() {
        if (mCondition.getType() == Condition.CONDITION_GOALS) {
            if (mScore.getPlayer1Points() >= mCondition.getValue()) {
                mScore.setWinner(1);
                return mIsSameOrder ? LEFT_PLAYER : RIGHT_PLAYER;
            } else if (mScore.getPlayer2Points() >= mCondition.getValue()) {
                mScore.setWinner(2);
                return mIsSameOrder ? RIGHT_PLAYER : LEFT_PLAYER;
            } else {
                return NO_PLAYER;
            }
        } else {
            if (mElapsedTime >= mCondition.getValue()) {
                if (mScore.getPlayer1Points() > mScore.getPlayer2Points()) {
                    mScore.setWinner(1);
                    return mIsSameOrder ? LEFT_PLAYER : RIGHT_PLAYER;
                } else if (mScore.getPlayer1Points() < mScore.getPlayer2Points()) {
                    mScore.setWinner(2);
                    return mIsSameOrder ? RIGHT_PLAYER : LEFT_PLAYER;
                } else {
                    return UNDEFINED_PLAYER;
                }
            } else {
                return NO_PLAYER;
            }
        }
    }

    public static void deleteSave(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("save_playerPairId");
        editor.remove("save_player1Name");
        editor.remove("save_player2Name");

        editor.remove("save_scoreId");
        editor.remove("save_player1Points");
        editor.remove("save_player2Points");
        editor.remove("save_date");

        editor.remove("save_isSameOrder");
        editor.remove("save_leftTeamName");
        editor.remove("save_rightTeamName");

        editor.remove("save_elapsedTime");

        editor.remove("save_conditionType");
        editor.remove("save_conditionValue");

        editor.remove("save_nextPlayer");

        editor.apply();
    }

    public void save(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong("save_playerPairId", mPlayerPair.getId());
        editor.putString("save_player1Name", mPlayerPair.getName1());
        editor.putString("save_player2Name", mPlayerPair.getName2());

        editor.putLong("save_scoreId", mScore.getId());
        editor.putInt("save_player1Points", mScore.getPlayer1Points());
        editor.putInt("save_player2Points", mScore.getPlayer2Points());
        editor.putLong("save_date", mScore.getDate());

        editor.putBoolean("save_isSameOrder", mIsSameOrder);
        editor.putString("save_leftTeamName", mLeftTeamName);
        editor.putString("save_rightTeamName", mRightTeamName);

        editor.putLong("save_elapsedTime", mElapsedTime);

        editor.putInt("save_conditionType", mCondition.getType());
        editor.putInt("save_conditionValue", mCondition.getValue());

        editor.putInt("save_nextPlayer", mNextPlayer);

        editor.apply();
    }

    public void loadSettings(Context context, SharedPreferences preferences) {
        mCondition.setType(
                preferences.getInt("conditionType", Condition.CONDITION_GOALS));

        if (mCondition.getType() == Condition.CONDITION_GOALS) {
            mCondition.setValue(preferences.getInt("conditionGoals", 0));
        } else {
            mCondition.setValue(preferences.getInt("conditionTime", 0));
        }
        mSpeed = preferences.getInt("speed", 0);
        mField = new Field(context, preferences.getInt("field", 0));
    }

    public PlayerPair getPlayerPair() {
        return mPlayerPair;
    }

    public void setPlayerPair(PlayerPair playerPair) {
        mPlayerPair = playerPair;
    }

    public Score getScore() {
        return mScore;
    }

    public String getLeftPlayerName() {
        if (mIsSameOrder) {
            return mPlayerPair.getName1();
        } else {
            return mPlayerPair.getName2();
        }
    }

    public void setLeftPlayerName(String leftPlayerName) {
        if (mIsSameOrder) {
            mPlayerPair.setName1(leftPlayerName);
        } else {
            mPlayerPair.setName2(leftPlayerName);
        }
    }

    public String getRightPlayerName() {
        if (mIsSameOrder) {
            return mPlayerPair.getName2();
        } else {
            return mPlayerPair.getName1();
        }
    }

    public void setRightPlayerName(String rightPlayerName) {
        if (mIsSameOrder) {
            mPlayerPair.setName2(rightPlayerName);
        } else {
            mPlayerPair.setName1(rightPlayerName);
        }
    }

    public int getLeftPlayerPoints() {
        if (mIsSameOrder) {
            return mScore.getPlayer1Points();
        } else {
            return mScore.getPlayer2Points();
        }
    }

    public void scoreLeftPlayer() {
        if (mIsSameOrder) {
            mScore.setPlayer1Points(getScore().getPlayer1Points() + 1);
        } else {
            mScore.setPlayer2Points(getScore().getPlayer2Points() + 1);
        }
        mNextPlayer = RIGHT_PLAYER;
    }

    public int getRightPlayerPoints() {
        if (mIsSameOrder) {
            return mScore.getPlayer2Points();
        } else {
            return mScore.getPlayer1Points();
        }
    }

    public void scoreRightPlayer() {
        if (mIsSameOrder) {
            mScore.setPlayer2Points(mScore.getPlayer2Points() + 1);
        } else {
            mScore.setPlayer1Points(mScore.getPlayer1Points() + 1);
        }
        mNextPlayer = LEFT_PLAYER;
    }

    public boolean isSameOrder() {
        return mIsSameOrder;
    }

    public void setIsSameOrder(boolean isSameOrder) {
        mIsSameOrder = isSameOrder;
    }

    public String getLeftTeamName() {
        return mLeftTeamName;
    }

    public void setLeftTeamName(String leftTeamName) {
        mLeftTeamName = leftTeamName;
    }

    public String getRightTeamName() {
        return mRightTeamName;
    }

    public void setRightTeamName(String rightTeamName) {
        mRightTeamName = rightTeamName;
    }

    public int getElapsedTime() {
        return (int) (mElapsedTime / 1000);
    }

    public void setElapsedTime(int elapsedTime) {
        mElapsedTime = elapsedTime;
    }

    public boolean elapseTime() {
        if (mCurrentTime == 0) { mCurrentTime = System.currentTimeMillis(); }
        long oldTime = mCurrentTime;
        mCurrentTime = System.currentTimeMillis();
        mElapsedTime += mCurrentTime - oldTime;

        if (mCondition.getType() == Condition.CONDITION_TIME
                && mElapsedTime + (mCurrentTime - oldTime) >= mCondition.getValue()) {
            return false;
        } else {
            return true;
        }
    }

    public Condition getCondition() {
        return mCondition;
    }

    public void setCondition(Condition condition) {
        mCondition = condition;
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public int getNextPlayer() {
        return mNextPlayer;
    }

    public void switchNextPlayer() {
        mNextPlayer = (mNextPlayer == LEFT_PLAYER) ? RIGHT_PLAYER : LEFT_PLAYER;
    }

    public Field getField() {
        return mField;
    }

    public void setField(Field field) {
        mField = field;
    }
}
