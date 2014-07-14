package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Question;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GetAllQuestionsTask extends AsyncTask<Void, Void, ArrayList<Question>> {

    private static final String TAG = GetAllQuestionsTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public GetAllQuestionsTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected ArrayList<Question> doInBackground(Void... arguments) {

        return AppContext.matrixFactory.getQuestionMatrix().getAllQuestions();
    }

    @Override
    protected void onPostExecute(ArrayList<Question> result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }

}
