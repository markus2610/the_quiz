package com.thilek.android.qleneagles_quiz.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import com.thilek.android.qleneagles_quiz.tasks.AddPlayerTask;
import com.thilek.android.qleneagles_quiz.tasks.GetPlayerTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.tasks.UpdatePlayerTask;
import com.thilek.android.qleneagles_quiz.views.Toasts;


public class PlayerDetailActivity extends Activity implements TaskListener {

    private static final int GET_PLAYER = 1;
    private static final int ADD_PLAYER = 2;
    private static final int SAVE_PLAYER = 3;

    private TextView firstNameText, lastNameText, departmentText;
    private EditText firstNameEditText, lastNameEditText, departmentEditText;
    private ViewSwitcher firstNameSwitch, lastNameSwitch, departmentSwitch;
    private Player player;
    private long groupID;

    Animation slide_in_left, slide_out_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_detail_layout);

        slide_in_left = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right);

        firstNameText = (TextView) findViewById(R.id.first_name_text);
        lastNameText = (TextView) findViewById(R.id.last_name_text);
        departmentText = (TextView) findViewById(R.id.department_text);


        firstNameEditText = (EditText) findViewById(R.id.first_name_edittext);
        lastNameEditText = (EditText) findViewById(R.id.last_name_edittext);
        departmentEditText = (EditText) findViewById(R.id.department_edittext);

        firstNameSwitch = (ViewSwitcher) findViewById(R.id.first_name_switcher);
        firstNameSwitch.setInAnimation(slide_in_left);
        firstNameSwitch.setOutAnimation(slide_out_right);

        lastNameSwitch = (ViewSwitcher) findViewById(R.id.last_name_switcher);
        lastNameSwitch.setInAnimation(slide_in_left);
        lastNameSwitch.setOutAnimation(slide_out_right);

        departmentSwitch = (ViewSwitcher) findViewById(R.id.department_switcher);
        departmentSwitch.setInAnimation(slide_in_left);
        departmentSwitch.setOutAnimation(slide_out_right);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstants.GROUP_ID)) {

            groupID = bundle.getLong(AppConstants.GROUP_ID);

            if (bundle.containsKey(AppConstants.PLAYER_ID)) {
                getPlayerDetailFromDatabase(bundle.getLong(AppConstants.PLAYER_ID));
            } else {
                addNewPlayer();
            }
        } else {
            finish();
        }

    }


    private void movePlayerDetail() {
        firstNameText.setText(firstNameEditText.getText().toString());
        lastNameText.setText(lastNameEditText.getText().toString());
        departmentText.setText(departmentEditText.getText().toString());
    }

    private void loadPlayerDetails() {
        firstNameText.setText(player.first_name);
        lastNameText.setText(player.last_name);
        departmentText.setText(player.department);

        firstNameEditText.setText(player.first_name);
        lastNameEditText.setText(player.last_name);
        departmentEditText.setText(player.department);
    }

    private void getPlayerDetails() {
        if (player == null) {
            player = new Player();
            player.group_id = groupID;
            player.first_name =  getString(R.string.default_player);
        }

        player.first_name = firstNameEditText.getText().toString();
        player.last_name = lastNameEditText.getText().toString();
        player.department = departmentEditText.getText().toString();
    }

    public void onPlayerDoneClicked(View view) {
        getPlayerDetails();
        new UpdatePlayerTask(this,SAVE_PLAYER).execute(player);
    }

    public void onEditPlayerClicked(View view) {
        ImageView editIcon = (ImageView) findViewById(R.id.editButton);

        if (editIcon.isActivated()) {
            movePlayerDetail();
            editIcon.setActivated(false);
            switchViewSwitcher(false);
        } else {
            editIcon.setActivated(true);
            switchViewSwitcher(true);
        }
    }


    private void addNewPlayer() {
        player = new Player();
        player.group_id = groupID;
        player.first_name =  getString(R.string.default_player);

        new AddPlayerTask(this, ADD_PLAYER).execute(player);

        switchViewSwitcher(true);
    }

    private void getPlayerDetailFromDatabase(long group_id) {
        new GetPlayerTask(this, GET_PLAYER).execute(group_id);
    }

    private void switchViewSwitcher(boolean showNext) {
        if (showNext) {
            lastNameSwitch.showNext();
            firstNameSwitch.showNext();
            departmentSwitch.showNext();
        } else {
            lastNameSwitch.showPrevious();
            firstNameSwitch.showPrevious();
            departmentSwitch.showPrevious();
        }
    }


    @Override
    public void onTaskStarted(int taskID, Object object) {

    }

    @Override
    public void onTaskProgressUpdated(int taskID, Object object) {

    }

    @Override
    public void onTaskCompleted(int taskID, Object object) {

        switch (taskID) {
            case GET_PLAYER: {
                player = (Player) object;
                loadPlayerDetails();
            }
            break;

            case ADD_PLAYER: {
                player = (Player) object;
            }
            break;

            case SAVE_PLAYER: {
                boolean isSaved = (Boolean) object;
                if (isSaved) {
                    finish();
                } else {
                    Toasts.customShortToast(PlayerDetailActivity.this, R.string.general_failed_update);
                }

            }
            break;
        }

    }

    @Override
    public void onTaskCanceled(int taskID, Object object) {

    }

    @Override
    public void onTaskError(int taskID, Object object) {

    }
}
