package com.tikalk.antsmasher.teams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Team;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * @author moshe on 2017/11/15.
 */

public class TeamAdapter extends RecyclerView.Adapter<TeamViewHolder> {

    private final List<Team> data = new ArrayList<>();
    private final TeamViewHolder.TeamViewHolderListener listener;
    private final Map<String, Bitmap> icons = new HashMap<>();

    public TeamAdapter(TeamViewHolder.TeamViewHolderListener listener) {
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(TeamViewHolder holder, int position) {
        Team team = data.get(position);
        final String id = team.getId();
        Bitmap icon = icons.get(id);
        if (icon == null) {
            icon = createIcon(holder, team.getAntSpecies().getTint());
            icons.put(id, icon);
        }
        holder.bind(team, icon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId().hashCode();
    }

    public void clear() {
        setData(null);
    }

    public void setData(Collection<Team> data) {
        this.data.clear();
        this.icons.clear();

        if (data != null) {
            this.data.addAll(data);
        }

        notifyDataSetChanged();
    }

    private Bitmap createIcon(TeamViewHolder holder, @ColorInt int tint) {
        final Context context = holder.iconView.getContext();
        Bitmap ant = BitmapFactory.decodeResource(context.getResources(), R.drawable.ant_normal);
        return tintImage(ant, tint);
    }
}
