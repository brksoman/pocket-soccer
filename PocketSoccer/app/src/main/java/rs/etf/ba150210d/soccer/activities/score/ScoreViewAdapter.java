package rs.etf.ba150210d.soccer.activities.score;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class ScoreViewAdapter extends RecyclerView.Adapter<ScoreViewAdapter.ScoreHolder> {

    private List<Score> mScores = new ArrayList<>();
    private List<PlayerPair> mPlayerPairs = new ArrayList<>();

    private ScoreViewListener mListener = null;

    public void setScores(List<Score> scores) {
        mScores.clear();
        mScores.addAll(scores);
    }

    public void setPlayerPairs(List<PlayerPair> playerPairs) {
        mPlayerPairs.clear();
        mPlayerPairs.addAll(playerPairs);
    }

    public void setScoreViewListener(ScoreViewListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ScoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View holderView = layoutInflater.inflate(R.layout.score_holder, viewGroup, false);
        return new ScoreHolder(holderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreHolder scoreHolder, int i) {
        PlayerPair playerPair = mPlayerPairs.get(i);
        int player1Wins = 0;
        int player2Wins = 0;

        for (Score score: mScores) {
            if (score.getPlayersId() == playerPair.getId()) {
                if (score.getWinner() == 1) {
                    ++player1Wins;
                } else if (score.getWinner() == 2) {
                    ++player2Wins;
                }
            }
        }
        scoreHolder.setData(playerPair, player1Wins, player2Wins);
    }

    @Override
    public int getItemCount() {
        return mPlayerPairs.size();
    }

    public class ScoreHolder extends RecyclerView.ViewHolder {

        private TextView mPlayer1Name;
        private TextView mPlayer2Name;

        private TextView mPlayer1Wins;
        private TextView mPlayer2Wins;

        public ScoreHolder(@NonNull View itemView) {
            super(itemView);

            mPlayer1Name = itemView.findViewById(R.id.scoreHolder_text_name1);
            mPlayer2Name = itemView.findViewById(R.id.scoreHolder_text_name2);
            mPlayer1Wins = itemView.findViewById(R.id.scoreHolder_text_wins1);
            mPlayer2Wins = itemView.findViewById(R.id.scoreHolder_text_wins2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ScoreHolder.this.getAdapterPosition();
                    if (0 <= position && position < mPlayerPairs.size() && mListener != null) {
                        mListener.onClick(mPlayerPairs.get(position));
                    }
                }
            });
        }

        public void setData(PlayerPair playerPair, int wins1, int wins2) {
            mPlayer1Name.setText(playerPair.getName1());
            mPlayer2Name.setText(playerPair.getName2());

            mPlayer1Wins.setText(Integer.toString(wins1));
            mPlayer2Wins.setText(Integer.toString(wins2));
        }
    }

    public interface ScoreViewListener {
        void onClick(PlayerPair playerPair);
    }
}
