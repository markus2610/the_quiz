package com.thilek.android.qleneagles_quiz.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.QuestionListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;
import com.thilek.android.qleneagles_quiz.tasks.*;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;


public class SetDetailActivity extends ListActivity implements TaskListener {

    private static final int GET_SET = 0;
    private static final int GET_QUESTIONS = 1;
    private static final int ADD_NEW_SET = 2;
    private static final int SAVE_SET = 3;
    private static final int DELETE_QUESTION = 4;


    private TextView emptyText, setNameText, setTypeText;
    private EditText setNameEditText, setTypeEditText;
    private ViewSwitcher setNameSwitcher, setTypeSwitcher;
    private QuestionListAdapter questionListAdapter;
    private QuestionSet questionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_detail_layout);

        emptyText = (TextView) findViewById(R.id.empty_list_text);
        setNameEditText = (EditText) findViewById(R.id.set_name_edittext);
        setTypeEditText = (EditText) findViewById(R.id.set_type_edittext);
        setNameText = (TextView) findViewById(R.id.set_name_text);
        setTypeText = (TextView) findViewById(R.id.set_type_text);

        setNameSwitcher = (ViewSwitcher) findViewById(R.id.set_name_switcher);
        setTypeSwitcher = (ViewSwitcher) findViewById(R.id.set_type_switcher);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstants.SET_ID)) {
            getSetDetailFromDatabase(bundle.getLong(AppConstants.SET_ID));
        } else {
            addNewSetOfQuestions();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (questionSet != null) {
            new GetQuestionsBySetTask(this, GET_QUESTIONS).execute(questionSet._id);
        }
    }

    @Override
    protected void onListItemClick(ListView list, View dataElement, int position, long id) {
        Question question = (Question) list.getAdapter().getItem(position);
        startActivity(new Intent(this, QuestionDetailActivity.class).putExtra(AppConstants.SET_ID, questionSet._id).putExtra(AppConstants.QUESTION_ID, question._id));
    }


    public void onAddQuestionClicked(View view) {
        startActivity(new Intent(this, QuestionDetailActivity.class).putExtra(AppConstants.SET_ID, questionSet._id));
    }


    public void onSetDoneClicked(View view) {
        getSetDetails();
        new UpdateSetTask(this, SAVE_SET).execute(questionSet);
    }

    private void moveQuestionSetDetail() {
        setNameText.setText(setNameEditText.getText().toString());
        setTypeText.setText(setTypeEditText.getText().toString());

    }

    private void loadQuestionSetDetails() {
        setNameText.setText(questionSet.set_name);
        setTypeText.setText(questionSet.set_type);

        setNameEditText.setText(questionSet.set_name);
        setTypeEditText.setText(questionSet.set_type);

    }

    public void onEditSetClicked(View view) {
        ImageView editIcon = (ImageView) findViewById(R.id.edit_set_button);

        if (editIcon.isActivated()) {
            moveQuestionSetDetail();
            editIcon.setActivated(false);
            switchViewSwitcher(false);
        } else {
            editIcon.setActivated(true);
            switchViewSwitcher(true);
        }
    }

    private void switchViewSwitcher(boolean showNext) {
        if (showNext) {
            setNameSwitcher.showNext();
            setTypeSwitcher.showNext();
        } else {
            setNameSwitcher.showPrevious();
            setTypeSwitcher.showPrevious();
        }
    }



    private void getSetDetails() {
        questionSet.set_name = setNameEditText.getText().toString();
        questionSet.set_type = setTypeEditText.getText().toString();
    }

    private void addNewSetOfQuestions() {
        QuestionSet newQuestionSet = new QuestionSet();
        newQuestionSet.set_name = getString(R.string.default_set);

        new AddSetTask(this, ADD_NEW_SET).execute(newQuestionSet);
    }

    private void getSetDetailFromDatabase(long group_id) {
        new GetSetTask(this, GET_SET).execute(group_id);
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
                    questionListAdapter = new QuestionListAdapter(SetDetailActivity.this, questions);
                    setListAdapter(questionListAdapter);
                }


            }
            break;

            case GET_SET: {
                questionSet = (QuestionSet) object;

                if (questionSet != null) {
                    new GetQuestionsBySetTask(this, GET_QUESTIONS).execute(questionSet._id);
                    loadQuestionSetDetails();
                }

            }
            break;
            case ADD_NEW_SET: {
                questionSet = (QuestionSet) object;
            }
            break;
            case SAVE_SET: {

                boolean isSaved = (Boolean) object;

                if (isSaved) {
                    finish();
                } else {
                    Toasts.customShortToast(SetDetailActivity.this, R.string.general_failed_update);
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
}
