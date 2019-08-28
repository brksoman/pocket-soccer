package rs.etf.ba150210d.soccer.model.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import rs.etf.ba150210d.soccer.model.entities.Score;

@Dao
public interface ScoreDao {

    @Insert
    void insert(Score score);

    @Delete
    void delete(Score score);

    @Update
    void update(Score score);

    @Query("DELETE FROM score")
    void deleteAll();

    @Query("SELECT * FROM score")
    LiveData<List<Score>> findAllScores();

    @Query("SELECT s.id, s.players_id, s.winner, s.player1_points, s.player2_points ,s.date FROM score s " +
            "INNER JOIN player_pair pp ON pp.id = s.players_id " +
            "WHERE (pp.name1 = :name1 AND pp.name2 = :name2 " +
            "OR pp.name1 = :name2 AND pp.name2 = :name1) " +
            "AND (s.winner <> -1)")
    LiveData<List<Score>> findScoresByPlayerPairNames(String name1, String name2);

    @Query("SELECT * FROM score WHERE players_id = :id")
    LiveData<List<Score>> findScoresByPlayerPairId(long id);

    @Query("SELECT * FROM score WHERE winner = -1 LIMIT 1")
    LiveData<Score> findUnfinishedScore();

    @Query("SELECT * FROM score WHERE winner = -1 LIMIT 1")
    Score findUnfinishedScoreSync();
}
