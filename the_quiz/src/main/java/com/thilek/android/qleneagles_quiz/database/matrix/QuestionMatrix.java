package com.thilek.android.qleneagles_quiz.database.matrix;

import android.content.ContentValues;
import android.database.Cursor;
import com.thilek.android.qleneagles_quiz.database.CupboardSqliteHelper;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import nl.qbusict.cupboard.QueryResultIterable;

import java.util.ArrayList;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class QuestionMatrix {

    public QuestionMatrix() {
    }


    public long addQuestion(Question question) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).put(question);
    }

    public long updateQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Question.PRIMARY_KEY, question._id);
        contentValues.put(Question.QUESTION, question.question);
        contentValues.put(Question.OPTION_ONE, question.option_one);
        contentValues.put(Question.OPTION_TWO, question.option_two);
        contentValues.put(Question.OPTION_THREE, question.option_three);
        contentValues.put(Question.OPTION_FOUR, question.option_four);
        contentValues.put(Question.ANSWER, question.right_answer);
        contentValues.put(Question.ANSWER_OPTION, question.answer_option);
        contentValues.put(Question.DIFFICULTY, question.difficulty);
        contentValues.put(Question.SET_ID, question.set_id);
        contentValues.put(Question.STATUS, question.question_status);

        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).update(Question.class, contentValues);
    }

    public boolean deleteQuestion(Question question) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Question.class, question._id);
    }

    public Question getQuestionByID(long id) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Question.class)
                .withSelection(Group.PRIMARY_KEY + " = ? ", String.valueOf(id)).get();
    }

    public void deleteAllQuestions() {
        cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Question.class, null);
    }

    public ArrayList<Question> getAllQuestions() {

        ArrayList<Question> groups = new ArrayList<Question>();

        Cursor questionCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Question.class).getCursor();

        QueryResultIterable<Question> clsQueryResultIterable = cupboard().withCursor(questionCursor).iterate(Question.class);
        for (Question question : clsQueryResultIterable) {
            groups.add(question);
        }
        clsQueryResultIterable.close();

        return groups;

    }

    public ArrayList<Question> getQuestionsBySetID(long setID) {

        ArrayList<Question> groups = new ArrayList<Question>();

        Cursor questionCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Question.class)
                .withSelection(Question.SET_ID + " = ? ", String.valueOf(setID)).getCursor();

        QueryResultIterable<Question> clsQueryResultIterable = cupboard().withCursor(questionCursor).iterate(Question.class);
        for (Question question : clsQueryResultIterable) {
            groups.add(question);
        }
        clsQueryResultIterable.close();

        return groups;

    }


    public ArrayList<Question> getQuestionsDifficulty(int difficulty, int amount) {

        ArrayList<Question> groups = new ArrayList<Question>();

        Cursor questionCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Question.class)
                .withSelection(Question.DIFFICULTY + " = ?  AND " + Question.STATUS + " = ?", String.valueOf(difficulty), String.valueOf(Question.UNUSED)).getCursor();

        QueryResultIterable<Question> clsQueryResultIterable = cupboard().withCursor(questionCursor).iterate(Question.class);
        for (Question question : clsQueryResultIterable) {
            groups.add(question);

            if (groups.size() == amount) {
                break;
            }
        }
        clsQueryResultIterable.close();

        return groups;

    }


}
