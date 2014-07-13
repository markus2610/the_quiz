package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.fragments.GameFragment;
import com.thilek.android.qleneagles_quiz.fragments.NewGameSettingFragment;
import com.thilek.android.qleneagles_quiz.game_manager.GameManager;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameActivity extends Activity {

    public GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_layout);

        gameManager = new GameManager(this);

        NewGameSettingFragment newGameSettingFragment = NewGameSettingFragment.getInstance();
        getFragmentManager().beginTransaction().replace( R.id.fragment_frame, newGameSettingFragment ).commit();
    }

    @Override
    public void onSaveInstanceState( Bundle savedInstanceState )
    {
        super.onSaveInstanceState( savedInstanceState );
    }

    @Override
    public void onRestoreInstanceState( Bundle savedInstanceState )
    {
        super.onRestoreInstanceState( savedInstanceState );
    }

    @Override
    public void onBackPressed()
    {
        GameFragment fragment = ( GameFragment ) getFragmentManager().findFragmentById( R.id.fragment_frame );
        if ( fragment != null )
        {
            if ( fragment.onFragmentBackPressed() )
            {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }
}
