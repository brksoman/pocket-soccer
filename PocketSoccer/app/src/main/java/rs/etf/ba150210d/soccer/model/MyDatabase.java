package rs.etf.ba150210d.soccer.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import rs.etf.ba150210d.soccer.model.dao.PlayerPairDao;
import rs.etf.ba150210d.soccer.model.dao.ScoreDao;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

@Database(entities = {PlayerPair.class, Score.class}, exportSchema = false, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance = null;

    public abstract PlayerPairDao playerPairDao();
    public abstract ScoreDao scoreDao();

    public static MyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MyDatabase.class, "pocket_soccer")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync().execute();

        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            PlayerPairDao playerPairDao = instance.playerPairDao();
            ScoreDao scoreDao = instance.scoreDao();

            /*playerPairDao.deleteAll();
            scoreDao.deleteAll();*/

            /*long id = playerPairDao.insert(new PlayerPair("Dje", "Zaburesk"));
            scoreDao.insert(new Score(
                    id, 1, 5, 2,
                    new Date(119, 1, 19).getTime()
            ));
            scoreDao.insert(new Score(
                    id, 2, 0, 3,
                    new Date(119, 1, 25).getTime()
            ));*/

            return null;
        }
    }
}
