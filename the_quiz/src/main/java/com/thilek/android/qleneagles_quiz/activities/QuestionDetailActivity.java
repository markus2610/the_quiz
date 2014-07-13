package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.tasks.AddQuestionTask;
import com.thilek.android.qleneagles_quiz.tasks.GetQuestionTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.tasks.UpdateQuestionTask;
import com.thilek.android.qleneagles_quiz.views.Toasts;


public class QuestionDetailActivity extends Activity implements TaskListener {

    private static final int GET_QUESTION = 1;
    private static final int ADD_QUESTION = 2;
    private static final int SAVE_QUESTION = 3;

    private TextView questionText, optionOneText, optionTwoText, optionThreeText, optionFourText, answerText, difficultyText;
    private EditText questionEditText, optionOneEditText, optionTwoEditText, optionThreeEditText, optionFourEditText;
    private ViewSwitcher questionSwitch, optionOneSwitch, optionTwoSwitch, optionThreeSwitch, optionFourSwitch, answerSwitch, difficultySwitch;
    private Spinner answerSpinner, difficultySpinner;
    private Question question;
    private ImageView editIcon;
    private long setID;

    private ArrayAdapter<String> answerAdapter, difficultyAdapter;

    private String[] answerOption = {"A", "B", "C", "D"};
    private String[] difficultyOption = {String.valueOf(Question.DIFFICULTY_LEVEL_1), String.valueOf(Question.DIFFICULTY_LEVEL_2), String.valueOf(Question.DIFFICULTY_LEVEL_3), String.valueOf(Question.DIFFICULTY_LEVEL_4),
            String.valueOf(Question.DIFFICULTY_LEVEL_5), String.valueOf(Question.DIFFICULTY_LEVEL_6), String.valueOf(Question.DIFFICULTY_LEVEL_7),
            String.valueOf(Question.DIFFICULTY_LEVEL_8), String.valueOf(Question.DIFFICULTY_LEVEL_9), String.valueOf(Question.DIFFICULTY_LEVEL_10),
            String.valueOf(Question.DIFFICULTY_LEVEL_11)};

