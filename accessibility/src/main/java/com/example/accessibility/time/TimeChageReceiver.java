package com.example.accessibility.time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.accessibility.service.WAAccessibilityManager;

public class TimeChageReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_TIME_TICK:
                //每过一分钟
            case Intent.ACTION_TIME_CHANGED:
                //系统时间修改
            case Intent.ACTION_TIMEZONE_CHANGED://系统时区变化
                Log.i("TimeChageReceiver", "TimeChageReceiver=" + intent.getAction());
                WAAccessibilityManager.getInstance().checkStartWhatsApp();
                break;
        }
    }
}
