package com.thilek.android.qleneagles_quiz.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class LoadQuestionsTask extends AsyncTask<Integer, Void, Map<Integer, ArrayList<Question>>> {

    private static final String TAG = LoadQuestionsTask.class.getSimpleName();

    private Context context;
    private int TASK_ID;
    private TaskListener taskListener;
    private int amount;


    public LoadQuestionsTask(Context context, TaskListener taskListener, int task_id) {
        this.TASK_ID = task_id;
        this.taskListener = taskListener;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        taskListener.onTaskStarted(TASK_ID, null);
    }

    @Override
    protected Map<Integer, ArrayList<Question>> doInBackground(Integer... arguments) {

        Map<Integer, ArrayList<Question>> questions = new HashMap<Integer, ArrayList<Question>>();

        amount = arguments[0];

        questions.put(Question.DIFFICULTY_LEVEL_1, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_1, amount));
        questions.put(Question.DIFFICULTY_LEVEL_2, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_2, amount));
        questions.put(Question.DIFFICULTY_LEVEL_3, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_3, amount));
        questions.put(Question.DIFFICULTY_LEVEL_4, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_4, amount));
        questions.put(Question.DIFFICULTY_LEVEL_5, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_5, amount));
        questions.put(Question.DIFFICULTY_LEVEL_6, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_6, amount));
        questions.put(Question.DIFFICULTY_LEVEL_7, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_7, amount));
        questions.put(Question.DIFFICULTY_LEVEL_8, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_8, amount));
        questions.put(Question.DIFFICULTY_LEVEL_9, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_9, amount));
        questions.put(Question.DIFFICULTY_LEVEL_10, AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_10, amount));


        return questions;
    }

    @Override
    protected void onPostExecute(Map<Integer, ArrayList<Question>> result) {

        String errorMessage = context.getString(R.string.not_enough_questions_long);

        if (result.get(Question.DIFFICULTY_LEVEL_1).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_1));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_2).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_2));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_3).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_3));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_4).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_4));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_5).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_5));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_6).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_6));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_7).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_7));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_8).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_8));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_9).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_9));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else if (result.get(Question.DIFFICULTY_LEVEL_10).size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_10));
            taskListener.onTaskError(TASK_ID, errorMessage);
        } else {
            taskListener.onTaskCompleted(TASK_ID, result);
        }
    }

}
