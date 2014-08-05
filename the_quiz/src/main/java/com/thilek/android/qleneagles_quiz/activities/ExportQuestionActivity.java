package com.thilek.android.qleneagles_quiz.activities;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.QuestionListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.tasks.DeleteQuestionTask;
import com.thilek.android.qleneagles_quiz.tasks.ExportQuestionsTask;
import com.thilek.android.qleneagles_quiz.tasks.GetAllQuestionsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.views.FileSelectorDialog;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.io.File;
import java.util.ArrayList;


public class ExportQuestionActivity extends ListActivity implements TaskListener, AdapterView.OnItemLongClickListener {

    private static final int GET_QUESTIONS = 1;
    private static final int EXPORT_QUESTIONS = 2;
    private static final int DELETE_QUESTION = 3;


    private TextView  removeQuestionMessage;
    private QuestionListAdapter questionListAdapter;
    private FileSelectorDialog fileDialog;
    private Dialog removeQuestionDialog;


    private static Handler uiThreadHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export_questions_layout);


        uiThreadHandler = new Handler(Looper.getMainLooper());

        TextView emptyText = (TextView) findViewById(R.id.empty_list_text);

        getListView().setEmptyView(emptyText);
        getListView().setOnItemLongClickListener(this);

        removeQuestionDialog = new Dialog(this, R.style.custom_app_dialog);
        removeQuestionDialog.setContentView(R.layout.dialog_question_group);
        removeQuestionDialog.setCanceledOnTouchOutside(false);
        removeQuestionMessage = (TextView) removeQuestionDialog.findViewById(R.id.remove_question_message);

        Button deleteButton = (Button) removeQuestionDialog.findViewById(R.id.remove_question_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = (Question) removeQuestionMessage.getTag();
                removeQuestionDialog.dismiss();

                new DeleteQuestionTask(ExportQuestionActivity.this, DELETE_QUESTION).execute(question);
            }
        });

        Button cancelDeleteButton = (Button) removeQuestionDialog.findViewById(R.id.cancel_question_button);
        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeQuestionDialog.dismiss();
            }
        });

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

                new ExportQuestionsTask(ExportQuestionActivity.this, EXPORT_QUESTIONS).execute(file);

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
                final ArrayList<Question> questions = (ArrayList<Question>) object;

                uiThreadHandler.post(new Runnable() {
                    public void run() {
                        if (questions.size() != 0) {
                            questionListAdapter = new QuestionListAdapter(ExportQuestionActivity.this, questions);
                            setListAdapter(questionListAdapter);
                        }
                    }
                });
            }
            break;

            case EXPORT_QUESTIONS: {
                boolean exported = (Boolean) object;

                if (exported) {
                    Toasts.customShortToast(this, R.string.export_successful);
                    new GetAllQuestionsTask(this, GET_QUESTIONS).execute();
                } else {
                    Toasts.customShortToast(this, R.string.fail_export);
                }

            }
            break;

            case DELETE_QUESTION: {
                final boolean exported = (Boolean) object;

                if (exported) {
                    Toasts.customShortToast(this, R.string.delete_question_successful);
                    new GetAllQuestionsTask(this, GET_QUESTIONS).execute();
                } else {
                    Toasts.customShortToast(this, R.string.delete_question_fail);
                }

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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Question question = (Question) parent.getAdapter().getItem(position);

        removeQuestionMessage.setTag(question);
        removeQuestionDialog.show();

        return false;
    }

}
