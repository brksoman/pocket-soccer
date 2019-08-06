package rs.etf.ba150210d.soccer.view_model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import rs.etf.ba150210d.soccer.model.MyRepository;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;
import rs.etf.ba150210d.soccer.view.play.PlayData;
import rs.etf.ba150210d.soccer.view.play.PlayMetadata;

public class PlayViewModel extends AndroidViewModel {

    private MyRepository mRepository;
    private LiveData<PlayerPair> mPlayerPairById = null;

    private PlayMetadata mMetadata = null;
    private PlayData mData = null;

    public PlayViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MyRepository(application);
    }

    public void setPlayerPairById(long id) {
        mPlayerPairById = mRepository.getPlayerPairById(id);
    }

    public LiveData<PlayerPair> getPlayerPairById() {
        return mPlayerPairById;
    }

    public void insertScore() {
        if (mMetadata != null) {
            Score score = mMetadata.getScore();
            score.initDate();
            mRepository.insertScore(score);
        }
    }

    public void setMetadata(PlayMetadata metadata) {
        mMetadata = metadata;
    }

    public void setData(PlayData data) {
        mData = data;
    }

    public PlayMetadata getMetadata() {
        return mMetadata;
    }

    public PlayData getData() {
        return mData;
    }
}
