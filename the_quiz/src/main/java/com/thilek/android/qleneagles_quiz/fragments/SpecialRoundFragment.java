package com.thilek.android.qleneagles_quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class SpecialRoundFragment extends GameFragment {

    private View vFragmentView;

    public static SpecialRoundFragment getInstance() {
        SpecialRoundFragment specialRoundFragment = new SpecialRoundFragment();
        return specialRoundFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        vFragmentView = inflater.inflate(R.layout.fragment_round, container, false);


        return vFragmentView;
    }
}
