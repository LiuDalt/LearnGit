package com.example.administrator.rooms;

import android.arch.persistence.room.ColumnInfo;

public class SimpleInfo {
    @ColumnInfo(name = "user_name")
    public String name;
    @ColumnInfo(name = "user_age")
    public int age;

    @Override
    public String toString() {
        return "name=" + name + " age=" + age;
    }
}
