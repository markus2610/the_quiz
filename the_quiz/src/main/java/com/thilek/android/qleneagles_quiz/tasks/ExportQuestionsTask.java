package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.reader.QuestionReader;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class ExportQuestionsTask extends AsyncTask<File, Void, Boolean> {

    private static final String TAG = ExportQuestionsTask.class.getSimpleName();


    private int TASK_ID;
    private TaskListener taskListener;


    public ExportQuestionsTask(TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Boolean doInBackground(File... arguments) {

        File filePath = arguments[0];

        AppContext.matrixFactory.getQuestionMatrix().deleteAllQuestions();

        return getData(filePath);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        taskListener.onTaskCompleted(TASK_ID, result);
    }


    private boolean getData(File strPath) {

        try {
            QuestionReader questionReader = new QuestionReader();
            questionReader.read(new FileInputStream(strPath));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
