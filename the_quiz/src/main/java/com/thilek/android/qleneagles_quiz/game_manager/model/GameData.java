package com.thilek.android.qleneagles_quiz.game_manager.model;

import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.views.Toasts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameData {

    public static final String GAME_ID = "game_id";
    public static final String GAME_DATE = "date";
    public static final String TEAMS = "teams";

    public long game_id;
    public Date gameDate;

    public Map<Long, TeamData> teams = new HashMap<Long, TeamData>();


    public GameData() {
        gameDate = new Date();
        game_id = gameDate.getTime();
    }


    public JSONObject generateJson() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(GAME_ID,game_id);
        jsonObject.put(GAME_DATE,gameDate.getTime());

        JSONArray teamsJsonArray =  new JSONArray();

        for (Map.Entry<Long, TeamData> entry : teams.entrySet()) {
            teamsJsonArray.put(entry.getValue().generateJson());
        }

        jsonObject.put(TEAMS,teamsJsonArray);


        return jsonObject;
    }

}
