package com.thilek.android.qleneagles_quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class SetListAdapter extends BaseAdapter {

    private List<QuestionSet> questionSets = new ArrayList<QuestionSet>();
    private LayoutInflater layoutInflater;

    public SetListAdapter(Context context, List<QuestionSet> questionSets) {

        this.questionSets = questionSets;
        this.layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return questionSets.size();
    }

    @Override
    public QuestionSet getItem(int position) {
        return questionSets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView setName;
        public TextView setType;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuestionSet questionSet = questionSets.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.list_item_player, null);
            holder = new ViewHolder();
            holder.setName = (TextView) convertView.findViewById(R.id.player_name);
            holder.setType = (TextView) convertView.findViewById(R.id.department_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setName.setText(questionSet.set_name);
        holder.setType.setText(questionSet.set_type);


        return convertView;
    }

}