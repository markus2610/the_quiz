package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class UpdatePlayerTask extends AsyncTask<Player, Void, Boolean> {

    private static final String TAG = UpdatePlayerTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public UpdatePlayerTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Boolean doInBackground(Player... arguments) {

        Player player = arguments[0];

        long id = AppContext.matrixFactory.getPlayerMatrix().updatePlayer(player);


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
