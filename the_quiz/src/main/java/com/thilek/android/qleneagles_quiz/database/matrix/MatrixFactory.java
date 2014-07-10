package com.thilek.android.qleneagles_quiz.database.matrix;

import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class MatrixFactory implements MatrixFactoryInterface{

    private GroupMatrix groupMatrix;
    private PlayerMatrix playerMatrix;
    private QuestionMatrix questionMatrix;
    private QuestionSetMatrix questionSetMatrix;

    @Override
    public GroupMatrix getGroupMatrix() {
        if (groupMatrix == null) {
            groupMatrix = new GroupMatrix();
        }
        return groupMatrix;
    }

    @Override
    public PlayerMatrix getPlayerMatrix() {
        if (playerMatrix == null) {
            playerMatrix = new PlayerMatrix();
        }
        return playerMatrix;
    }

    @Override
    public QuestionMatrix getQuestionMatrix() {
        if (questionMatrix == null) {
            questionMatrix = new QuestionMatrix();
        }
        return questionMatrix;
    }

    @Override
    public QuestionSetMatrix getQuestionSetMatrix() {
        if (questionSetMatrix == null) {
            questionSetMatrix = new QuestionSetMatrix();
        }
        return questionSetMatrix;
    }
}
