package com.thilek.android.qleneagles_quiz.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.SetListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;
import com.thilek.android.qleneagles_quiz.tasks.GetGroupsTask;
import com.thilek.android.qleneagles_quiz.tasks.GetSetsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;

import java.util.ArrayList;


public class SetListActivity extends ListActivity implements TaskListener {

    private static final int GET_QUESTION_SETS = 1;

    private TextView emptyText;
    private SetListAdapter setListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_layout);

        emptyText = (TextView) findViewById(R.id.empty_list_text);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetSetsTask(this, GET_QUESTION_SETS).execute();

    }

    @Override
    protected void onListItemClick(ListView list, View dataElement, int position, long id) {
        QuestionSet questionSet = (QuestionSet) list.getAdapter().getItem(position);

        startActivity(new Intent(this, SetDetailActivity.class).putExtra(AppConstants.SET_ID, questionSet._id));
    }


    public void onQuestionSetClicked(View view) {
        startActivity(new Intent(this, SetDetailActivity.class));
    }

    @Override
    public void onTaskStarted(int taskID, Object object) {

    }

    @Override
    public void onTaskProgressUpdated(int taskID, Object object) {

    }

    @Override
    public void onTaskCompleted(int taskID, Object object) {

        switch (taskID) {
            case GET_QUESTION_SETS: {
                ArrayList<QuestionSet> questionSets = (ArrayList<QuestionSet>) object;

                if (questionSets.size() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    setListAdapter = new SetListAdapter(SetListActivity.this, questionSets);
                    setListAdapter(setListAdapter);
                }


            }
            break;
        }

    }

    @Override
    public void onTaskCanceled(int taskID, Object object) {

    }

    @Override
    public void onTaskError(int taskID, Object object) {

    }
}
