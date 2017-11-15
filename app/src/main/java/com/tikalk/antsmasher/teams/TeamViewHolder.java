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

public class TeamViewHolder extends RecyclerView.ViewHolder {

    protected final ImageView iconView;
    protected final TextView nameView;
    protected final TextView countView;

    public TeamViewHolder(View itemView) {
        super(itemView);
        iconView = itemView.findViewById(R.id.icon);
        nameView = itemView.findViewById(R.id.name);
        countView = itemView.findViewById(R.id.count);
    }

    public void bind(Team team) {
        final Context context = itemView.getContext();

        nameView.setText(team.getName());
        iconView.setImageDrawable(team.getIcon());

        final int count = team.getPlayers().size();
        countView.setText(context.getResources().getQuantityString(R.plurals.player_count, count, count));
    }
}
