package com.thilek.android.qleneagles_quiz.database.matrix;

import android.content.ContentValues;
import android.database.Cursor;
import com.thilek.android.qleneagles_quiz.database.CupboardSqliteHelper;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import nl.qbusict.cupboard.QueryResultIterable;

import java.util.ArrayList;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by tsilvadorai on 14.06.14.
 */
public class GroupMatrix {

    public GroupMatrix() {
    }


    public long addGroup(Group group){
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).put(group);
    }

    public boolean deleteGroup(Group group){
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Group.class,group._id);
    }

    public long updateGroup(Group group){
        ContentValues contentValues =  new ContentValues();
        contentValues.put(Group.PRIMARY_KEY,group._id);
        contentValues.put(Group.GROUP_NAME,group.group_name);
        contentValues.put(Group.GROUP_DESCRIPTION,group.group_description);
        contentValues.put(Group.GROUP_PHOTO,group.group_photo);

        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).update(Group.class, contentValues);
    }

    public Group getGroupByID(long id){
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Group.class)
                .withSelection(Group.PRIMARY_KEY + " = ? "  , String.valueOf(id)).get();
    }

    public void deleteAllGroups() {
        cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Group.class, null);
    }

    public ArrayList<Group> getAllGroups() {

        ArrayList<Group> groups = new ArrayList<Group>();

        Cursor groupCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Group.class).getCursor();

        QueryResultIterable<Group> clsQueryResultIterable = cupboard().withCursor(groupCursor).iterate(Group.class);
        for (Group group : clsQueryResultIterable) {
            groups.add(group);
        }
        clsQueryResultIterable.close();

        return groups;

    }

}
