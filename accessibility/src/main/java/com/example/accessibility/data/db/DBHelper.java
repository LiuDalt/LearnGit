package com.example.accessibility.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.accessibility.application.AccessibilityApplication;
import com.example.accessibility.data.Group;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + AccessibilityApplication.sContext.getPackageName() + "/" + "ws_db.db";
    private static String TABLE_NAME = "ws_group";


    private static DBHelper sDBHelper;

    public static DBHelper getInstance() {
        if(sDBHelper == null){
            synchronized (DBHelper.class){
                if(sDBHelper == null){
                    sDBHelper = new DBHelper(AccessibilityApplication.sContext, DB_PATH, null, 1);
                }
            }
        }
        return sDBHelper;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public List<Group> query(int start, int end){
        List<Group> list = new ArrayList<>();
        try {
            SQLiteDatabase db = sDBHelper.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[] { "id", "url" }, "id>= ? and id <= ?",
                    new String[] { String.valueOf(start), String.valueOf(end) }, null, null, null);
            while (cursor.moveToNext()){
                Group group = new Group();
                group.mId = cursor.getInt(cursor.getColumnIndex("id"));
                group.mGroupLink = cursor.getString(cursor.getColumnIndex("url"));
                list.add(group);
            }
            cursor.close();
        }catch (Exception e){
            Log.i(TAG, "" + e);
        }
        return list;
    }

    public int getCount(){
        String sql = "select count(*) from " + TABLE_NAME;
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public static boolean copyDBFromFile(){
        File dst = new File(DB_PATH);
        if(dst.exists()){
            return true;
        }
        try {
            InputStream is = AccessibilityApplication.sContext.getAssets().open("ws_db.db");
            FileOutputStream fos = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = is.read(buf)) > 0) {
                fos.write(buf, 0, count);
            }
            is.close();
            fos.close();
            Log.i(TAG, "copy db success");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
