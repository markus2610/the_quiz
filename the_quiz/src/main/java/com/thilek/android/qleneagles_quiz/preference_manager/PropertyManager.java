package com.thilek.android.qleneagles_quiz.preference_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;
import com.thilek.android.qleneagles_quiz.AppContext;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {

    private static PropertyManager sharedPropertiesManager;

    private static String WRITABLE_PROPERTIES = "writableVenueProperties";

    private static String READONLY_PROPERTIES = "application.properties";

    private SharedPreferences writableProperties;

    private Properties readOnlyProperties;

    public PropertyManager() {
        loadProperties(AppContext.getContext());
    }

    public PropertyManager(Context context) {
        loadProperties(AppContext.getContext());
    }

    private void loadProperties(Context context) {
        readOnlyProperties = loadProperties(context, READONLY_PROPERTIES);
        writableProperties = context.getSharedPreferences(WRITABLE_PROPERTIES, 0);
    }

    public static PropertyManager getInstance() {
        if (sharedPropertiesManager == null)
            sharedPropertiesManager = new PropertyManager();
        return sharedPropertiesManager;
    }

    public static PropertyManager getInstance(Context context) {
        if (sharedPropertiesManager == null)
            sharedPropertiesManager = new PropertyManager(context);
        return sharedPropertiesManager;
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = writableProperties.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = writableProperties.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    public Integer getInt(String name) {
        int value = writableProperties.getInt(name, -1);
        if (value != -1) {
            return value;
        }
        return Integer.valueOf(readOnlyProperties.getProperty(name, "-1"));
    }

    public String getString(String name) {
        String value = writableProperties.getString(name, null);
        if (value != null) {
            return value;
        }
        value = readOnlyProperties.getProperty(name, null);
        return value;
    }

    public String getString(String name, String defaultValue) {
        String value = getString(name);
        return value != null ? value : defaultValue;
    }

    public Boolean getBoolean(String name) {
        try {
            String value = writableProperties.getString(name, null);
            if (value != null) {
                return Boolean.valueOf(value);
            }
            value = readOnlyProperties.getProperty(name);
            return Boolean.valueOf(value);
        } catch (ClassCastException e) {
            Log.e(this.getClass().getName(), e.toString() + " with key:" + name);
            return null;
        }
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = writableProperties.edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public void log() {
        System.out.println("**********************************************************");
        System.out.println("readonly");
        System.out.println();
        readOnlyProperties.list(System.out);
        System.out.println("**********************************************************");
        System.out.println("writable");
        System.out.println();
        for (String key : writableProperties.getAll().keySet()) {
            try {
                System.out.println(key + "=" + writableProperties.getString(key, "<null>"));
            } catch (ClassCastException e) {
                try {
                    System.out.println(key + "=" + writableProperties.getInt(key, -1));
                } catch (ClassCastException e2) {
                    try {
                        System.out.println(key + "=" + writableProperties.getBoolean(key, false));
                    } catch (ClassCastException e3) {

                    }
                }
            }
        }
        System.out.println("**********************************************************");
    }

    public Properties getWritableProperties() {
        return readOnlyProperties;
    }

    private Properties loadProperties(Context context, String fileName) {
        Resources resources = context.getResources();
        AssetManager assetManager = resources.getAssets();
        Properties properties = new Properties();
        try {
            InputStream inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
