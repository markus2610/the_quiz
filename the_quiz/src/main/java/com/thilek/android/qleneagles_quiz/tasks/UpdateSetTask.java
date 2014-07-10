package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class UpdateSetTask extends AsyncTask<QuestionSet, Void, Boolean> {

    private static final String TAG = UpdateSetTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public UpdateSetTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Boolean doInBackground(QuestionSet... arguments) {

        QuestionSet questionSet = arguments[0];

        long id = AppContext.matrixFactory.getQuestionSetMatrix().updateQuestionSet(questionSet);


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
