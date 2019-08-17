package rs.etf.ba150210d.soccer.activities.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import rs.etf.ba150210d.soccer.model.MyRepository;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;

public class MainViewModel extends AndroidViewModel {

    private MyRepository mRepository;

    private LiveData<PlayerPair> mNewlyInsertedPlayerPair;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = new MyRepository(application);
    }

    public void setNewlyInsertedPlayerPair(PlayerPair playerPair) {
        mNewlyInsertedPlayerPair = mRepository.insertAndGetPlayerPair(playerPair);
    }

    public LiveData<PlayerPair> getNewlyInsertedPlayerPair() {
        return mNewlyInsertedPlayerPair;
    }
}
