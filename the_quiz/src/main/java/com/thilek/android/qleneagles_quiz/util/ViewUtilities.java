package com.thilek.android.qleneagles_quiz.util;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.List;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class ViewUtilities {

    public static void changeEditableStatus(EditText editText, boolean isEditable) {
        if (!isEditable) { // disable editing password
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            editText.setClickable(false); // user navigates with wheel and selects widget

        } else { // enable editing of password
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
        }
    }


    public static boolean emptyFieldCheck(List<EditText> editTexts) {
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }
}
