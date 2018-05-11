package com.example.administrator.mannotation;

import android.support.annotation.NonNull;

import java.lang.annotation.Inherited;

@Name
public abstract class Person {
    @Name()
    protected String mName;
    @Age(value = 10)
    protected int mAge;
    @Gender(Gender.Type.Female)
    protected String mGender;
    @Job (name = "Android程序员", isManager = false)
    protected String mJob;

    @Level(1)
    protected int mLevel;

    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }

    public @Level(1) int getLevel() {
        return mLevel;
    }

    public void setLevel(@Level(1) int level, @Age int age) {
        mLevel = level;
    }

    public @Age int getAge() {
        return mAge;
    }

    public void setAge(@Age int age) {
        mAge = age;
    }

    @Age
    public @Gender @Name String getGender(@Age(2) int age) {
        return mGender;
    }

    public @Gender @Name @Job(name = "abcd", isManager = true) abstract String getAllInfo(@Level(10) @Age(30)int level);

    public void setGender(String gender) {
        mGender = gender;
    }

    public void setJob(String job) {
        mJob = job;
    }

    public @Job(name = "anndroid", isManager = false) String getJob(@Level(10) int level) {
        return mJob;
    }

    @Override
    public String toString() {
        return "name=" + mName + " age=" + mAge + " gender=" + mGender;
    }
}
