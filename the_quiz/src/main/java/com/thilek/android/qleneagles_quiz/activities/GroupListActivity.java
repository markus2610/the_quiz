package com.thilek.android.qleneagles_quiz.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.GroupListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.tasks.GetGroupsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;

import java.util.ArrayList;


public class GroupListActivity extends ListActivity implements TaskListener {

    private static final int GET_GROUPS = 1;

    private TextView emptyText;
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_layout);

        emptyText = (TextView) findViewById(R.id.empty_list_text);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetGroupsTask(this, GET_GROUPS).execute();

    }

    @Override
    protected void onListItemClick(ListView list, View dataElement, int position, long id) {
        Group group = (Group) list.getAdapter().getItem(position);

        startActivity(new Intent(this, GroupDetailActivity.class).putExtra(AppConstants.GROUP_ID, group._id));
    }


    public void onAddGroupClicked(View view) {
        startActivity(new Intent(this, GroupDetailActivity.class));
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
            case GET_GROUPS: {
                ArrayList<Group> groups = (ArrayList<Group>) object;

                if (groups.size() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    groupListAdapter = new GroupListAdapter(GroupListActivity.this, groups);
                    setListAdapter(groupListAdapter);
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
