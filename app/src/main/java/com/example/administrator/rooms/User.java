package com.example.administrator.rooms;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users_tb", indices = {@Index("user_age"), @Index("user_name")})
public class User extends Object {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_name")
    public String name;

    @ColumnInfo(name = "user_age")
    public int age;

    @Embedded(prefix = "address1_")
    public Address mAddress1;

    @Embedded(prefix = "address2_")
    public Address mAddress2;

    @Override
    public String toString() {
        return "id=" + id + " name=" + name + " age=" + age + " address1=" + mAddress1 + " address2=" + mAddress2;
    }

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress1(Address address1) {
        mAddress1 = address1;
    }

    public void setAddress2(Address address2) {
        mAddress2 = address2;
    }

    public Address getAddress1() {
        return mAddress1;
    }

    public Address getAddress2() {
        return mAddress2;
    }
}
