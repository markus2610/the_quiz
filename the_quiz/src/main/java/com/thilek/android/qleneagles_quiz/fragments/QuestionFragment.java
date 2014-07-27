package com.thilek.android.qleneagles_quiz.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.game_manager.model.TeamData;
import com.thilek.android.qleneagles_quiz.tasks.SetUsedQuestionTask;
import com.thilek.android.qleneagles_quiz.util.ViewUtilities;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class QuestionFragment extends GameFragment implements View.OnClickListener {

    public static final String ROUND = "ROUND";


    private static final int BEEP_VOLUME = 100;
    private static final int BEEP_DURATION = 200;

    private View vFragmentView;

    private ImageView filterOption, audienceOption, presenterIcon;
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

        presenterIcon = (ImageView) vFragmentView.findViewById(R.id.result_question_icon);

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

        questions.addAll(getFragmentActivity().gameManager.getQuestions(currentRound));
        teamData.addAll(getFragmentActivity().gameManager.getActiveTeams());

        loadTeamStatus(teamData.get(currentTeam));
        loadQuestionStatus(questions.get(currentQuestion));

    }


    private void loadTeamStatus(TeamData teamData) {
        if (teamData.helpOption) {
            audienceOption.setVisibility(View.VISIBLE);
        } else {
            audienceOption.setVisibility(View.INVISIBLE);
        }

        if (teamData.filterOption) {
            filterOption.setVisibility(View.VISIBLE);
        } else {
            filterOption.setVisibility(View.INVISIBLE);
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
        resetOptions();

        if (currentTeam < (teamData.size() - 1)) {
            timerButton.setText(getString(R.string.start_button_text));

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
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, BEEP_VOLUME);
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, BEEP_DURATION);
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
            answeredRight();
        } else {
            answeredWrong();
            getFragmentActivity().gameManager.knockedOut(teamData.get(currentTeam).group_id, currentRound);
        }
    }


    private void answeredRight() {
        loadAndAnimatePresenterIcon(true);
        Toasts.customLongToast(getActivity(), R.string.right_answer_selected);
    }

    private void answeredWrong() {
        loadAndAnimatePresenterIcon(false);
        Toasts.customLongToast(getActivity(), R.string.wrong_answer_selected);
    }


    private void askAudience() {
        handleTimer(0, false);
        audienceOption.setVisibility(View.INVISIBLE);
        teamData.get(currentTeam).helpOption = false;
        getFragmentActivity().gameManager.useAudienceOption(teamData.get(currentTeam).group_id);
    }

    private void resetOptions() {
        optionOne.setVisibility(View.VISIBLE);
        optionTwo.setVisibility(View.VISIBLE);
        optionThree.setVisibility(View.VISIBLE);
        optionFour.setVisibility(View.VISIBLE);
    }


    private void filterAnswerClicked() {
        handleTimer(0, false);
        filterOption.setVisibility(View.INVISIBLE);
        teamData.get(currentTeam).filterOption = false;
        getFragmentActivity().gameManager.useFilterOption(teamData.get(currentTeam).group_id);

        Question question = questions.get(currentQuestion);

        if (question.answer_option.equals("A") || question.right_answer.equals("A")) {
            optionOne.setVisibility(View.VISIBLE);
        } else {
            optionOne.setVisibility(View.INVISIBLE);
        }

        if (question.answer_option.equals("B") || question.right_answer.equals("B")) {
            optionTwo.setVisibility(View.VISIBLE);
        } else {
            optionTwo.setVisibility(View.INVISIBLE);
        }

        if (question.answer_option.equals("C") || question.right_answer.equals("C")) {
            optionThree.setVisibility(View.VISIBLE);
        } else {
            optionThree.setVisibility(View.INVISIBLE);
        }

        if (question.answer_option.equals("D") || question.right_answer.equals("D")) {
            optionFour.setVisibility(View.VISIBLE);
        } else {
            optionFour.setVisibility(View.INVISIBLE);
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


    private void loadAndAnimatePresenterIcon(final boolean isRightAnswer) {

        presenterIcon.setVisibility(View.VISIBLE);
        presenterIcon.setActivated(isRightAnswer);


        Animation translateAnimation = new TranslateAnimation(ViewUtilities.intToPixelFloat(300), 0.0f, 0.0f, 0.0f);
        translateAnimation.setDuration(2000);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                presenterIcon.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                presenterIcon.setLayoutParams(params);

                MediaPlayer mp;

                if (isRightAnswer) {
                    mp = MediaPlayer.create(getActivity(), R.raw.right);
                } else {
                    mp = MediaPlayer.create(getActivity(), R.raw.wrong);
                }


                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();

                        presenterIcon.setVisibility(View.GONE);
                        nextTeam();
                    }

                });
                mp.start();


            }
        });

        presenterIcon.setAnimation(translateAnimation);


    }


    @Override
    public boolean onFragmentBackPressed() {
        return false;
    }
}
