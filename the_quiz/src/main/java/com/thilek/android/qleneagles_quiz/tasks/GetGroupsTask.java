package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetGroupsTask extends AsyncTask<Void, Void, ArrayList<Group>> {

    private static final String TAG = GetGroupsTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetGroupsTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected ArrayList<Group> doInBackground(Void... arguments) {

        return AppContext.matrixFactory.getGroupMatrix().getAllGroups();
    }

    @Override
    protected void onPostExecute(ArrayList<Group> result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
