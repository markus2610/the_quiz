package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class UpdateGroupTask extends AsyncTask<Group, Void, Boolean> {

    private static final String TAG = UpdateGroupTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public UpdateGroupTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Boolean doInBackground(Group... arguments) {

        Group group = arguments[0];

        long id = AppContext.matrixFactory.getGroupMatrix().updateGroup(group);


        if (id < 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
