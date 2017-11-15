package com.tikalk.antsmasher.teams;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

/**
 * @author moshe on 2017/11/15.
 */

public class TeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public interface TeamViewHolderListener {
        void onTeamClick(Team team);
    }

    protected final ImageView iconView;
    protected final TextView nameView;
    protected final TextView countView;

    private final TeamViewHolderListener listener;
    private Team team;

    public TeamViewHolder(View itemView, TeamViewHolderListener listener) {
        super(itemView);
        iconView = itemView.findViewById(R.id.icon);
        nameView = itemView.findViewById(R.id.name);
        countView = itemView.findViewById(R.id.count);

        itemView.setOnClickListener(this);
        this.listener = listener;
    }

    public void bind(Team team) {
        this.team = team;
        final Context context = itemView.getContext();

        nameView.setText(team.getName());
        iconView.setImageDrawable(team.getIcon());

        final int count = team.getPlayers().size();
        countView.setText(context.getResources().getQuantityString(R.plurals.player_count, count, count));
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onTeamClick(team);
        }
    }
}
