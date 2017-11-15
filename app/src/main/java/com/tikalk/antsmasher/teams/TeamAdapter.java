package com.tikalk.antsmasher.teams;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

/**
 * @author moshe on 2017/11/15.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamViewHolder> {

    private final List<Team> data = new ArrayList<>();

    public TeamAdapter() {
        setHasStableIds(true);
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Team team = data.get(position);
        holder.bind(team);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public void clear() {
        setData(null);
    }

    public void setData(Collection<Team> data) {
        this.data.clear();

        if (data != null) {
            this.data.addAll(data);
        }

        notifyDataSetChanged();
    }
}
