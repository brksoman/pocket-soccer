package rs.etf.ba150210d.soccer.view.score;

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
import rs.etf.ba150210d.soccer.view.util.FragmentOwnerInterface;
import rs.etf.ba150210d.soccer.view_model.ScoreViewModel;

public class AllScoreFragment extends Fragment {

    private FragmentOwnerInterface mFragmentOwner;
    private ScoreViewModel mViewModel;

    private PlayerPair mSelectedPlayerPair;

    public AllScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_score, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        RecyclerView recyclerView = view.findViewById(R.id.allScore_recyclerView);
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
                mFragmentOwner.switchFragment(FragmentOwnerInterface.PAIR_SCORE_FRAGMENT);
            }
        });

        FloatingActionButton backFab = view.findViewById(R.id.allScore_fab_back);
        backFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentOwner.goBack();
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
        if (context instanceof FragmentOwnerInterface) {
            mFragmentOwner = (FragmentOwnerInterface) context;
            mViewModel = (ScoreViewModel) mFragmentOwner.getViewModel();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentOwnerInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentOwner = null;
    }
}
