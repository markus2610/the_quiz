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
public class PlayerMatrix {

    public PlayerMatrix() {
    }


    public long addPlayer(Player player) {
       return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).put(player);
    }

    public long updatePlayer(Player player){
        ContentValues contentValues =  new ContentValues();
        contentValues.put(Player.PRIMARY_KEY,player._id);
        contentValues.put(Player.FIRST_NAME,player.first_name);
        contentValues.put(Player.LAST_NAME,player.last_name);
        contentValues.put(Player.DEPARTMENT,player.department);
        contentValues.put(Player.GROUP_ID,player.group_id);

        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).update(Player.class, contentValues);
    }

    public boolean deletePlayer(Player player) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Player.class, player._id);
    }

    public Player getPlayerByID(long id) {
        return cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Player.class)
                .withSelection(Player.PRIMARY_KEY + " = ? ", String.valueOf(id)).get();
    }

    public void deleteAllPlayers() {
        cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).delete(Player.class, null);
    }

    public ArrayList<Player> getAllPlayers() {

        ArrayList<Player> players = new ArrayList<Player>();

        Cursor playerCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Player.class).getCursor();

        QueryResultIterable<Player> clsQueryResultIterable = cupboard().withCursor(playerCursor).iterate(Player.class);
        for (Player player : clsQueryResultIterable) {
            players.add(player);
        }
        clsQueryResultIterable.close();

        return players;

    }


    public ArrayList<Player> getAllPlayersByGroupID(long groupID) {

        ArrayList<Player> players = new ArrayList<Player>();

        Cursor playerCursor = cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).query(Player.class)
                .withSelection(Player.GROUP_ID + " = ? ", String.valueOf(groupID)).getCursor();

        QueryResultIterable<Player> clsQueryResultIterable = cupboard().withCursor(playerCursor).iterate(Player.class);
        for (Player player : clsQueryResultIterable) {
            players.add(player);
        }
        clsQueryResultIterable.close();

        return players;

    }
}
