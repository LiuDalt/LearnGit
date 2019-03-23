package com.example.administrator.marquee;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LiveMarqueeMTextView extends AppCompatTextView {

    private static final long MARQUEE_CHECK = 50;

    private String TAG = "LiveMarqueeMTextView";
    private Runnable mMarqueeRunnable;

    public LiveMarqueeMTextView(Context context) {
        super(context);
    }

    public LiveMarqueeMTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveMarqueeMTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isSelected() {
        return true;
    }

    /**
     *
     * @param times marquee repeat times,  -1 means marquee repeat indefinitely
     * @param listener
     */
    public void startMarquee(int times, MarqueeListener listener){
        removeCallbacks(mMarqueeRunnable);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setFocusable(true);
        setSelected(true);
        setSelectAllOnFocus(true);
        setFocusableInTouchMode(true);
        requestFocus();
        setMarqueeRepeatLimit(times);
        if(start(times)){
            if(listener == null){
                return;
            }
            listener.onStart();
            if(times == -1){
                return;
            }
        } else {
            return;
        }
        mMarqueeRunnable = new Runnable() {
            @Override
            public void run() {
                if(isMarqueeRunning()){
                    postDelayed(this, MARQUEE_CHECK );
                } else {
                    listener.onComplete();
                }
            }
        };
        postDelayed(mMarqueeRunnable, MARQUEE_CHECK);
    }

    private boolean start(int times) {
        try {
            Class<?> textViewClass = this.getClass();
            while (true){
                if(!textViewClass.getSimpleName().equals("TextView")){
                    textViewClass = textViewClass.getSuperclass();
                    if(textViewClass.getSimpleName().equals("View")){
                        return false;
                    }
                } else {
                    break;
                }

            }
            Method start = textViewClass.getDeclaredMethod("startMarquee");
            start.setAccessible(true);
            start.invoke(this);
            return true;
        } catch (Exception e){
            Log.d(TAG, "start exception called " + e);
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        removeCallbacks(mMarqueeRunnable);
    }

    private boolean isMarqueeRunning() {
        try {
            Class<?> textViewClass = this.getClass();
            while (true){
                if(!textViewClass.getSimpleName().equals("TextView")){
                    textViewClass = textViewClass.getSuperclass();
                    if(textViewClass.getSimpleName().equals("View")){
                        return false;
                    }
                } else {
                    break;
                }

            }
            Field marquee = textViewClass.getDeclaredField("mMarquee");
            marquee.setAccessible(true);
            Object mMarquee = marquee.get(this);
            Class<?> marqueeClass = mMarquee.getClass();
            Field status = marqueeClass.getDeclaredField("mStatus");
            status.setAccessible(true);
            byte mStatus = (byte)status.get(mMarquee);
            return mStatus == 2;
        } catch (Exception e){
            Log.d(TAG, "isMarqueeRunning() exception called");
        }
        return false;
    }

    public interface MarqueeListener{
        void onComplete();
        void onStart();
    }
}
