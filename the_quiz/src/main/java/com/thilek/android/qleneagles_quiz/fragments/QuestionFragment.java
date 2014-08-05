package com.thilek.android.qleneagles_quiz.fragments;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
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

    private Button optionOne, optionTwo, optionThree, optionFour, timerButton, dialogTimerButton;

    private Dialog timerDialog;

    private ArrayList<TeamData> teamData = new ArrayList<TeamData>();
    private ArrayList<Question> questions = new ArrayList<Question>();

    private int currentTeam = 0;
    private int currentQuestion = 0;
    private int currentRound = 0;

    private boolean isAnswered = false;

    private CountDownTimer countDownTimer, dialogDownTimer;
    private static Handler uiThreadHandler;


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
        uiThreadHandler = new Handler(Looper.getMainLooper());

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

        timerDialog = new Dialog(getActivity(), R.style.custom_app_dialog);
        timerDialog.setContentView(R.layout.dialog_timer_layout);
        timerDialog.setCanceledOnTouchOutside(false);

        dialogTimerButton = (Button) timerDialog.findViewById(R.id.dialog_timer_button);
        dialogTimerButton.setOnClickListener(this);

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
            RoundFragment roundFragment = RoundFragment.getInstance(currentRound);
            getFragmentManager().beginTransaction().replace(R.id.fragment_frame, roundFragment).commit();
        }

    }


    private void handleTimer(int time, boolean start) {
        if (start) {
            countDownTimer = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(final long millisUntilFinished) {

                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            long seconds = millisUntilFinished / 1000;

                            timerButton.setTextColor(getResources().getColor(R.color.custom_green_button_inactive_start));
                            timerButton.setText(seconds + " s");

                            if (seconds < AppConstants.ALERT_TIMER_DURATION) {
                                timerButton.setTextColor(getResources().getColor(R.color.custom_red_button_inactive_start));
                                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, BEEP_VOLUME);
                                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, BEEP_DURATION);
                            }
                        }
                    });
                }

                @Override
                public void onFinish() {
                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            timerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                            timerButton.setText(getString(R.string.general_done_button));

                            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                            animation.setDuration(500); // duration - half a second
                            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                            timerButton.startAnimation(animation);
                        }
                    });
                }
            }.start();
        } else {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                timerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                timerButton.setText(getString(R.string.general_done_button));
            }
        }
    }

    private void handleDialogTimer(boolean start) {
        if (start) {
            dialogDownTimer = new CountDownTimer(AppConstants.HALF_TIMER_DURATION, 1000) {
                @Override
                public void onTick(final long millisUntilFinished) {

                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            long seconds = millisUntilFinished / 1000;

                            dialogTimerButton.setTextColor(getResources().getColor(R.color.custom_green_button_inactive_start));
                            dialogTimerButton.setText(seconds + " s");

                            if (seconds < AppConstants.ALERT_TIMER_DURATION) {
                                dialogTimerButton.setTextColor(getResources().getColor(R.color.custom_red_button_inactive_start));
                                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, BEEP_VOLUME);
                                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, BEEP_DURATION);
                            }
                        }
                    });
                }

                @Override
                public void onFinish() {
                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            timerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                            timerButton.setText(getString(R.string.general_done_button));

                            dialogTimerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                            dialogTimerButton.setText(getString(R.string.general_done_button));

                            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                            animation.setDuration(500); // duration - half a second
                            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                            dialogTimerButton.startAnimation(animation);
                        }
                    });
                }
            }.start();
        } else {
            if (dialogDownTimer != null) {
                dialogDownTimer.cancel();

                dialogTimerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                timerButton.setTextColor(getResources().getColor(R.color.timer_font_color));
                dialogTimerButton.setText(getString(R.string.general_done_button));
            }
        }
    }


    private void checkAnswer(String option) {
        handleTimer(0, false);
        isAnswered = true;

        if (option.equals(questions.get(currentQuestion).right_answer)) {
            answeredRight(option);
        } else {
            answeredWrong(option, questions.get(currentQuestion).right_answer);
            getFragmentActivity().gameManager.knockedOut(teamData.get(currentTeam).group_id, currentRound);
        }
    }


    private void answeredRight(String option) {
        handleAnswerAnimation(true, option, null);
        Toasts.customLongToast(getActivity(), R.string.right_answer_selected);
    }

    private void answeredWrong(String option, String rightAnswer) {
        handleAnswerAnimation(false, option, rightAnswer);
        Toasts.customLongToast(getActivity(), R.string.wrong_answer_selected);
    }


    private void askAudience() {
        handleTimer(0, false);
        audienceOption.setVisibility(View.INVISIBLE);
        teamData.get(currentTeam).helpOption = false;
        getFragmentActivity().gameManager.useAudienceOption(teamData.get(currentTeam).group_id);
        timerDialog.show();
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

    private int getAnswerButton(String option) {

        if (option.equals("A")) {
            return optionOne.getId();
        } else if (option.equals("B")) {
            return optionTwo.getId();
        } else if (option.equals("C")) {
            return optionThree.getId();
        } else if (option.equals("D")) {
            return optionFour.getId();
        }

        return 0;
    }

    private int getRightAnswerButton(String option) {

        if (option.equals("A")) {
            return optionOne.getId();
        } else if (option.equals("B")) {
            return optionTwo.getId();
        } else if (option.equals("C")) {
            return optionThree.getId();
        } else if (option.equals("D")) {
            return optionFour.getId();
        }

        return 0;
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
                timerButton.clearAnimation();
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

        } else if (v.getId() == dialogTimerButton.getId()) {
            if (dialogTimerButton.getText().equals(getString(R.string.general_done_button))) {
                dialogTimerButton.clearAnimation();
                timerDialog.dismiss();
            } else if (timerButton.getText().equals(getString(R.string.start_button_text))) {
                handleDialogTimer(true);
            } else {
                handleDialogTimer(false);
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

    private void handleAnswerAnimation(final boolean isRightAnswer, final String option, final String rightOption) {
        MediaPlayer mediaPlayer;

        final Button button = (Button) vFragmentView.findViewById(getAnswerButton(option));

        if (isRightAnswer) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.right);
        } else {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.wrong);
        }

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                uiThreadHandler.post(new Runnable() {
                    public void run() {


                        if (isRightAnswer) {
                            button.setBackgroundResource(R.drawable.custom_rounded_green_button);
                        } else {
                            button.setBackgroundResource(R.drawable.custom_rounded_red_button);

                            final Button rightButton = (Button) vFragmentView.findViewById(getRightAnswerButton(rightOption));
                            rightButton.setBackgroundResource(R.drawable.custom_rounded_green_button);
                        }

                        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
                        animation.setDuration(100); // duration - half a second
                        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
                        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
                        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
                        button.startAnimation(animation);

                    }
                });

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                uiThreadHandler.post(new Runnable() {
                    public void run() {

                        if (!isRightAnswer) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                            final Button rightButton = (Button) vFragmentView.findViewById(getRightAnswerButton(rightOption));
                            rightButton.setBackgroundResource(R.drawable.custom_rounded_button);
                        }


                        button.clearAnimation();
                        button.setBackgroundResource(R.drawable.custom_rounded_button);

                        nextTeam();
                    }
                });
            }

        });
        mediaPlayer.start();
    }


    @Override
    public boolean onFragmentBackPressed() {
        return false;
    }
}
