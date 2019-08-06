package rs.etf.ba150210d.soccer.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import rs.etf.ba150210d.soccer.model.MyRepository;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class MainViewModel extends AndroidViewModel {

    private MyRepository mRepository;
    private LiveData<Score> mUnfinishedScore;
    private LiveData<PlayerPair> mSelectedPlayerPair;
    private LiveData<PlayerPair> mNewlyInsertedPlayerPair;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MyRepository(application);
        mUnfinishedScore = mRepository.getUnfinishedScore();
    }

    public LiveData<Score> getUnfinishedScore() {
        return mUnfinishedScore;
    }

    public void setSelectedPlayerPairByScore(Score score) {
        mSelectedPlayerPair = mRepository.getPlayerPairById(score.getPlayersId());
    }

    public LiveData<PlayerPair> getSelectedPlayerPair() {
        return mSelectedPlayerPair;
    }

    public void setNewlyInsertedPlayerPair(PlayerPair playerPair) {
        mNewlyInsertedPlayerPair = mRepository.insertAndGetPlayerPair(playerPair);
    }

    public LiveData<PlayerPair> getNewlyInsertedPlayerPair() {
        return mNewlyInsertedPlayerPair;
    }
}
