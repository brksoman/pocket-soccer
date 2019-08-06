package rs.etf.ba150210d.soccer.model;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import rs.etf.ba150210d.soccer.model.dao.PlayerPairDao;
import rs.etf.ba150210d.soccer.model.dao.ScoreDao;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class MyRepository {

    private PlayerPairDao mPlayerPairDao;
    private ScoreDao mScoreDao;

    public MyRepository(Context context) {
        MyDatabase database = MyDatabase.getInstance(context);

        mPlayerPairDao = database.playerPairDao();
        mScoreDao = database.scoreDao();
    }

    public void insertPlayerPair(PlayerPair playerPair) {
        new InsertPlayerPairAsync(mPlayerPairDao).execute(playerPair);
    }

    public void insertScore(Score score) {
        new InsertScoreAsync(mScoreDao).execute(score);
    }

    public LiveData<List<PlayerPair>> getAllPlayerPairs() {
        return mPlayerPairDao.findAllPlayerPairs();
    }

    public LiveData<List<Score>> getAllScores() {
        return mScoreDao.findAllScores();
    }

    public LiveData<PlayerPair> getPlayerPairById(long id) {
        return mPlayerPairDao.findPlayerPairById(id);
    }

    public LiveData<PlayerPair> getPlayerPairByNames(String name1, String name2) {
        return mPlayerPairDao.findPlayerPairByNames(name1, name2);
    }

    public LiveData<List<Score>> getScoresByPlayerPairId(long id) {
        return mScoreDao.findScoresByPlayerPairId(id);
    }

    public LiveData<List<Score>> getScoresByPlayerPairNames(String name1, String name2) {
        return mScoreDao.findScoresByPlayerPairNames(name1, name2);
    }

    public LiveData<Score> getUnfinishedScore() {
        return mScoreDao.findUnfinishedScore();
    }

    public LiveData<PlayerPair> insertAndGetPlayerPair(PlayerPair playerPair) {
        try {
            return new InsertAndGetPlayerPairAsync(mPlayerPairDao).execute(playerPair).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InsertPlayerPairAsync extends AsyncTask<PlayerPair, Void, Void> {
        private PlayerPairDao mPlayerPairDao;

        public InsertPlayerPairAsync(PlayerPairDao playerPairDao) {
            mPlayerPairDao = playerPairDao;
        }

        @Override
        protected Void doInBackground(PlayerPair... playerPairs) {
            mPlayerPairDao.insert(playerPairs[0]);
            return null;
        }
    }

    private static class InsertScoreAsync extends AsyncTask<Score, Void, Void> {
        private ScoreDao mScoreDao;

        public InsertScoreAsync(ScoreDao scoreDao) {
            mScoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Score... scores) {
            mScoreDao.insert(scores[0]);
            return null;
        }
    }

    private static class DeleteAllScoresAsync extends AsyncTask<Void, Void, Void> {
        private ScoreDao mScoreDao;

        public DeleteAllScoresAsync(ScoreDao scoreDao) {
            mScoreDao = scoreDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mScoreDao.deleteAll();
            return null;
        }
    }

    private static class InsertAndGetPlayerPairAsync
            extends AsyncTask<PlayerPair, Void, LiveData<PlayerPair>> {
        private PlayerPairDao mPlayerPairDao;

        public InsertAndGetPlayerPairAsync(PlayerPairDao playerPairDao) {
            mPlayerPairDao = playerPairDao;
        }

        @Override
        protected LiveData<PlayerPair> doInBackground(PlayerPair... playerPairs) {
            PlayerPair existentPlayerPair = mPlayerPairDao.findPlayerPairByNamesSync(
                    playerPairs[0].getName1(), playerPairs[0].getName2());

            long id;
            if (existentPlayerPair != null) {
                id = existentPlayerPair.getId();
            } else {
                id = mPlayerPairDao.insert(playerPairs[0]);
            }
            return mPlayerPairDao.findPlayerPairById(id);
        }
    }
}
