package rs.etf.ba150210d.soccer.model.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "player_pair")
public class PlayerPair {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "name1")
    private String name1;

    @ColumnInfo(name = "name2")
    private String name2;

    public PlayerPair(String name1, String name2) {
        this.id = 0;
        this.name1 = name1;
        this.name2 = name2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
