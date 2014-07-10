package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import com.thilek.android.qleneagles_quiz.database.models.Question;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetQuestionTask extends AsyncTask<Long, Void, Question> {

    private static final String TAG = GetQuestionTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetQuestionTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Question doInBackground(Long... arguments) {

        Long id = arguments[0];

        return AppContext.matrixFactory.getQuestionMatrix().getQuestionByID(id);
    }

    @Override
    protected void onPostExecute(Question result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
