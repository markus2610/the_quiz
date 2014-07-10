package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetSetsTask extends AsyncTask<Void, Void, ArrayList<QuestionSet>> {

    private static final String TAG = GetSetsTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetSetsTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected ArrayList<QuestionSet> doInBackground(Void... arguments) {
        return AppContext.matrixFactory.getQuestionSetMatrix().getAllQuestionSets();
    }

    @Override
    protected void onPostExecute(ArrayList<QuestionSet> result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
