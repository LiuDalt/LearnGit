package com.example.accessibility.sharepre;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class SharePreferenceUtils {

    private static final String SP_NAME = "wa_access_sp";

    private static SharedPreferences mPreferences;

    public static void init(Context context){
        mPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(){
        return mPreferences.edit();
    }

    public static boolean put(String key, Object value, Type type){
        SharedPreferences.Editor editor = getEditor();
        switch (type){
            case LONG:
                editor.putLong(key, (Long) value);
                break;
            case FLOAT:
                editor.putFloat(key, (Float) value);
                break;
            case STRING:
                editor.putString(key, (String) value);
                break;
            case BOOLEAN:
                editor.putBoolean(key, (Boolean) value);
                break;
            case INTEGER:
                editor.putInt(key, (Integer) value);
                break;
            case STRING_SET:
                editor.putStringSet(key, (Set<String>) value);
                break;
        }
        return editor.commit();
    }

    public static Object get(String key, Object def, Type type){
        switch (type) {
            case LONG:
                return mPreferences.getLong(key, (Long) def);
            case FLOAT:
                return mPreferences.getFloat(key, (Float) def);
            case STRING:
                return mPreferences.getString(key, (String) def);
            case BOOLEAN:
                return mPreferences.getBoolean(key, (Boolean) def);
            case INTEGER:
                return mPreferences.getInt(key, (Integer) def);
            case STRING_SET:
                return mPreferences.getStringSet(key, (Set<String>) def);
        }
        return null;
    }



}
