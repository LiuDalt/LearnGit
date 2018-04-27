package com.example.administrator.myapplication;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class MyApplication extends Application {
    public static RefWatcher sRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();

        sRefWatcher = LeakCanary.install(this);
    }
}
