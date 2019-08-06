package rs.etf.ba150210d.soccer.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import rs.etf.ba150210d.soccer.model.entities.PlayerPair;

@Dao
public interface PlayerPairDao {

    @Insert
    long insert(PlayerPair playerPair);

    @Delete
    void delete(PlayerPair playerPair);

    @Query("DELETE FROM player_pair")
    void deleteAll();

    @Query("SELECT * FROM player_pair")
    LiveData<List<PlayerPair>> findAllPlayerPairs();

    @Query("SELECT * FROM player_pair WHERE id = :id LIMIT 1")
    LiveData<PlayerPair> findPlayerPairById(long id);

    @Query("SELECT * FROM player_pair " +
            "WHERE name1 = :name1 AND name2 = :name2 " +
            "OR name1 = :name2 AND name2 = :name1 " +
            "LIMIT 1")
    LiveData<PlayerPair> findPlayerPairByNames(String name1, String name2);

    @Query("SELECT * FROM player_pair " +
            "WHERE name1 = :name1 AND name2 = :name2 " +
            "OR name1 = :name2 AND name2 = :name1 " +
            "LIMIT 1")
    PlayerPair findPlayerPairByNamesSync(String name1, String name2);
}
