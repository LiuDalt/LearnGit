package com.example.accessibility.time;

import android.util.Log;

public class Time {
    public int mYear;
    public int mMonth;
    public int mDay;
    public int mHour;
    public int mMinute;
    public int mSecond;

    public String toSharePreferenceStr(){
        return mYear + "_" + mMonth + "_" + mDay + "_" + mHour + "_" + mMinute + "_" + mSecond;
    }

    public static Time parseFromSharePreferenceStr(String str){
        String[] splits = str.split("_");
        Time time = new Time();
        time.mYear = Integer.parseInt(splits[0]);
        time.mMonth = Integer.parseInt(splits[1]);
        time.mDay = Integer.parseInt(splits[2]);
        time.mHour = Integer.parseInt(splits[3]);
        time.mMinute = Integer.parseInt(splits[4]);
        time.mSecond = Integer.parseInt(splits[5]);
        return time;
    }

    @Override
    public String toString() {
        return "year = [" + mYear + "], month = [" + mMonth + "], day = [" + mDay + "], hour = [" + mHour + "], minute = [" + mMinute + "], second = [" + mSecond + "]";
    }
}
