package com.thilek.android.qleneagles_quiz.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.tasks.GetGroupsTask;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class RoundsSetting extends GameFragment {

    private View vFragmentView;

    public static NewGameSettingFragment getInstance() {

        NewGameSettingFragment newGameSettingFragment = new NewGameSettingFragment();

        return newGameSettingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        vFragmentView = inflater.inflate(R.layout.fragment_game_set_up, container, false);



        return vFragmentView;
    }
}
