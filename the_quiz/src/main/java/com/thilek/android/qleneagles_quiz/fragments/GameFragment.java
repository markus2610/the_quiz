package com.thilek.android.qleneagles_quiz.fragments;

import android.app.Fragment;
import com.thilek.android.qleneagles_quiz.activities.GameActivity;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class GameFragment extends Fragment {

    public GameActivity getFragmentActivity() {
        return (GameActivity) getActivity();
    }
}
