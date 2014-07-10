package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class AddGroupTask extends AsyncTask<Group, Void, Group> {

    private static final String TAG = AddGroupTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public AddGroupTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Group doInBackground(Group... arguments) {

        Group group = arguments[0];

        long id = AppContext.matrixFactory.getGroupMatrix().addGroup(group);

        return AppContext.matrixFactory.getGroupMatrix().getGroupByID(id);

    }

    @Override
    protected void onPostExecute(Group result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
