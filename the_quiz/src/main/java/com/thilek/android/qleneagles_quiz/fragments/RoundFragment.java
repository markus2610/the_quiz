package com.thilek.android.qleneagles_quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.game_manager.model.TeamData;
import com.thilek.android.qleneagles_quiz.tasks.LoadQuestionsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class RoundFragment extends GameFragment implements View.OnClickListener, TaskListener {

    public static final String ROUND = "ROUND";

    public static final int LOAD_QUESTIONS = 0;

    private View vFragmentView;

    private TextView roundNumberText, roundStatusMessage;

    private RelativeLayout startButton;

    private int currentRound = 0;

    public static RoundFragment getInstance() {
        RoundFragment roundsFragment = new RoundFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ROUND, 0);

        roundsFragment.setArguments(bundle);
        return roundsFragment;
    }

    public static RoundFragment getInstance(int round) {
        RoundFragment roundsFragment = new RoundFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(ROUND, round);

        roundsFragment.setArguments(bundle);
        return roundsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        vFragmentView = inflater.inflate(R.layout.fragment_round, container, false);

        startButton = (RelativeLayout) vFragmentView.findViewById(R.id.round_start_button);
        startButton.setOnClickListener(this);

        roundNumberText = (TextView) vFragmentView.findViewById(R.id.round_text);
        roundStatusMessage = (TextView) vFragmentView.findViewById(R.id.loading_message);

        checkRoundStatus();

        return vFragmentView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == startButton.getId()) {
            if (roundNumberText.getText().equals(getString(R.string.has_winner)) ||roundNumberText.getText().equals(getString(R.string.game_over)) || roundNumberText.getText().equals(getString(R.string.not_enough_questions_short))) {
                getFragmentActivity().finish();
            } else if (roundNumberText.getText().equals(getString(R.string.general_please_wait_string))) {
                Toasts.customShortToast(getActivity(), R.string.general_please_wait_string);
            } else {
                startRound();
            }
        }
    }

    private void checkRoundStatus() {
        currentRound = getArguments().getInt(ROUND);

        if (currentRound != 0) {
            initializeRound();
        } else {
            int teams = getFragmentActivity().gameManager.getActiveTeams().size();

            new LoadQuestionsTask(getActivity(), this, LOAD_QUESTIONS).execute(teams);
        }
    }


    private void initializeRound() {

        if (getFragmentActivity().gameManager.getActiveTeams().size() > 0) {

            if (currentRound != AppConstants.MAX_ROUNDS) {
                currentRound = currentRound + 1;

                String roundNumber = getString(R.string.round_text);
                roundNumber = roundNumber.replace("***", String.valueOf(currentRound));
                roundNumberText.setText(roundNumber);

                roundStatusMessage.setText(getString(R.string.click_to_start));

            } else if (getFragmentActivity().gameManager.getActiveTeams().size() == 1) {

                TeamData winnerTeam =  getFragmentActivity().gameManager.getActiveTeams().get(0);

                String message = getString(R.string.winner_team);
                message = message.replace("***", String.valueOf(winnerTeam.group.group_name));

                roundNumberText.setText(getString(R.string.has_winner));
                roundStatusMessage.setText(message);

                Toasts.customShortToast(getActivity(), message);


            } else {
                Toasts.customShortToast(getActivity(), R.string.special_round);

                SpecialRoundFragment specialRoundFragment = SpecialRoundFragment.getInstance();
                getFragmentManager().beginTransaction().replace(R.id.fragment_frame, specialRoundFragment).commit();
            }
        } else {
            roundNumberText.setText(getString(R.string.game_over));
            roundStatusMessage.setText(getString(R.string.game_over_status));
        }

    }


    private void startRound() {
        QuestionFragment questionFragment = QuestionFragment.getInstance(currentRound);
        getFragmentManager().beginTransaction().replace(R.id.fragment_frame, questionFragment).commit();
    }

    @Override
    public void onTaskStarted(int taskID, Object object) {
        roundNumberText.setText(getString(R.string.general_please_wait_string));
        roundStatusMessage.setText(getString(R.string.loading_questions));
    }

    @Override
    public void onTaskProgressUpdated(int taskID, Object object) {

    }

    @Override
    public void onTaskCompleted(int taskID, Object object) {

        Map<Integer, ArrayList<Question>> questions = (Map<Integer, ArrayList<Question>>) object;
        getFragmentActivity().gameManager.gameData.questions.clear();
        getFragmentActivity().gameManager.gameData.questions.putAll(questions);

        initializeRound();
    }

    @Override
    public void onTaskCanceled(int taskID, Object object) {

    }

    @Override
    public void onTaskError(int taskID, Object object) {

        String message = (String) object;
        roundStatusMessage.setText(message);
        roundNumberText.setText(getString(R.string.not_enough_questions_short));
    }
}
