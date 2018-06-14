package com.example.administrator.myapplication;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


public class PackageUtils {
    private static volatile String sVersionName = "";

    /**
     * 获取版本名字
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        if (TextUtils.isEmpty(sVersionName)) {
            PackageInfo pi;
            try {
                pi = getPackageManager(context).getPackageInfo(getPackageName(context), PackageManager.GET_CONFIGURATIONS);
                sVersionName = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
            }

        }

        return sVersionName;
    }

    private static volatile int sVersionCode = 0;

    /**
     * 获取版本号
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        if (sVersionCode == 0) {
            try {
                PackageInfo pi = getPackageManager(context).getPackageInfo(getPackageName(context), PackageManager.GET_CONFIGURATIONS);
                sVersionCode = pi.versionCode;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sVersionCode;
    }

    public static PackageManager getPackageManager(Context context) {
        return context.getPackageManager();
    }

    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 是否首次安装
     * @param context
     * @return
     */
    public static boolean isFirstInstall( Context context) {
        if(context == null) {
            return false;
        }
        try {
            PackageManager mg = context.getPackageManager();
            PackageInfo info = mg.getPackageInfo(context.getPackageName(), 0);
            return info.firstInstallTime == info.lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static volatile String sChannel = "";

    /**
     * 获取渠道信息
     * @param context
     * @return
     */

    public static String getChannel(Context context) {
        if (TextUtils.isEmpty(sChannel)) {
            try {
                ApplicationInfo ai = getPackageManager(context).getApplicationInfo(
                        getPackageName(context), PackageManager.GET_META_DATA);
                sChannel = (String) ai.metaData.get("APP_CHANNEL");
            } catch (Exception e) {
            }
        }
        return sChannel;
    }
}
