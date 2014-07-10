package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Player;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetPlayersByGroupTask extends AsyncTask<Long, Void, ArrayList<Player>> {

    private static final String TAG = GetPlayersByGroupTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetPlayersByGroupTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected ArrayList<Player> doInBackground(Long... arguments) {

        long group_id = arguments[0];

        return AppContext.matrixFactory.getPlayerMatrix().getAllPlayersByGroupID(group_id);
    }

    @Override
    protected void onPostExecute(ArrayList<Player> result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
