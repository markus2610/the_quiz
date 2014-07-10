package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.game_manager.GameManager;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameActivity extends Activity {

    public GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);

        gameManager = new GameManager(this);
    }
}
