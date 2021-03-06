package com.thilek.android.qleneagles_quiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class QuestionListAdapter extends BaseAdapter {

    private List<Question> players = new ArrayList<Question>();
    private LayoutInflater layoutInflater;
    private Context context;

    public QuestionListAdapter(Context context, List<Question> groupList) {

        this.context = context;
        this.players = groupList;
        this.layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Question getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView questionTxt;
        public TextView questionStatus;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = players.get(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_question, null);

            holder = new ViewHolder();
            holder.questionTxt = (TextView) convertView.findViewById(R.id.question_text);
            holder.questionStatus = (TextView) convertView.findViewById(R.id.question_status);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.questionTxt.setText(question.question);

        if (question.question_status == Question.UNUSED) {
            holder.questionStatus.setText(context.getString(R.string.question_unused));
        } else {
            holder.questionStatus.setText(context.getString(R.string.question_used));
        }

        return convertView;
    }

}