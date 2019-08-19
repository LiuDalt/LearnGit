package com.example.javatest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestTimee {
    public static void main(String[] args){
        int time = (int) (System.currentTimeMillis() / 100);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        String str = format.format(time);
        System.out.println(str);
    }
}
