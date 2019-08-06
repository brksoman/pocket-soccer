package rs.etf.ba150210d.soccer.model.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "score", foreignKeys = {
        @ForeignKey(entity = PlayerPair.class,
                parentColumns = "id", childColumns = "players_id",
                onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE)
})
public class Score {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "players_id")
    private long playersId;

    // winner = 0 - draw
    // winner = -1 - unfinished
    @ColumnInfo(name = "winner")
    private int winner;

    @ColumnInfo(name = "player1_points")
    private int player1Points;

    @ColumnInfo(name = "player2_points")
    private int player2Points;

    @ColumnInfo(name = "date")
    private long date;

    public Score(long playersId, int winner, int player1Points, int player2Points, long date) {
        this.playersId = playersId;
        this.winner = winner;
        this.player1Points = player1Points;
        this.player2Points = player2Points;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlayersId() {
        return playersId;
    }

    public void setPlayersId(long playersId) {
        this.playersId = playersId;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getPlayer1Points() {
        return player1Points;
    }

    public void setPlayer1Points(int player1Points) {
        this.player1Points = player1Points;
    }

    public int getPlayer2Points() {
        return player2Points;
    }

    public void setPlayer2Points(int player2Points) {
        this.player2Points = player2Points;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void initDate() {
        date = new Date().getTime();
    }
}
