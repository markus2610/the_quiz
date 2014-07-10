package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetSetTask extends AsyncTask<Long, Void, QuestionSet> {

    private static final String TAG = GetSetTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetSetTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected QuestionSet doInBackground(Long... arguments) {

        Long set_id = arguments[0];

        return AppContext.matrixFactory.getQuestionSetMatrix().getQuestionSetByID(set_id);
    }

    @Override
    protected void onPostExecute(QuestionSet result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
