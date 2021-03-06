package com.thilek.android.qleneagles_quiz.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.thilek.android.qleneagles_quiz.R;
import com.thilek.android.qleneagles_quiz.adapters.GroupListAdapter;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.tasks.GetGroupsTask;
import com.thilek.android.qleneagles_quiz.tasks.TaskListener;
import com.thilek.android.qleneagles_quiz.views.Toasts;

import java.util.ArrayList;

/**
 * Created by tsilvadorai on 10.07.14.
 */
public class NewGameSettingFragment extends GameFragment implements View.OnClickListener, TaskListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int GET_ALL_GROUPS = 0;

    private View vFragmentView;

    private TextView emptyText, removeGroupMessage;
    private Button createGameButton, removeGroupButton, cancelButton;
    private ImageView addTeamButton;

    private ListView selectionGroupList, sameGroupList;
    private Dialog groupListDialog, removeGroupDialog;

    private GroupListAdapter groupListAdapter;

    private GetGroupsTask getGroupsTask;


    public static NewGameSettingFragment getInstance() {

        NewGameSettingFragment newGameSettingFragment = new NewGameSettingFragment();

        return newGameSettingFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        vFragmentView = inflater.inflate(R.layout.fragment_game_set_up, container, false);

        emptyText = (TextView) vFragmentView.findViewById(R.id.empty_list_text);

        sameGroupList = (ListView) vFragmentView.findViewById(R.id.group_selected_list);
        sameGroupList.setOnItemLongClickListener(this);
        sameGroupList.setEmptyView(emptyText);


        createGameButton = (Button) vFragmentView.findViewById(R.id.create_game_button);
        createGameButton.setOnClickListener(this);
        addTeamButton = (ImageView) vFragmentView.findViewById(R.id.adding_group_button);
        addTeamButton.setOnClickListener(this);

        groupListDialog = new Dialog(getActivity(), R.style.custom_app_dialog);
        groupListDialog.setContentView(R.layout.dialog_group_list);
        groupListDialog.setCanceledOnTouchOutside(false);

        selectionGroupList = (ListView) groupListDialog.findViewById(R.id.group_selection_list);
        selectionGroupList.setOnItemClickListener(this);


        removeGroupDialog = new Dialog(getActivity(), R.style.custom_app_dialog);
        removeGroupDialog.setContentView(R.layout.dialog_remove_group);
        removeGroupDialog.setCanceledOnTouchOutside(false);

        removeGroupButton = (Button) removeGroupDialog.findViewById(R.id.remove_group_button);
        removeGroupButton.setOnClickListener(this);
        cancelButton = (Button) removeGroupDialog.findViewById(R.id.cancel_group_button);
        cancelButton.setOnClickListener(this);
        removeGroupMessage = (TextView) removeGroupDialog.findViewById(R.id.remove_group_message);


        getGroupsTask = new GetGroupsTask(this, GET_ALL_GROUPS);
        getGroupsTask.execute();

        return vFragmentView;
    }

    private void createGame() {
        RoundFragment roundFragment = RoundFragment.getInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragment_frame, roundFragment).commit();
    }


    private void removeGroup(Group group) {
        getFragmentActivity().gameManager.removeTeam(group);
        sameGroupList.setAdapter(new GroupListAdapter(getFragmentActivity(), getFragmentActivity().gameManager.getAllGroups()));
        handleCreateButton();

        groupListDialog.dismiss();
    }

    private void handleCreateButton() {
        if (sameGroupList.getCount() < 1) {
            createGameButton.setVisibility(View.GONE);
        } else {
            createGameButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onFragmentBackPressed() {
        return true;
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
            case GET_ALL_GROUPS: {
                ArrayList<Group> groups = (ArrayList<Group>) object;

                if (groups.size() != 0) {
                    groupListAdapter = new GroupListAdapter(getActivity(), groups);
                    selectionGroupList.setAdapter(groupListAdapter);
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


    @Override
    public void onClick(View v) {
        if (v.getId() == addTeamButton.getId()) {
            if (groupListAdapter == null || groupListAdapter.getCount() < 1)
                Toasts.customShortToast(getActivity(), R.string.no_groups);
            else
                groupListDialog.show();
        } else if (v.getId() == createGameButton.getId()) {
            createGame();
        } else if (v.getId() == removeGroupButton.getId()) {
            Group group = (Group) removeGroupMessage.getTag();
            removeGroup(group);
        } else if (v.getId() == cancelButton.getId()) {
            removeGroupDialog.dismiss();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == selectionGroupList.getId()) {
            Group group = (Group) parent.getAdapter().getItem(position);
            getFragmentActivity().gameManager.addTeam(group);

            sameGroupList.setAdapter(new GroupListAdapter(getFragmentActivity(), getFragmentActivity().gameManager.getAllGroups()));
            handleCreateButton();

            groupListDialog.dismiss();
            removeGroupDialog.dismiss();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Group group = (Group) parent.getAdapter().getItem(position);

        String message = getString(R.string.remove_group_message);
        message = message.replace("***", group.group_name);

        removeGroupMessage.setText(message);
        removeGroupMessage.setTag(group);

        groupListDialog.dismiss();
        removeGroupDialog.show();

        return false;
    }
}
