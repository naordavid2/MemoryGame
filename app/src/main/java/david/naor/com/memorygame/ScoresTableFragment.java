package david.naor.com.memorygame;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import david.naor.com.memorygame.Adapters.ScoresAdapter;
import david.naor.com.memorygame.Data.Score;

public class ScoresTableFragment extends Fragment {
    private ScoresAdapter scoresAdapter;
    private GameSelectActivity myActivity;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ArrayList<Score> data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =   inflater.inflate(R.layout.scores_table,container,false);
        myActivity = (GameSelectActivity) getActivity();
        data = myActivity.getScores();
        recyclerView = view.findViewById(R.id.recycler_scores);
        layoutManager = new LinearLayoutManager(myActivity);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        scoresAdapter = new ScoresAdapter(data);
        recyclerView.setAdapter(scoresAdapter);

        DividerItemDecoration itemDecor = new DividerItemDecoration(myActivity,DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

        return view;
    }

    public void notifyOnDataChange(){
        data = myActivity.getScores();
        scoresAdapter.notifyDataSetChanged();
    }
}
