package com.example.accessibility;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class AcessibilityManager {
    public static final int WORK_DURATION = 1000;
    public static final int SEND_MSG_DURATION = WORK_DURATION * 5;
    private final Handler mHandler;
    private OperateState mState;
    private String TAG = "AcessibilityManager";
    private boolean mWorkable = true;
    private boolean mWorkStarted = false;
    private long mLastWorkTime = 0;
    private HandlerThread mHandlerThread;
    private static AcessibilityManager sManager;
    private MyAccessibilityService mService;

    public static AcessibilityManager getInstance() {
        if(sManager == null){
            synchronized (AcessibilityManager.class){
                if(sManager == null){
                    sManager = new AcessibilityManager();
                }
            }
        }
        return sManager;
    }

    private AcessibilityManager() {
        mHandlerThread = new HandlerThread("TaskThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if(mWorkable){
                    work();
                }
            }
        };
    }

    public void start(){
        if(mWorkStarted || !mWorkable){
            return;
        }
        start(StateConstant.JOIN_GROUP);
    }

    private void start(int state){
        mState = new OperateState();
        mState.setState(state);
        mWorkStarted = true;
        mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        Log.d(TAG, "start() called with: state = [" + mState.getStateStr() + "]");
    }

    public int getState(){
        if(mState == null){
            return StateConstant.INIT_STATE;
        }
        return mState.getState();
    }

    public void setService(MyAccessibilityService myAccessibilityService) {
        mService = myAccessibilityService;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean work() {
        if(!mWorkable || !mWorkStarted){
            return false;
        }
        boolean hadWork = false;
        if(mState.perform(mService)){
            hadWork = true;
            mState.next();
        }
        if(hadWork){
            mLastWorkTime = System.currentTimeMillis();
            Log.d(TAG, "work() called " + mState.getStateStr() + " end");
        }
        if(mState.isEnd()){
            reset();
        }
        mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        return hadWork;
    }

    public void reset() {
        mWorkable = false;
        mWorkStarted = false;
    }

    public void enableWork(boolean enable){
        mWorkable = enable;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void destroy() {
        mHandlerThread.quitSafely();
    }
}
