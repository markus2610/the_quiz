package com.thilek.android.qleneagles_quiz.database.models;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class QuestionSet {

    public static final String PRIMARY_KEY = "_id";
    public static final String SET_NAME = "set_name";
    public static final String SET_TYPE = "set_type";

    public Long _id;
    public String set_name;
    public String set_type;

    @Override
    public String toString() {
        return set_name + " , " + set_type ;
    }
}
