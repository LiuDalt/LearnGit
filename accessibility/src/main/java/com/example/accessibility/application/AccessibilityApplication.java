package com.example.accessibility.application;

import android.app.Application;
import android.content.Context;

import com.example.accessibility.data.GroupManager;
import com.example.accessibility.sharepre.SharePreferenceUtils;

public class AccessibilityApplication extends Application {

    public static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        SharePreferenceUtils.init(this);
        GroupManager.getInstance().init();

    }
}
