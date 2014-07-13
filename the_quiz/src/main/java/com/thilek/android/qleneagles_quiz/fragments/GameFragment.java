package com.thilek.android.qleneagles_quiz.fragments;

import android.app.Fragment;
import com.thilek.android.qleneagles_quiz.activities.GameActivity;
import com.thilek.android.qleneagles_quiz.listeners.onFragmentBackListener;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameFragment extends Fragment implements onFragmentBackListener {

    @Override
    public boolean onFragmentBackPressed() {
        return true;
    }


    public GameActivity getFragmentActivity() {
        return (GameActivity) getActivity();
    }
}
