package rs.etf.ba150210d.soccer.activities.score;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import rs.etf.ba150210d.soccer.model.MyRepository;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class ScoreViewModel extends AndroidViewModel {

    private MyRepository mRepository;
    private LiveData<List<Score>> mAllScores;
    private LiveData<List<PlayerPair>> mAllPlayerPairs;
    private LiveData<List<Score>> mScoresOfSelectedPair;

    public ScoreViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MyRepository(application);

        mAllScores = mRepository.getAllScores();
        mAllPlayerPairs = mRepository.getAllPlayerPairs();
    }

    public LiveData<List<Score>> getAllScores() {
        return mAllScores;
    }

    public LiveData<List<PlayerPair>> getAllPlayerPairs() {
        return mAllPlayerPairs;
    }

    public void setSelectedPair(PlayerPair playerPair) {
        mScoresOfSelectedPair = mRepository.getScoresByPlayerPairId(playerPair.getId());
    }

    public LiveData<List<Score>> getScoresOfSelectedPair() {
        return mScoresOfSelectedPair;
    }
}
