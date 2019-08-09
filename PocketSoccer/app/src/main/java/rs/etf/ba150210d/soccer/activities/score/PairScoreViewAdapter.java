package rs.etf.ba150210d.soccer.activities.score;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class PairScoreViewAdapter extends RecyclerView.Adapter<PairScoreViewAdapter.PairScoreHolder> {

    private List<Score> mScores = new ArrayList<>();

    public void setScores(List<Score> scores) {
        mScores.clear();
        mScores.addAll(scores);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PairScoreHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.pair_score_holder, viewGroup, false);

        return new PairScoreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PairScoreHolder pairScoreHolder, int i) {
        Score score = mScores.get(i);
        pairScoreHolder.setData(score);
    }

    @Override
    public int getItemCount() {
        return mScores.size();
    }

    public class PairScoreHolder extends RecyclerView.ViewHolder {

        private TextView mPoints1;
        private TextView mPoints2;
        private TextView mDate;

        public PairScoreHolder(@NonNull View itemView) {
            super(itemView);

            mPoints1 = itemView.findViewById(R.id.pairScoreHolder_points1);
            mPoints2 = itemView.findViewById(R.id.pairScoreHolder_points2);
            mDate = itemView.findViewById(R.id.pairScoreHolder_date);
        }

        public void setData(Score score) {
            mPoints1.setText(Integer.toString(score.getPlayer1Points()));
            mPoints2.setText(Integer.toString(score.getPlayer2Points()));

            Date date = new Date(score.getDate());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            mDate.setText(formatter.format(date));
        }
    }
}
