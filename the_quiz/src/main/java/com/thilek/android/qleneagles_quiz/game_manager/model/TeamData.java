package com.thilek.android.qleneagles_quiz.game_manager.model;

import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class TeamData {

    public static final String GROUP_ID = "id";
    public static final String SCORE = "score";
    public static final String SET_ID = "set_id";
    public static final String HELP_OPTION = "help_option";
    public static final String FILTER_OPTION = "filter_option";

    public long group_id;
    public Group group;

    public int score;
    public boolean helpOption;
    public boolean filterOption;
    public long set_id;


    public TeamData(long id) {
        this.group = AppContext.matrixFactory.getGroupMatrix().getGroupByID(id);
        this.group_id = id;
        this.helpOption = false;
        this.filterOption = false;
        this.score = 0;
        this.set_id = 0;
    }

    public TeamData(Group group) {
        this.group = group;
        this.group_id = group._id;
        this.helpOption = false;
        this.filterOption = false;
        this.score = 0;
        this.set_id = 0;
    }



    public JSONObject generateJson() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(GROUP_ID,group_id);
        jsonObject.put(SCORE,score);
        jsonObject.put(HELP_OPTION,helpOption);
        jsonObject.put(FILTER_OPTION,filterOption);
        jsonObject.put(SET_ID,set_id);

        return jsonObject;
    }


}
