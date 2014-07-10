package com.thilek.android.qleneagles_quiz;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.thilek.android.qleneagles_quiz.database.CupboardSqliteHelper;
import com.thilek.android.qleneagles_quiz.database.matrix.MatrixFactory;
import com.thilek.android.qleneagles_quiz.database.matrix.MatrixFactoryInterface;
import com.thilek.android.qleneagles_quiz.game_manager.RecordManager;

import java.util.Locale;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by tsilvadorai on 08.04.14.
 */
public class AppContext extends Application {

    private volatile static AppContext appContext;

    public static MatrixFactoryInterface matrixFactory;

    public static RecordManager recordManager;

    public static Locale LOCALE = Locale.US;

    public static final boolean DEBUG_MODE = false;

    @Override
    public void onCreate() {

        AppContext.appContext = this;

        LOCALE = getResources().getConfiguration().locale;

        cupboard().withDatabase(CupboardSqliteHelper.getDatabase()).createTables();

        matrixFactory = new MatrixFactory();

        super.onCreate();
    }

    public static RecordManager getRecordManager() {

        if (recordManager == null)
            recordManager = new RecordManager();

        return recordManager;
    }


    public static Context getContext() {
        return appContext.getApplicationContext();
    }

    public static String getResourceString(int resId) {
        return appContext.getApplicationContext().getString(resId);
    }

    public static SharedPreferences getPreferenceSetting() {

        SharedPreferences preferenceSetting = getContext()
                .getSharedPreferences(
                        getContext().getString(R.string.app_name),
                        Context.MODE_PRIVATE);

        return preferenceSetting;
    }

    /**
     * returns the class name with the current package name of the project
     * this string is uses as identifier in the inten filter
     *
     * @param context    context
     * @param simpleName simple class name as from class.getSimpleName
     * @return
     */
    public static String getTargetName(Context context, String simpleName) {
        return context.getPackageName() + ".activities." + simpleName;
    }

}
