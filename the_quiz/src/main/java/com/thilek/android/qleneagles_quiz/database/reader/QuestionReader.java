package com.thilek.android.qleneagles_quiz.database.reader;

import com.thilek.android.qleneagles_quiz.database.matrix.QuestionMatrix;
import com.thilek.android.qleneagles_quiz.database.models.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tsilvadorai on 14.07.14.
 */
public class QuestionReader {


    private static final String SEPARATOR = ",";
    private static final String SEARCH_KEY = "question";
    private static final int LANGUAGE_COLUMN_SIZE = 8;


    public QuestionReader() {
    }

    public QuestionMatrix read(InputStream clsInputStream) {
        QuestionMatrix matrix = new QuestionMatrix();
        matrix.deleteAllQuestions();

        BufferedReader clsBufferedReader = new BufferedReader(new InputStreamReader(clsInputStream));
        String line;

        try {
            while ((line = clsBufferedReader.readLine()) != null) {
                if (isQuestion(line)) {
                    Question clsLanguage = toQuestion(line);
                    if (clsLanguage != null)
                        matrix.addQuestion(clsLanguage);
                }
            }
            clsBufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }


    private boolean isQuestion(String line) {
        return !"".equals(line.trim()) && !line.contains(SEARCH_KEY);
    }


    private Question toQuestion(String strLine) {

        String[] parts = strLine.split(SEPARATOR);
        if (parts.length < LANGUAGE_COLUMN_SIZE) return null;

        Question clsQuestion = new Question();
        clsQuestion.question = parts[0];
        clsQuestion.option_one = parts[1];
        clsQuestion.option_two = parts[2];
        clsQuestion.option_three = parts[3];
        clsQuestion.option_four = parts[4];
        clsQuestion.right_answer = parts[5];
        clsQuestion.answer_option = parts[6];
        clsQuestion.difficulty = Integer.valueOf(parts[7]);
        clsQuestion.question_status = Question.UNUSED;

        return clsQuestion;
    }

}
