package com.thilek.android.qleneagles_quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GroupListAdapter extends BaseAdapter {

    private ArrayList<Group> groups = new ArrayList<Group>();
    private LayoutInflater layoutInflater;

    public GroupListAdapter(Context context, ArrayList<Group> groupList) {

        this.groups = groupList;
        this.layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return groups.size();
    }

    @Override
    public Group getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView groupName;
        public ImageView groupPhoto;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group group = groups.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.list_item_group, null);
            holder = new ViewHolder();
            holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
            holder.groupPhoto = (ImageView) convertView
                    .findViewById(R.id.group_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.groupName.setText(group.group_name);


        return convertView;
    }

}