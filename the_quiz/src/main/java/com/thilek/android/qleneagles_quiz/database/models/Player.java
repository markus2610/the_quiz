package com.thilek.android.qleneagles_quiz.database.models;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class Player {


    public static final String PRIMARY_KEY = "_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String DEPARTMENT = "department";
    public static final String GROUP_ID = "group_id";



    public Long _id;
    public String first_name;
    public String last_name;
    public String department;
    public Long group_id;


    @Override
    public String toString() {
        return first_name + " , " + last_name + " , " + department + " , " + group_id ;
    }

}
