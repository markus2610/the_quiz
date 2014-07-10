package com.thilek.android.qleneagles_quiz.database.models;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class Group {


    public static final String PRIMARY_KEY = "_id";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_DESCRIPTION = "group_description";
    public static final String GROUP_PHOTO = "group_photo";



    public Long _id;
    public String group_name;
    public String group_description;
    public String group_photo;


    @Override
    public String toString() {
        return group_name + " , " + group_description + " , " + group_photo  ;
    }

}
