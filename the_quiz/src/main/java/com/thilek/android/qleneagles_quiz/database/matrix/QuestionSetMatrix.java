package com.thilek.android.qleneagles_quiz.database.matrix;

import android.content.ContentValues;
import android.database.Cursor;
import com.thilek.android.qleneagles_quiz.database.CupboardSqliteHelper;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;
import nl.qbusict.cupboard.QueryResultIterable;

import java.util.ArrayList;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class QuestionSetMatrix {

    public QuestionSetMatrix() {
    }


    public long addSet(QuestionSet questionSet) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).put(questionSet);
    }

    public boolean deleteQuestionSet(QuestionSet questionSet) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(QuestionSet.class, questionSet._id);
    }

    public long updateQuestionSet(QuestionSet questionSet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionSet.PRIMARY_KEY, questionSet._id);
        contentValues.put(QuestionSet.SET_NAME, questionSet.set_name);
        contentValues.put(QuestionSet.SET_TYPE, questionSet.set_type);

        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).update(QuestionSet.class, contentValues);
    }

    public QuestionSet getQuestionSetByID(long id) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(QuestionSet.class)
                .withSelection(QuestionSet.PRIMARY_KEY + " = ? ", String.valueOf(id)).get();
    }

    public void deleteAllQuestionSets() {
        cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(QuestionSet.class, null);
    }

    public ArrayList<QuestionSet> getAllQuestionSets() {

        ArrayList<QuestionSet> questionSets = new ArrayList<QuestionSet>();

        Cursor questionSetCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(QuestionSet.class).getCursor();

        QueryResultIterable<QuestionSet> clsQueryResultIterable = cupboard().withCursor(questionSetCursor).iterate(QuestionSet.class);
        for (QuestionSet questionSet : clsQueryResultIterable) {
            questionSets.add(questionSet);
        }
        clsQueryResultIterable.close();

        return questionSets;

    }

}
