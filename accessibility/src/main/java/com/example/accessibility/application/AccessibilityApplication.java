package com.example.accessibility.application;

import android.app.Application;

import com.example.accessibility.data.GroupManager;
import com.example.accessibility.sharepre.SharePreferenceUtils;

public class AccessibilityApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharePreferenceUtils.init(this);
        GroupManager.getInstance().init();

    }
}
