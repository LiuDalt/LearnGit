package com.example.javatest;

import android.util.Log;

public class TestException {
    public static void main(String[] args){
        try {
            Object object = null;
            object.toString();
        } catch (NullPointerException e){
            System.out.println("null " + e);
        } catch (Exception e){
            System.out.println("root " + e);
        }
    }
}
