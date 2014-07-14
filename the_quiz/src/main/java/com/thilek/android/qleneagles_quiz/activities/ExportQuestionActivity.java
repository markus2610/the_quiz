package com.thilek.android.qleneagles_quiz.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.QuestionListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.tasks.GetAllQuestionsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.views.FileSelectorDialog;

import java.io.File;
import java.util.ArrayList;


public class ExportQuestionActivity extends ListActivity implements TaskListener {

    private static final int GET_QUESTIONS = 1;
    private static final int EXPORT_QUESTIONS = 2;
    private static final int DELETE_QUESTION = 3;


    private TextView emptyText;
    private QuestionListAdapter questionListAdapter;

    private FileSelectorDialog fileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export_questions_layout);

        emptyText = (TextView) findViewById(R.id.empty_list_text);

        getListView().setEmptyView(emptyText);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new GetAllQuestionsTask(this, GET_QUESTIONS).execute();

    }

    @Override
    protected void onListItemClick(ListView list, View dataElement, int position, long id) {
        Question question = (Question) list.getAdapter().getItem(position);
        startActivity(new Intent(this, QuestionDetailActivity.class).putExtra(AppConstants.QUESTION_ID, question._id));
    }


    public void onAddQuestionClicked(View view) {
        startActivity(new Intent(this, QuestionDetailActivity.class));
    }


    public void onExportClicked(View view) {
        File mPath = Environment.getExternalStorageDirectory();

        fileDialog = new FileSelectorDialog(this, mPath);
        fileDialog.addFileListener(new FileSelectorDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
        //fileDialog.addDirectoryListener(new FileDialog.DirectorySelectedListener() {
        //  public void directorySelected(File directory) {
        //      Log.d(getClass().getName(), "selected dir " + directory.toString());
        //  }
        //});
        //fileDialog.setSelectDirectoryOption(false);
        fileDialog.showDialog();
    }

    @Override
    public void onTaskStarted(int taskID, Object object) {

    }

    @Override
    public void onTaskProgressUpdated(int taskID, Object object) {

    }

    @Override
    public void onTaskCompleted(int taskID, Object object) {

        switch (taskID) {
            case GET_QUESTIONS: {
                ArrayList<Question> questions = (ArrayList<Question>) object;

                if (questions.size() == 0) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    questionListAdapter = new QuestionListAdapter(ExportQuestionActivity.this, questions);
                    setListAdapter(questionListAdapter);
                }


            }
            break;

            case EXPORT_QUESTIONS: {


            }
            break;
        }

    }

    @Override
    public void onTaskCanceled(int taskID, Object object) {

    }

    @Override
    public void onTaskError(int taskID, Object object) {

    }
}
