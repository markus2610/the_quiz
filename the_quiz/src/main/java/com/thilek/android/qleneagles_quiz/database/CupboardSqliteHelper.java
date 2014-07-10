package com.thilek.android.qleneagles_quiz.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.thilek.android.qleneagles_quiz.AppConstants;
import com.thilek.android.qleneagles_quiz.AppContext;
import com.thilek.android.qleneagles_quiz.database.models.Group;
import com.thilek.android.qleneagles_quiz.database.models.Player;
import com.thilek.android.qleneagles_quiz.database.models.Question;
import com.thilek.android.qleneagles_quiz.database.models.QuestionSet;
import com.thilek.android.qleneagles_quiz.util.Logs;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class CupboardSqliteHelper extends SQLiteOpenHelper {


    static {
        /**
         * use annotations
         */
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());

        // register the models with cupboard. A model should be registered before it can be
        // used in any way. There are a few options to make sure the models are registered:
        // 1. In a static block like this in a SQLiteOpenHelper or ContentProvider
        // 2. In a custom Application class either form a static block or onCreate
        // 3. By creating your own factory class and have the static block there.

        cupboard().register(Player.class);
        cupboard().register(Group.class);
        cupboard().register(Question.class);
        cupboard().register(QuestionSet.class);

    }

    private static CupboardSqliteHelper mInstance = null;

    private CupboardSqliteHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.VERSION);
    }

    /**
     * use this to acces the sqlite helper
     */
    public static CupboardSqliteHelper getInstance(Context ctx) {
        /**
         * we use the application context
         * this will ensure that we dont accidentally leak an Activitys
         * context (see this article for more information:
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new CupboardSqliteHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables won't upgrade tables ; unlike upgradeTables() below.
        cupboard().withDatabase(db).createTables();

        //TODO: Index
        // we can setup an index on the author field or add other tables if we like.
        // db.execSQL("create index author_id on "+cupboard().getTable(Book.class)+" (author)");
        Logs.i(CupboardSqliteHelper.class.getSimpleName(), "Registered and created these tables:");
        for (Class clz : cupboard().getRegisteredEntities()) {
            Logs.i(CupboardSqliteHelper.class.getSimpleName(), cupboard().getTable(clz));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public static SQLiteDatabase getDatabase() {
        return CupboardSqliteHelper.getInstance(AppContext.getContext()).getWritableDatabase();
    }

}
