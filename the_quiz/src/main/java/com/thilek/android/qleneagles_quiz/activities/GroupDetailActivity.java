package com.thilek.android.qleneagles_quiz.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.PlayerListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import com.thilek.android.qleneagles_quiz.tasks.*;
import com.thilek.android.qleneagles_quiz.util.ViewUtilities;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;


public class GroupDetailActivity extends ListActivity implements TaskListener {

    private static final int GET_GROUP = 0;
    private static final int GET_PLAYERS = 1;
    private static final int ADD_NEW_GROUP = 2;
    private static final int SAVE_GROUP = 3;
    private static final int DELETE_PLAYER = 4;

    private static Handler uiThreadHandler;

    private EditText groupNameEditText;
    private ImageView groupPhoto;
    private PlayerListAdapter playerListAdapter;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_detail_layout);

        uiThreadHandler = new Handler(Looper.getMainLooper());

        TextView emptyText = (TextView) findViewById(R.id.empty_list_text);
        getListView().setEmptyView(emptyText);

        groupNameEditText = (EditText) findViewById(R.id.group_name);
        groupPhoto = (ImageView) findViewById(R.id.group_photo);

        ViewUtilities.changeEditableStatus(groupNameEditText, false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(AppConstants.GROUP_ID)) {
            getGroupDetailFromDatabase(bundle.getLong(AppConstants.GROUP_ID));
        } else {
            addNewGroup();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (group != null) {
            new GetPlayersByGroupTask(this, GET_PLAYERS).execute(group._id);
        }
    }

    @Override
    protected void onListItemClick(ListView list, View dataElement, int position, long id) {

        Player player = (Player) list.getAdapter().getItem(position);
        startActivity(new Intent(this, PlayerDetailActivity.class).putExtra(AppConstants.GROUP_ID, group._id).putExtra(AppConstants.PLAYER_ID, player._id));
    }


    public void onAddPlayerClicked(View view) {
        startActivity(new Intent(this, PlayerDetailActivity.class).putExtra(AppConstants.GROUP_ID, group._id));
    }

    public void onChangePhotoClicked(View view) {
        Toasts.customShortToast(this, R.string.general_coming_soon_string);
    }

    public void onGroupDoneClicked(View view) {
        getGroupDetail();
        new UpdateGroupTask(this, SAVE_GROUP).execute(group);
    }

    public void onChangeNameClicked(View view) {
        ViewUtilities.changeEditableStatus(groupNameEditText, true);
    }

    private void getGroupDetail() {
        group.group_name = groupNameEditText.getText().toString();
    }

    private void addNewGroup() {
        Group newGroup = new Group();
        newGroup.group_name = getString(R.string.default_group);

        new AddGroupTask(this, ADD_NEW_GROUP).execute(newGroup);
    }

    private void getGroupDetailFromDatabase(long group_id) {
        new GetGroupTask(this, GET_GROUP).execute(group_id);
    }

    private void loadGroupPhoto(String location) {

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
            case GET_PLAYERS: {
                final ArrayList<Player> players = (ArrayList<Player>) object;

                if (players.size() != 0) {
                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            playerListAdapter = new PlayerListAdapter(GroupDetailActivity.this, players);
                            setListAdapter(playerListAdapter);
                        }
                    });
                }

            }
            break;

            case GET_GROUP: {
                group = (Group) object;

                if (group != null) {
                    new GetPlayersByGroupTask(this, GET_PLAYERS).execute(group._id);

                    uiThreadHandler.post(new Runnable() {
                        public void run() {
                            groupNameEditText.setText(group.group_name);
                            loadGroupPhoto(group.group_photo);
                        }
                    });
                }

            }
            break;
            case ADD_NEW_GROUP: {
                group = (Group) object;
            }
            break;
            case SAVE_GROUP: {

                boolean isSaved = (Boolean) object;

                if (isSaved) {
                    finish();
                } else {
                    Toasts.customShortToast(GroupDetailActivity.this, R.string.general_failed_update);
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
