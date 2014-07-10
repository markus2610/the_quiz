package com.thilek.android.qleneagles_quiz.database.models;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class Question {

    public static final int DIFFICULTY_LEVEL_1 = 1;
    public static final int DIFFICULTY_LEVEL_2 = 2;
    public static final int DIFFICULTY_LEVEL_3 = 3;


    public static final String PRIMARY_KEY = "_id";
    public static final String QUESTION = "question";
    public static final String OPTION_ONE = "option_one";
    public static final String OPTION_TWO = "option_two";
    public static final String OPTION_THREE = "option_three";
    public static final String OPTION_FOUR = "option_four";
    public static final String ANSWER = "right_answer";
    public static final String ANSWER_OPTION = "answer_option";
    public static final String DIFFICULTY = "difficulty";
    public static final String SET_ID = "set_id";


    public Long _id;
    public String question;
    public String option_one;
    public String option_two;
    public String option_three;
    public String option_four;
    public String right_answer;
    public String answer_option;
    public int difficulty;
    public Long set_id;

    @Override
    public String toString() {
        return question + " , " + option_one + " , " + option_two + " , " + option_three + " , " + option_four + " , " + right_answer + " , " + answer_option +  " , " + difficulty + " , " + set_id;
    }
}
