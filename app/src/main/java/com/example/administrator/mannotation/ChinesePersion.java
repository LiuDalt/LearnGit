package com.example.administrator.mannotation;

import android.support.annotation.NonNull;


@Age
public class ChinesePersion extends Person {
    @Override
    public String getGender(int age) {
        return super.getGender(age);
    }

    @Override
    public String getAllInfo(int level) {
        return null;
    }

}
