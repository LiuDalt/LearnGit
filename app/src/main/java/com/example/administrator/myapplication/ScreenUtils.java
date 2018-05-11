package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

public class ScreenUtils {
    public static class Screen{
        public int mWidth;
        public int mHeight;

        public Screen(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        @Override
        public String toString() {
            return "width x height = " +mWidth + " x " + mHeight;
        }
    }

    /**
     * 已经不推荐使用此种方法
     * @param context
     * @return
     */
    public static Screen getScreenWithDisplay(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return new Screen(display.getWidth(), display.getHeight());
    }

    /**
     * 推荐
     * @param context
     * @return
     */
    public static Screen getScreenWithSize(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return new Screen(point.x, point.y);
    }

    /**
     * 推荐
     * @param context
     * @return
     */
    public static Screen getScreenWithMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        return new Screen(dm.widthPixels, dm.heightPixels);
    }

    public static Screen getRealScreenWithMetrics(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics =new DisplayMetrics();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(metrics);
            return new Screen(metrics.widthPixels, metrics.heightPixels);
        }else{
            return getScreenWithMetrics(context);
        }
    }

    public static Screen getRealScreenWithSize(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
            return new Screen(point.x, point.y);
        }else{
            return getScreenWithSize(context);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToSp(float pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    //沉浸式隐藏导航栏 api > 14
    public static void hideNavigation(Activity context){
        View decorView = context.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static int getStatusBarHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
            }
        }
        return statusHeight;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme()
                .resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources()
                            .getDisplayMetrics());
        }
        return 0;
    }


    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     *
     * @param dark 状态栏字体和图标是否为深色
     */
    public static void setStatusBarFontDark(Window window, boolean dark) {
        // 小米MIUI
        if (RomUtils.isMIUI()) {
            try {
                Class clazz = window.getClass();
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {    //状态栏亮色且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {       //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 魅族FlymeUI
        if (RomUtils.isFlyme()) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // android6.0+系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    public static boolean copyToClipBoard(Context context, String text) {
        try {
            Object service = context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager cb = (android.text.ClipboardManager) service;
                cb.setText(text);
            } else {
                android.content.ClipboardManager cb = (android.content.ClipboardManager) service;
                cb.setText(text);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getMaxCpuFreq() {
        String result = "";
        BufferedReader cpuReader = null;
        try {
            cpuReader = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            result = cpuReader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (cpuReader != null) {
                try {
                    cpuReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (pattern.matcher(result).matches()) {
            if (result.length() > 32) {
                return 0xFFFFFFFF;
            }
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }


    public static int getScreenRotationAngle(Activity activity) {
        int rotation;
        if (Build.VERSION.SDK_INT > 7) {
            rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        } else {
            rotation = activity.getWindowManager().getDefaultDisplay().getOrientation();
        }

        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    public static String getClipboardContent(Context context) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager cm = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null && cm.getText() != null) {
                return cm.getText().toString();
            }
        }else{
            android.content.ClipboardManager cm = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                ClipData cd = cm.getPrimaryClip();
                if (cd != null && cd.getItemCount() > 0) {
                    if (cd.getItemAt(0) != null && cd.getItemAt(0).getText() != null) {
                        return cd.getItemAt(0).getText().toString();
                    }
                }
            }
        }
        return null;
    }
}
