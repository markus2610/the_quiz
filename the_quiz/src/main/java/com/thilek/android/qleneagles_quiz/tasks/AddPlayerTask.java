package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class AddPlayerTask extends AsyncTask<Player, Void, Player> {

    private static final String TAG = AddPlayerTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public AddPlayerTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Player doInBackground(Player... arguments) {

        Player player = arguments[0];

        long id;

        id = AppContext.matrixFactory.getPlayerMatrix().addPlayer(player);

        return AppContext.matrixFactory.getPlayerMatrix().getPlayerByID(id);

    }

    @Override
    protected void onPostExecute(Player result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
