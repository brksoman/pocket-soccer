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

import java.util.List;

import rs.etf.ba150210d.soccer.R;
import rs.etf.ba150210d.soccer.model.entities.PlayerPair;
import rs.etf.ba150210d.soccer.model.entities.Score;
import rs.etf.ba150210d.soccer.util.FragmentOwner;

public class AllScoreFragment extends Fragment {

    private ScoreActivity mOwner;
    private ScoreViewModel mViewModel;

    private PlayerPair mSelectedPlayerPair;

    public AllScoreFragment() {
        /* Required empty public constructor */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_score, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.allScore_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final ScoreViewAdapter adapter = new ScoreViewAdapter();
        recyclerView.setAdapter(adapter);

        mViewModel.getAllScores().observe(this, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scores) {
                adapter.setScores(scores);
                adapter.notifyDataSetChanged();
            }
        });

        mViewModel.getAllPlayerPairs().observe(this, new Observer<List<PlayerPair>>() {
            @Override
            public void onChanged(@Nullable List<PlayerPair> playerPairs) {
                adapter.setPlayerPairs(playerPairs);
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setScoreViewListener(new ScoreViewAdapter.ScoreViewListener() {
            @Override
            public void onClick(PlayerPair playerPair) {
                mSelectedPlayerPair = playerPair;
                mOwner.switchFragment(FragmentOwner.PAIR_SCORE_FRAGMENT);
            }
        });

        FloatingActionButton backFab = view.findViewById(R.id.allScore_fab_back);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwner.goBack();
            }
        });

        FloatingActionButton deleteFab = view.findViewById(R.id.allScore_fab_delete);
        deleteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteAll();
                mViewModel.getAllPlayerPairs().observe(AllScoreFragment.this,
                        new Observer<List<PlayerPair>>() {
                    @Override
                    public void onChanged(@Nullable List<PlayerPair> playerPairs) {
                        adapter.setPlayerPairs(playerPairs);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }

    public PlayerPair getSelectedPlayerPair() {
        return mSelectedPlayerPair;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScoreActivity) {
            mOwner = (ScoreActivity) context;
            mViewModel = mOwner.getViewModel();
        } else {
            throw new RuntimeException("Owner must be ScoreActivity");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOwner = null;
    }
}
