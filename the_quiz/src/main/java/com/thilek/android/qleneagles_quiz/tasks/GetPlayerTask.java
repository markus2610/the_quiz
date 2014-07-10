package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetPlayerTask extends AsyncTask<Long, Void, Player> {

    private static final String TAG = GetPlayerTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetPlayerTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Player doInBackground(Long... arguments) {

        Long player_id = arguments[0];

        return AppContext.matrixFactory.getPlayerMatrix().getPlayerByID(player_id);
    }

    @Override
    protected void onPostExecute(Player result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
