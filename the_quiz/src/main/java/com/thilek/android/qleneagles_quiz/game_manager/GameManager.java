package com.thilek.android.qleneagles_quiz.game_manager;

import android.app.Activity;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.game_manager.model.GameData;
import com.thilek.android.qleneagles_quiz.game_manager.model.TeamData;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameManager {

    public Activity activity;

    public GameData gameData;

    public GameManager(Activity activity) {
        this.activity = activity;
    }

    public void createGame() {
        gameData = new GameData();
    }

    public void addTeam(Group group) {

        if (gameData != null) {
            TeamData newTeam = new TeamData(group);

            if (gameData.teams.containsKey(newTeam.group_id)) {
                Toasts.customShortToast(activity, R.string.team_already_added);
            } else {
                gameData.teams.put(newTeam.group_id, newTeam);
            }
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
    }

    public void removeTeam(Group group) {
        if (gameData != null) {
            if (gameData.teams.containsKey(group._id)) {
                gameData.teams.remove(group._id);
                Toasts.customShortToast(activity, R.string.team_removed);
            } else {
                Toasts.customShortToast(activity, R.string.team_not_found);
            }
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
    }


    public void fullPoint(long group_id) {
        if (gameData != null) {
            if (gameData.teams.containsKey(group_id)) {
                gameData.teams.get(group_id).score += AppConstants.FULL_POINT;
            } else {
                Toasts.customShortToast(activity, R.string.team_not_found);
            }
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
    }

    public void subPoint(long group_id) {
        if (gameData != null) {
            if (gameData.teams.containsKey(group_id)) {
                gameData.teams.get(group_id).score += AppConstants.SUB_POINT;
            } else {
                Toasts.customShortToast(activity, R.string.team_not_found);
            }
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
    }

    public void knockedOut(long group_id, int round) {
        if (gameData != null) {
            if (gameData.teams.containsKey(group_id)) {
                gameData.teams.get(group_id).knockedOutRound = round;
                gameData.teams.get(group_id).isActive = false;
            } else {
                Toasts.customShortToast(activity, R.string.team_not_found);
            }
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
    }

    public ArrayList<TeamData> getAllTeams() {

        ArrayList<TeamData> teamData = new ArrayList<TeamData>();

        if (gameData != null) {

            for (Map.Entry<Long, TeamData> entry : gameData.teams.entrySet()) {
                teamData.add(entry.getValue());
            }

        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
        return teamData;

    }


    public ArrayList<Group> getAllGroups() {

        ArrayList<Group> teamData = new ArrayList<Group>();

        if (gameData != null) {

            for (Map.Entry<Long, TeamData> entry : gameData.teams.entrySet()) {
                teamData.add(entry.getValue().group);
            }

        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
        return teamData;

    }

    public ArrayList<TeamData> getActiveTeams() {

        ArrayList<TeamData> teamData = new ArrayList<TeamData>();

        if (gameData != null) {

            for (Map.Entry<Long, TeamData> entry : gameData.teams.entrySet()) {

                if (entry.getValue().isActive)
                    teamData.add(entry.getValue());
            }

        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
        return teamData;

    }

    public ArrayList<Question> getQuestions(int round) {
        ArrayList<Question> questions = new ArrayList<Question>();

        if (gameData != null) {

            if (gameData.questions.containsKey(round))
                questions.addAll(gameData.questions.get(round));

        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
        return questions;
    }


    public void addQuestions(int round, ArrayList<Question> questions) {

        if (gameData != null) {

            if (gameData.questions.containsKey(round)) {
                gameData.questions.get(round).clear();
                gameData.questions.get(round).addAll(questions);
            } else {
                gameData.questions.put(round, questions);
            }

        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }

    }


    public TeamData getWinner() {
        if (gameData != null) {

            TeamData teamData = null;

            for (Map.Entry<Long, TeamData> entry : gameData.teams.entrySet()) {
                if (teamData == null || teamData.score > entry.getValue().score) {
                    teamData = entry.getValue();
                }
            }

            return teamData;
        } else {
            Toasts.customShortToast(activity, R.string.game_not_initialized);
        }
        return null;
    }
}
