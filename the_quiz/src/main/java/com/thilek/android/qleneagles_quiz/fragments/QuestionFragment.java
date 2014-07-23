package com.thilek.android.qleneagles_quiz.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.game_manager.model.TeamData;
import com.thilek.android.qleneagles_quiz.tasks.SetUsedQuestionTask;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class QuestionFragment extends GameFragment implements View.OnClickListener {

    public static final String ROUND = "ROUND";

    private View vFragmentView;

    private ImageView filterOption, audienceOption;
    private TextView teamName, questionText;

    private Button optionOne, optionTwo, optionThree, optionFour, timerButton;

    private ArrayList<TeamData> teamData = new ArrayList<TeamData>();
    private ArrayList<Question> questions = new ArrayList<Question>();

    private int currentTeam = 0;
    private int currentQuestion = 0;
    private int currentRound = 0;

    private boolean isAnswered = false;

    private CountDownTimer countDownTimer;

    public static QuestionFragment getInstance() {
        QuestionFragment questionFragment = new QuestionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ROUND, 0);

        questionFragment.setArguments(bundle);
        return questionFragment;
    }

    public static QuestionFragment getInstance(int round) {
        QuestionFragment questionFragment = new QuestionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ROUND, round);

        questionFragment.setArguments(bundle);
        return questionFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        vFragmentView = inflater.inflate(R.layout.fragment_question_fragment, container, false);

        filterOption = (ImageView) vFragmentView.findViewById(R.id.half_button);
        filterOption.setOnClickListener(this);
        audienceOption = (ImageView) vFragmentView.findViewById(R.id.audience_button);
        audienceOption.setOnClickListener(this);

        teamName = (TextView) vFragmentView.findViewById(R.id.team_name_text);
        questionText = (TextView) vFragmentView.findViewById(R.id.question_text);

        optionOne = (Button) vFragmentView.findViewById(R.id.option_one_button);
        optionOne.setOnClickListener(this);
        optionTwo = (Button) vFragmentView.findViewById(R.id.option_two_button);
        optionTwo.setOnClickListener(this);
        optionThree = (Button) vFragmentView.findViewById(R.id.option_three_button);
        optionThree.setOnClickListener(this);
        optionFour = (Button) vFragmentView.findViewById(R.id.option_four_button);
        optionFour.setOnClickListener(this);

        timerButton = (Button) vFragmentView.findViewById(R.id.timer_button);
        timerButton.setOnClickListener(this);

        initializeRound();

        return vFragmentView;
    }

    private void initializeRound() {

        currentRound = getArguments().getInt(ROUND);

        if (currentQuestion == 0) {
            NewGameSettingFragment newGameSettingFragment = NewGameSettingFragment.getInstance();
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame, newGameSettingFragment).commit();
            Toasts.customShortToast(getActivity(), R.string.problem_loading_question);
        } else {
            questions = getFragmentActivity().gameManager.getQuestions(currentRound);
            teamData = getFragmentActivity().gameManager.getActiveTeams();

            loadTeamStatus(teamData.get(currentTeam));
            loadQuestionStatus(questions.get(currentQuestion));
        }

    }


    private void loadTeamStatus(TeamData teamData) {
        if (teamData.helpOption) {
            audienceOption.setVisibility(View.VISIBLE);
        } else {
            audienceOption.setVisibility(View.GONE);
        }

        if (teamData.filterOption) {
            filterOption.setVisibility(View.VISIBLE);
        } else {
            filterOption.setVisibility(View.GONE);
        }

        teamName.setText(teamData.group.group_name);
    }

    private void loadQuestionStatus(Question question) {
        questionText.setText(question.question);

        optionOne.setText(question.option_one);
        optionTwo.setText(question.option_two);
        optionThree.setText(question.option_three);
        optionFour.setText(question.option_four);

    }

    private void nextTeam() {
        new SetUsedQuestionTask().execute(questions.get(currentQuestion));

        if (currentTeam < teamData.size()) {
            currentTeam = currentTeam + 1;
            loadTeamStatus(teamData.get(currentTeam));

            currentQuestion = currentQuestion + 1;
            loadQuestionStatus(questions.get(currentQuestion));
        } else {
            Toasts.customShortToast(getActivity(), R.string.round_completed);

            RoundFragment roundFragment = RoundFragment.getInstance(currentRound);
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame, roundFragment).commit();
        }

    }


    private void handleTimer(int time, boolean start) {
        if (start) {
            countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timerButton.setText((millisUntilFinished / 1000) + " s");
                }

                @Override
                public void onFinish() {
                    timerButton.setText(getString(R.string.general_done_button));
                }
            }.start();
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                timerButton.setText(getString(R.string.general_done_button));
            }
        }
    }


    private void checkAnswer(String option) {
        handleTimer(0, false);
        isAnswered = true;

        if (option.equals(questions.get(currentQuestion).right_answer)) {
            nextTeam();
        } else {
            getFragmentActivity().gameManager.knockedOut(teamData.get(currentTeam).group_id, currentRound);
        }
    }


    private void askAudience() {
        handleTimer(0, false);
        audienceOption.setVisibility(View.GONE);
        teamData.get(currentTeam).helpOption = false;
        getFragmentActivity().gameManager.useAudienceOption(teamData.get(currentTeam).group_id);
    }


    private void filterAnswerClicked() {
        handleTimer(0, false);
        filterOption.setVisibility(View.GONE);
        teamData.get(currentTeam).filterOption = false;
        getFragmentActivity().gameManager.useFilterOption(teamData.get(currentTeam).group_id);

        Question question =  questions.get(currentQuestion);

        if(question.answer_option.equals("A") ||question.right_answer.equals("A") ){
            optionOne.setVisibility(View.VISIBLE);
        }else{
            optionOne.setVisibility(View.GONE);
        }

        if(question.answer_option.equals("B") ||question.right_answer.equals("B") ){
            optionTwo.setVisibility(View.VISIBLE);
        }else{
            optionTwo.setVisibility(View.GONE);
        }

        if(question.answer_option.equals("C") ||question.right_answer.equals("C") ){
            optionThree.setVisibility(View.VISIBLE);
        }else{
            optionThree.setVisibility(View.GONE);
        }

        if(question.answer_option.equals("D") ||question.right_answer.equals("D") ){
            optionFour.setVisibility(View.VISIBLE);
        }else{
            optionFour.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == audienceOption.getId()) {
            askAudience();
        } else if (v.getId() == filterOption.getId()) {
            filterAnswerClicked();
        } else if (v.getId() == optionOne.getId()) {
            checkAnswer("A");
        } else if (v.getId() == optionTwo.getId()) {
            checkAnswer("B");
        } else if (v.getId() == optionThree.getId()) {
            checkAnswer("C");
        } else if (v.getId() == optionFour.getId()) {
            checkAnswer("D");
        } else if (v.getId() == timerButton.getId()) {
            if (timerButton.getText().equals(getString(R.string.general_done_button))) {
                if (isAnswered) {
                    nextTeam();
                } else {
                    Toasts.customShortToast(getActivity(), R.string.choose_answer_first);
                }
            } else if (timerButton.getText().equals(getString(R.string.start_button_text))) {
                handleTimer(AppConstants.FULL_TIMER_DURATION, true);
            } else {
                handleTimer(0, false);
            }
        }
    }

    @Override
    public boolean onFragmentBackPressed() {
        return false;
    }
}
