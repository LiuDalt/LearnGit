package com.example.administrator.marquee;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MarqueeTextView extends AppCompatTextView {

    private static final long MARQUEE_CHECK = 20;

    private String TAG = "LiveMarqueeMTextView";
    private Runnable mMarqueeRunnable;
    private MarqueeListener mListener;
    private boolean mNeedSelectedForMarquee = true;
    private boolean mHadHookRepeat = false;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean setMarqueeDuration(int timeMs){
        try {
            final int textWidth = getWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
            final float lineWidth = getLayout().getLineWidth(0);
            final float gap = textWidth / 3.0f;
            float mGhostStart = lineWidth - textWidth + gap;
            float mMaxScroll = mGhostStart + textWidth;
            float pixelsPerSecond = mMaxScroll / timeMs / 1000f;
            Class textViewClass = TextView.class;
            Field marqueeField = textViewClass.getDeclaredField("mMarquee");
            marqueeField.setAccessible(true);
            Object mMarquee = marqueeField.get(this);//反射获取TextView 的Marquee 字段
            Class<?> marqueeClass = mMarquee.getClass();
            String fieldString = "mScrollUnit";
            Field pixelsPerSecondFiled = marqueeClass.getDeclaredField(fieldString);
            pixelsPerSecondFiled.setAccessible(true);
            pixelsPerSecondFiled.setFloat(mMarquee, pixelsPerSecond);
            return true;
        } catch (Exception e){
            Log.i(TAG, "setMarqueeDuration call exception " + e);
        }
        return false;
    }

    private boolean hookRepeatCallback(){
        if(mHadHookRepeat){
            return true;
        }
        try {
            Class textViewClass = TextView.class;
            if(textViewClass == null){
                return false;
            }
            Field marqueeField = textViewClass.getDeclaredField("mMarquee");
            marqueeField.setAccessible(true);
            Object mMarquee = marqueeField.get(this);//反射获取TextView 的Marquee 字段
            Class<?> marqueeClass = mMarquee.getClass();
            Field mRestartCallbackField = marqueeClass.getDeclaredField("mRestartCallback");
            mRestartCallbackField.setAccessible(true);
            Field statusField = marqueeClass.getDeclaredField("mStatus");
            statusField.setAccessible(true);
            Field repeatLimitField = marqueeClass.getDeclaredField("mRepeatLimit");
            repeatLimitField.setAccessible(true);
            Method marqueeStartMethod = marqueeClass.getDeclaredMethod("start", int.class);
            marqueeStartMethod.setAccessible(true);
            mRestartCallbackField.set(mMarquee, new Choreographer.FrameCallback(){

                @Override
                public void doFrame(long frameTimeNanos) {
                    try {
                        byte status = statusField.getByte(mMarquee);
                        int repeatLimit = repeatLimitField.getInt(mMarquee);
                        if(status == 2){
                            if(repeatLimit >= 0){
                                repeatLimit--;
                                repeatLimitField.setInt(mMarquee, repeatLimit);
                            }
                            marqueeStartMethod.invoke(mMarquee, repeatLimit);
                            if(mListener != null){
                                mListener.onRepeatMarquee(repeatLimit);
                            }
                            if(repeatLimit == 0 && mListener != null){
                                mListener.onStop();
                                mListener.onComplete();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mHadHookRepeat = true;
            return true;
        } catch (Exception e){
            Log.d(TAG, "hookRepeatCallback() exception called " + e);
        }
        return false;
    }

    public void setListener(MarqueeListener listener) {
        mListener = listener;
    }

    /**
     * 判断是否可以走马灯
     * @return
     */
    public boolean isCanMarquee(){
        try {
            Class<?> textViewClass = TextView.class;
            //通过反射textview的startMarquee方法，使跑马灯再次跑起来
            Method start = textViewClass.getDeclaredMethod("canMarquee");
            start.setAccessible(true);
            return (boolean) start.invoke(this);
        } catch (Exception e){
            Log.d(TAG, "isCanMarquee exception called " + e);
        }
        return false;
    }

    /**
     * 判断是否可以走马灯
     * @return
     */
    public boolean stopMarquee(){
        try {
            mNeedSelectedForMarquee = false;
            Class<?> textViewClass = TextView.class;
            Method start = textViewClass.getDeclaredMethod("stopMarquee");
            start.setAccessible(true);
            start.invoke(this);
            if(mListener != null){
                mListener.onStop();
            }
            return true;
        } catch (Exception e){
            Log.d(TAG, "stopMarquee exception called " + e);
        }
        return false;
    }


    @Override
    public boolean isSelected() {
        return mNeedSelectedForMarquee;//返回true 才会走马灯
    }

    /**
     *走马灯除了下面说的几个条件只外，还需要满足文字的宽度比view的宽度要短
     * @param times marquee repeat times,  -1 means marquee repeat indefinitely
     */
    public void startMarquee(int times, int perDuration){
        removeCallbacks(mMarqueeRunnable);
        //------------------走马灯必备内部条件-----------
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setSelected(true);
        //--------------------------------------
        setMarqueeRepeatLimit(times);
        if(!startMarquee()) {
            return;
        }
        mMarqueeRunnable = new Runnable() {
            @Override
            public void run() {
                if(isMarqueeRunning()){
                    setMarqueeDuration(perDuration);
                    hookRepeatCallback();
                }
            }
        };
        postDelayed(mMarqueeRunnable, MARQUEE_CHECK);
    }

    private boolean startMarquee() {
        try {
            Class<?> textViewClass = TextView.class;
            //通过反射textview的startMarquee方法，使跑马灯再次跑起来
            mNeedSelectedForMarquee = true;
            Method start = textViewClass.getDeclaredMethod("startMarquee");
            start.setAccessible(true);
            start.invoke(this);
            if(mListener != null){
                mListener.onStart();
            }
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

    public boolean isMarqueeRunning() {
        try {
            Class textViewClass = getClass().getSuperclass().getSuperclass();
            if(textViewClass == null){
                return false;
            }
            Field marquee = textViewClass.getDeclaredField("mMarquee");
            marquee.setAccessible(true);
            Object mMarquee = marquee.get(this);//反射获取TextView 的Marquee 字段
            Class<?> marqueeClass = mMarquee.getClass();
            Field status = marqueeClass.getDeclaredField("mStatus");
            status.setAccessible(true);
            byte mStatus = (byte)status.get(mMarquee);//反射获取Marquee中的mStatus字段
            return mStatus == 2; //当mStatus=2表示走马灯正在运行，mStatus=0表示已经停止走马灯
        } catch (Exception e){
            Log.d(TAG, "isMarqueeRunning() exception called " + e);
        }
        return false;
    }

    public interface MarqueeListener{
        void onComplete();
        void onStart();
        void onStop();
        void onRepeatMarquee(int remainRepeatTimes);
    }
}
