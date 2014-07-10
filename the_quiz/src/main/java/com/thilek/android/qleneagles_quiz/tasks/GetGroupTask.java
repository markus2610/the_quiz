package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetGroupTask extends AsyncTask<Long, Void, Group> {

    private static final String TAG = GetGroupTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetGroupTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Group doInBackground(Long... arguments) {

        Long group_id = arguments[0];

        return AppContext.matrixFactory.getGroupMatrix().getGroupByID(group_id);
    }

    @Override
    protected void onPostExecute(Group result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
