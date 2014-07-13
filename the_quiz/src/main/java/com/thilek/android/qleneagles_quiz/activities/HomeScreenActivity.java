package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.views.Toasts;


public class HomeScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_layout);


    }


    public void onGameClicked(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void onGroupsClicked(View view) {
        startActivity(new Intent(this, GroupListActivity.class));
    }

    public void onSettingsClicked(View view) {
        startActivity(new Intent(this, SetListActivity.class));
    }

    public void onInfoClicked(View view) {
        Toasts.customShortToast(this, R.string.general_coming_soon_string);
    }
}
