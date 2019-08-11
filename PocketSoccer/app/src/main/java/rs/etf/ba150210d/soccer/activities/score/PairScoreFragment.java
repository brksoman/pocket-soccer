package rs.etf.ba150210d.soccer.activities.score;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;

public class PairScoreFragment extends Fragment {

    private ScoreActivity mOwner;
    private ScoreViewModel mViewModel;

    private PlayerPair mSelectedPlayerPair;

    public PairScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pair_score, container, false);

        mViewModel.setSelectedPair(mSelectedPlayerPair);

        TextView name1 = view.findViewById(R.id.pairScore_name1);
        name1.setText(mSelectedPlayerPair.getName1());

        TextView name2 = view.findViewById(R.id.pairScore_name2);
        name2.setText(mSelectedPlayerPair.getName2());

        RecyclerView recyclerView = view.findViewById(R.id.pairScore_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final PairScoreViewAdapter adapter = new PairScoreViewAdapter();
        recyclerView.setAdapter(adapter);

        mViewModel.getScoresOfSelectedPair().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scores) {
                adapter.setScores(scores);
            }
        });

        FloatingActionButton backFab = view.findViewById(R.id.pairScore_fab_back);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.goBack();
            }
        });

        return view;
    }

    public void setSelectedPlayerPair(PlayerPair playerPair) {
        mSelectedPlayerPair = playerPair;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScoreActivity) {
            mOwner = (ScoreActivity) context;
            mViewModel = mOwner.getViewModel();
        } else {
            throw new RuntimeException("Owner must be ScoreActivity!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwner = null;
    }
}
