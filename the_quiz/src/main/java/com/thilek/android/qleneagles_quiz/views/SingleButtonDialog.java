package com.thilek.android.qleneagles_quiz.views;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.R;


/**
 * Utilities for  AsyncTask
 *
 * @author thsi276hdm 2014-04-23
 */
public class SingleButtonDialog extends Dialog {

    private TextView txtMessage;
    private Button btnOk;
    private Context clsContext;


    /**
     * Dialog constructor
     *
     * @param context
     * @author thsi276hdm 2014-04-23
     */
    public SingleButtonDialog(Context context) {
        super(context, R.style.custom_app_dialog);

        setContentView(R.layout.dialog_generic_single_layout);

        clsContext = context;

        txtMessage = (TextView) findViewById(R.id.dialog_text);
        btnOk = (Button) findViewById(R.id.dialog_button);
    }

    /**
     * Set progress dialog is cancelable
     *
     * @param isCancelable
     * @author thsi276hdm 2014-04-23
     */
    public void setProgressCancelable(boolean isCancelable) {
        this.setCancelable(isCancelable);
    }


    /**
     * Initial button and the dialog messages
     *
     * @param iResourceIDDialogMessage
     * @param iResourceIDButton
     * @author thsi276hdm 2014-04-23
     */
    public void init(int iResourceIDDialogMessage, int iResourceIDButton) {
        btnOk.setText(clsContext.getString(iResourceIDButton));
        txtMessage.setText(clsContext.getString(iResourceIDDialogMessage));
    }


    /**
     * Initial button and the dialog messages
     *
     * @param strMessage
     * @param strButton
     * @author thsi276hdm 2014-04-23
     */
    public void init(String strMessage, String strButton) {
        btnOk.setText(strButton);
        txtMessage.setText(strMessage);
    }


    /**
     * Return ok button
     *
     * @return ok button
     * @author thsi276hdm 2014-04-23
     */
    public Button getButton() {
        return btnOk;
    }
}
