package com.thilek.android.qleneagles_quiz.tasks;

import android.os.AsyncTask;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Question;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class SetUsedQuestionTask extends AsyncTask<Question, Void, Boolean> {

    private static final String TAG = SetUsedQuestionTask.class.getSimpleName();

    @Override
    protected Boolean doInBackground(Question... arguments) {

        Question question = arguments[0];
        question.question_status = Question.USED;

        long id = AppContext.matrixFactory.getQuestionMatrix().updateQuestion(question);


        if (id < 0) {
            return false;
        } else {
            return true;
        }
    }

}
