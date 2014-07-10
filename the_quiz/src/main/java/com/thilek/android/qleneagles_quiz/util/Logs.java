package com.thilek.android.qleneagles_quiz.util;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;

/**
 * Logging Class.<br> Can be used like {@link android.util.Log}: <code>Logs.d("tagString", "messageString");</code><br>
 * But you can send just "this" instead of tagString to Log with the SimpleClassName as your Tag: <code>Logs.d(this, "messageString");</code> <br>
 * Or you can just do <code>Logs.d(messageString);</code> and the Class will try to fill in the ClassName drom Stacktraces as Tag. (Not always working)
 *
 * @author stost
 */
public class Logs {

    public static boolean isLogsEnabled() {
        return logsEnabled;
    }

    private static boolean logsEnabled = true;

    public final static void d(Object tag, String message) {
        if (logsEnabled) {
            Log.d(tag.getClass().getSimpleName(), message);
        }
    }

    public final static void d(String message) {
        if (logsEnabled) {
            Log.d(getLocation(), message);
        }
    }

    public final static void d(String tag, String message) {
        if (logsEnabled) {
            Log.d(tag, message);
        }
    }

    public final static void e(Object tag, String message) {
        if (logsEnabled) {
            Log.e(tag.getClass().getSimpleName(), message);
        }
    }

    public final static void e(Exception message) {
        if (logsEnabled) {
           message.printStackTrace();
        }
    }

    public final static void e(String message) {
        if (logsEnabled) {
            Log.e(getLocation(), message);
        }
    }

    public final static void e(String tag, String message) {
        if (logsEnabled) {
            Log.e(tag, message);
        }
    }

    public final static void stack(String tag) {
        if (logsEnabled) {
            if (tag == null) new Exception().printStackTrace();
            else new Exception("Printing StackTrace for " + tag).printStackTrace();
        }
    }

    public final static void stack() {
        stack(null);
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }
            return getClassName(clazz.getEnclosingClass());
        }
        return "";
    }

    /**
     * Gets a tag for the calling class
     * Caution: This is slow! Dont use in often repeating Logs!
     *
     * @return
     */
    private static String getLocation() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int i = 1;
        try {
            while (Logs.class.getName().equals(stackTraceElements[i].getClassName()) || Thread.class.getName().equals(stackTraceElements[i].getClassName())) {
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            i--;
        }
        int lineNumber = stackTraceElements[i].getLineNumber();
        String className = stackTraceElements[i].getClassName();
        String methodName = stackTraceElements[i].getMethodName();
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            return "[" + getClassName(clazz) + ":" + methodName + ":" + lineNumber + "]: ";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public final static void i(Object tag, String message) {
        if (logsEnabled) {
            Log.i(tag.getClass().getSimpleName(), message);
        }
    }

    public final static void i(String message) {
        if (logsEnabled) {
            Log.i(getLocation(), message);
        }
    }

    public final static void i(String tag, String message) {
        if (logsEnabled) {
            Log.i(tag, message);
        }
    }

    /**
     * Sets the loging to enabled/disabled depending on ApplicationInfo.FLAG_DEBUGGABLE. <br>
     * If you export a signed App using Eclipse it automatically sets this Flag to false.
     *
     * @param appContext
     * @return true if logging is enabled (=App is debuggable)
     */
    public static boolean initialize(Context appContext) {
        Logs.logsEnabled = (0 != (appContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        return Logs.logsEnabled;
    }

    /**
     * Enable or disable Logging.<br>
     * This value will be changed by {@link com.hdm_i.workbookbasic.util.L initialize(Context appContext)}.
     *
     * @param enabled
     */
    public static void setEnabled(boolean enabled) {
        Logs.logsEnabled = enabled;
    }

    public final static void v(Object tag, String message) {
        if (logsEnabled) {
            Log.v(tag.getClass().getSimpleName(), message);
        }
    }

    /**
     * Dont use this in frequently repeated logs. it is very slow and will slow down the whole app.
     *
     * @param message
     */
    @Deprecated
    public final static void v(String message) {
        if (logsEnabled) {
            Log.v(getLocation(), message);
        }
    }

    public final static void v(String tag, String message) {
        if (logsEnabled) {
            Log.v(tag, message);
        }
    }

    public final static void w(Object tag, String message) {
        if (logsEnabled) {
            Log.w(tag.getClass().getSimpleName(), message);
        }
    }

    public final static void w(String tag, String message) {
        if (logsEnabled) {
            Log.w(tag, message);
        }
    }

    public final static void w(String message) {
        if (logsEnabled) {
            Log.w(getLocation(), message);
        }
    }

    public final static void wtf(Object tag, String message) {
        if (logsEnabled) {
            Log.wtf(tag.getClass().getSimpleName(), message);
        }
    }

    public final static void wtf(String message) {
        if (logsEnabled) {
            Log.wtf(getLocation(), message);
        }
    }

    public final static void wtf(String tag, String message) {
        if (logsEnabled) {
            Log.wtf(tag, message);
        }
    }

}


