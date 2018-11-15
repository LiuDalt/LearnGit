package com.example.accessibility.time;

import android.util.Log;

import java.util.Calendar;

public class TimeUtils {
    public static Time getCurrTime(){
        Time time = new Time();
        Calendar calendar = Calendar.getInstance();
        time.mYear = calendar.get(Calendar.YEAR);
        time.mMonth = calendar.get(Calendar.MONTH)+1;
        time.mDay = calendar.get(Calendar.DAY_OF_MONTH);
        time.mHour = calendar.get(Calendar.HOUR_OF_DAY);
        time.mMinute = calendar.get(Calendar.MINUTE);
        time.mSecond = calendar.get(Calendar.SECOND);
        return time;
    }
}
