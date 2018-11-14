package com.example.accessibility.application;

import android.app.Application;

import com.example.accessibility.data.GroupManager;

public class AccessibilityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GroupManager.getInstance().init();
    }
}
