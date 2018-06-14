package com.example.administrator.rooms;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {User.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase{
    private static UserDatabase sDatabase;
    private static Object sObject = new Object();
    private static Migration migrations1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE users_tb " + " ADD COLUMN address1_city TEXT default null");
            database.execSQL("ALTER TABLE users_tb " + " ADD COLUMN address2_city TEXT default null");
            Log.i("room---", "upgrade 1 to 2");
        }
    };

    public static UserDatabase getInstance(Context context) {
        if(sDatabase == null) {
            synchronized (sObject) {
                if(sDatabase == null){
                    //允许主线程访问数据库，默认是不允许
//                    sDatabase = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "users.db")
//                            .allowMainThreadQueries()
//                            .build();
                    sDatabase = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "users.db")
                            .addMigrations(migrations1_2)
                            .build();
                }
            }
        }
        return sDatabase;
    }

    public abstract UserDao getUserDao();
}
