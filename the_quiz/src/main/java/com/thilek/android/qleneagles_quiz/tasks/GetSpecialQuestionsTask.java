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
public class GetSpecialQuestionsTask extends AsyncTask<Integer, Void,ArrayList<Question>> {

	private static final String TAG = GetSpecialQuestionsTask.class.getSimpleName();

	private Context      context;
	private TaskListener taskListener;
	private int          amount;

	public GetSpecialQuestionsTask( Context context, TaskListener taskListener )
	{
		this.taskListener = taskListener;
		this.context = context;

	}

	@Override
	protected void onPreExecute()
	{
		taskListener.onTaskStarted( 0, null );
	}

	@Override
	protected ArrayList<Question> doInBackground( Integer... arguments )
	{

        amount = arguments[0];

        return  AppContext.matrixFactory.getQuestionMatrix().getQuestionsDifficulty(Question.DIFFICULTY_LEVEL_11, amount);
    }

    @Override
    protected void onPostExecute(ArrayList<Question> result) {

        String errorMessage = context.getString(R.string.not_enough_questions_long);

        if (result.size() != amount) {
            errorMessage = errorMessage.replace("***", String.valueOf(Question.DIFFICULTY_LEVEL_11));
            taskListener.onTaskError(0, errorMessage);

        } else {
            taskListener.onTaskCompleted(0, result);
        }
    }

}
