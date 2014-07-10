package com.thilek.android.qleneagles_quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class PlayerListAdapter extends BaseAdapter {

    private List<Player> players = new ArrayList<Player>();
    private LayoutInflater layoutInflater;

    public PlayerListAdapter(Context context, List<Player> groupList) {

        this.players = groupList;
        this.layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Player getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView playerName;
        public TextView departmentName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Player player = players.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_player, null);

            holder = new ViewHolder();
            holder.playerName = (TextView) convertView.findViewById(R.id.player_name);
            holder.departmentName = (TextView) convertView.findViewById(R.id.department_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.playerName.setText(player.first_name + " " + player.last_name);
        holder.departmentName.setText(player.department);

        return convertView;
    }

}