package com.example.accessibility.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.example.accessibility.application.AccessibilityApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + AccessibilityApplication.sContext.getPackageName() + "/" + "ws_db.db";


    private static DBHelper sDBHelper = new DBHelper(AccessibilityApplication.sContext, "ws_db.db", null, 1);

    public static DBHelper getInstance() {
        return sDBHelper;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static void query(){
        try {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
            Cursor cursor = db.rawQuery("select * from ws_group where id < 5", null);
            while (cursor.moveToNext()){
                Log.i(TAG, "id=" + cursor.getInt(cursor.getColumnIndex("id")) + " uri=" + cursor.getString(cursor.getColumnIndex("url")));
            }
        }catch (Exception e){
            Log.i(TAG, "" + e);
        }

    }

    public static boolean copyDBFromFile(){
        File src = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "access.whatsapp" + File.separator + "ws_db.db");
        File dst = new File(DB_PATH);
        if(dst.exists()){
            return true;
        }
        if(src.exists()){
            try {
                FileInputStream fis = new FileInputStream(src);
                FileOutputStream fos = new FileOutputStream(dst);
                byte[] buf = new byte[1024];
                int count = 0;
                while ((count = fis.read(buf)) > 0) {
                    fos.write(buf, 0, count);
                }
                fis.close();
                fos.close();
                Log.i(TAG, "copy db success");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
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