    Animation slide_in_left, slide_out_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_question_detail_layout);

        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        questionText = (TextView) findViewById(R.id.question_text);
        optionOneText = (TextView) findViewById(R.id.option_one_text);
        optionTwoText = (TextView) findViewById(R.id.option_two_text);
        optionThreeText = (TextView) findViewById(R.id.option_three_text);
        optionFourText = (TextView) findViewById(R.id.option_four_text);
        answerText = (TextView) findViewById(R.id.right_answer_text);
        difficultyText = (TextView) findViewById(R.id.difficulty_text);

        editIcon = (ImageView) findViewById(R.id.editButton);


        questionEditText = (EditText) findViewById(R.id.question_edittext);
        optionOneEditText = (EditText) findViewById(R.id.option_one_edittext);
        optionTwoEditText = (EditText) findViewById(R.id.option_two_edittext);
        optionThreeEditText = (EditText) findViewById(R.id.option_three_edittext);
        optionFourEditText = (EditText) findViewById(R.id.option_four_edittext);

        questionSwitch = (ViewSwitcher) findViewById(R.id.question_switcher);
        questionSwitch.setInAnimation(slide_in_left);
        questionSwitch.setOutAnimation(slide_out_right);

        optionOneSwitch = (ViewSwitcher) findViewById(R.id.option_one_switcher);
        optionOneSwitch.setInAnimation(slide_in_left);
        optionOneSwitch.setOutAnimation(slide_out_right);

        optionTwoSwitch = (ViewSwitcher) findViewById(R.id.option_two_switcher);
        optionTwoSwitch.setInAnimation(slide_in_left);
        optionTwoSwitch.setOutAnimation(slide_out_right);

        optionThreeSwitch = (ViewSwitcher) findViewById(R.id.option_three_switcher);
        optionThreeSwitch.setInAnimation(slide_in_left);
        optionThreeSwitch.setOutAnimation(slide_out_right);

        optionFourSwitch = (ViewSwitcher) findViewById(R.id.option_four_switcher);
        optionFourSwitch.setInAnimation(slide_in_left);
        optionFourSwitch.setOutAnimation(slide_out_right);

        answerSwitch = (ViewSwitcher) findViewById(R.id.answer_switcher);
        answerSwitch.setInAnimation(slide_in_left);
        answerSwitch.setOutAnimation(slide_out_right);

        difficultySwitch = (ViewSwitcher) findViewById(R.id.difficulty_switcher);
        difficultySwitch.setInAnimation(slide_in_left);
        difficultySwitch.setOutAnimation(slide_out_right);

        answerSpinner = (Spinner) findViewById(R.id.answer_spinner);
        answerAdapter = new ArrayAdapter<String>(this, R.layout.custom_single_list_item, answerOption);
        answerSpinner.setAdapter(answerAdapter);

        difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        difficultyAdapter = new ArrayAdapter<String>(this, R.layout.custom_single_list_item, difficultyOption);
        difficultySpinner.setAdapter(difficultyAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstants.SET_ID)) {

            setID = bundle.getLong(AppConstants.SET_ID);

            if (bundle.containsKey(AppConstants.QUESTION_ID)) {
                getQuestionDetailFromDatabase(bundle.getLong(AppConstants.QUESTION_ID));
            } else {
                addNewQuestion();
            }
        } else {
            finish();
        }

    }


    private void moveQuestionDetail() {
        questionText.setText(questionEditText.getText().toString());
        optionOneText.setText(optionOneEditText.getText().toString());
        optionTwoText.setText(optionTwoEditText.getText().toString());
        optionThreeText.setText(optionThreeEditText.getText().toString());
        optionFourText.setText(optionFourEditText.getText().toString());
        answerText.setText(String.valueOf(answerSpinner.getSelectedItem()));
        difficultyText.setText(String.valueOf(difficultySpinner.getSelectedItem()));
    }

    private void loadQuestionDetails() {
        questionText.setText(question.question);
        optionOneText.setText(question.option_one);
        optionTwoText.setText(question.option_two);
        optionThreeText.setText(question.option_three);
        optionFourText.setText(question.option_four);
        answerText.setText(question.right_answer);
        difficultyText.setText(String.valueOf(question.difficulty));

        questionEditText.setText(question.question);
        optionOneEditText.setText(question.option_one);
        optionTwoEditText.setText(question.option_two);
        optionThreeEditText.setText(question.option_three);
        optionFourEditText.setText(question.option_four);

        answerSpinner.setSelection(answerAdapter.getPosition(question.right_answer), false);
        difficultySpinner.setSelection(difficultyAdapter.getPosition(String.valueOf(question.difficulty)), false);

    }

    private void getQuestionDetails() {
        if (question == null) {
            question = new Question();
            question.set_id = setID;
            question.question = getString(R.string.default_question);
        }

        question.question = questionEditText.getText().toString();
        question.option_one = optionOneEditText.getText().toString();
        question.option_two = optionTwoEditText.getText().toString();
        question.option_three = optionThreeEditText.getText().toString();
        question.option_four = optionFourEditText.getText().toString();
        question.right_answer = String.valueOf(answerSpinner.getSelectedItem());
        question.difficulty = Integer.valueOf(String.valueOf(difficultySpinner.getSelectedItem()));
    }

    public void onQuestionDoneClicked(View view) {
        getQuestionDetails();
        new UpdateQuestionTask(this, SAVE_QUESTION).execute(question);
    }

    public void onEditQuestionClicked(View view) {
        switchViewSwitcher(editIcon.isActivated());
    }


    private void addNewQuestion() {
        question = new Question();
        question.set_id = setID;
        question.question = getString(R.string.default_question);

        new AddQuestionTask(this, ADD_QUESTION).execute(question);

        switchViewSwitcher(true);
    }

    private void getQuestionDetailFromDatabase(long group_id) {
        new GetQuestionTask(this, GET_QUESTION).execute(group_id);
    }

    private void switchViewSwitcher(boolean showNext) {

        if (!showNext) {
            moveQuestionDetail();
            editIcon.setActivated(false);

            optionOneSwitch.showPrevious();
            questionSwitch.showPrevious();
            optionTwoSwitch.showPrevious();
            optionThreeSwitch.showPrevious();
            optionFourSwitch.showPrevious();
            answerSwitch.showPrevious();
            difficultySwitch.showPrevious();
        } else {
            editIcon.setActivated(true);

            optionOneSwitch.showNext();
            questionSwitch.showNext();
            optionTwoSwitch.showNext();
            optionThreeSwitch.showNext();
            optionFourSwitch.showNext();
            answerSwitch.showNext();
            difficultySwitch.showNext();
        }

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
            case GET_QUESTION: {
                question = (Question) object;
                loadQuestionDetails();
            }
            break;

            case ADD_QUESTION: {
                question = (Question) object;
            }
            break;

            case SAVE_QUESTION: {
                boolean isSaved = (Boolean) object;
                if (isSaved) {
                    finish();
                } else {
                    Toasts.customShortToast(QuestionDetailActivity.this, R.string.general_failed_update);
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
