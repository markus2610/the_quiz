package com.thilek.android.qleneagles_quiz.database.matrix;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public interface MatrixFactoryInterface {

    public abstract GroupMatrix getGroupMatrix();

    public abstract PlayerMatrix getPlayerMatrix();

    public abstract QuestionMatrix getQuestionMatrix();

    public abstract QuestionSetMatrix getQuestionSetMatrix();
}
