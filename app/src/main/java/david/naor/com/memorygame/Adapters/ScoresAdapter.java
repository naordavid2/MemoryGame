package david.naor.com.memorygame.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import david.naor.com.memorygame.Data.Score;
import david.naor.com.memorygame.R;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoresViewHolder> {
    private ArrayList<Score> data;

    public ScoresAdapter (ArrayList<Score> data){
        this.data = data;
    }

    public class ScoresViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewName;
        public TextView textViewScore;
        public TextView textViewPosition;

        public ScoresViewHolder(View view){
            super(view);
            textViewName = (TextView) view.findViewById(R.id.scores_text_view_name);
            textViewScore = (TextView) view.findViewById(R.id.scores_text_view_score);
            textViewPosition = (TextView) view.findViewById(R.id.scores_text_view_position);
        }
    }


    @Override
    public ScoresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_details_cell, parent, false);

        return new ScoresViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScoresViewHolder holder, int position) {
        Score score = data.get(position);
        holder.textViewName.setText(score.getName());
        holder.textViewScore.setText(String.valueOf(score.getScore()));
        holder.textViewPosition.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

}
