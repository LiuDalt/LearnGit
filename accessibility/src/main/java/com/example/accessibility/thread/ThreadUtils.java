package com.example.accessibility.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    private static final Handler sHandlder = new Handler(Looper.getMainLooper());
    private static ExecutorService sCachedThreadPool = Executors.newCachedThreadPool();
    private static ScheduledExecutorService sScheduledThreadPool = Executors.newScheduledThreadPool(3);

    public static void runOnUiThread(Runnable runnable){
        if(runnable == null){
            return;
        }
        sHandlder.post(runnable);
    }

    /**
     *
     * @param runnable
     * @param duration 毫秒
     */
    public static void runOnUiTHreadFixRate(final Runnable runnable, final int duration){
        if(runnable == null){
            return;
        }
        sHandlder.post(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                sHandlder.postDelayed(this, duration);
            }
        });
    }

    public static void removeUiRunnable(Runnable r){
        sHandlder.removeCallbacks(r);
    }

    /**
     *
     * @param runnable
     * @param delay 毫秒
     */
    public static void runOnUiThread(Runnable runnable, int delay){
        if(runnable == null){
            return;
        }
        sHandlder.postDelayed(runnable, delay);
    }

    public static void runOnBackgroundThread(Runnable runnable){
        if(runnable == null){
            return;
        }
        sCachedThreadPool.submit(runnable);
    }

    /**
     *
     * @param runnable
     * @param delay 毫秒
     */
    public static void runOnBackgroundThread(Runnable runnable, int delay){
        if(runnable == null){
            return;
        }
        sScheduledThreadPool.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     *
     * @param runnable
     * @param duration 毫秒
     */
    public static void runOnBackgroundThreadFixRate(Runnable runnable, int duration){
        if(runnable == null){
            return;
        }
        sScheduledThreadPool.scheduleAtFixedRate(runnable, 0, duration, TimeUnit.MILLISECONDS);
    }
}
