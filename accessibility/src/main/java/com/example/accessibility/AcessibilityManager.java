package com.example.accessibility;

import android.util.Log;

public class AcessibilityManager {
    private static final long WORK_DURATION = 100;
    private OperateState mState;
    private String TAG = "AcessibilityManager";
    private boolean mWorkable = true;
    private long mLastWorkTime = 0;

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

    public void start(int state){
        mState = new OperateState();
        mState.setState(state);
    }

    public void next(){

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

    public void work() {
        if(System.currentTimeMillis() - mLastWorkTime < WORK_DURATION){
            return;
        }
        boolean hadWork = false;
        if(getState() == StateConstant.INIT_STATE){
            if(!mWorkable){
                return;
            }
            start(StateConstant.INPUT_TEXT);
        } else if(getState() == StateConstant.JOIN_GROUP){
            if(AccessibilityUtils.performClick(mService, "invite_accept")){
                Log.i(TAG, "joinGroup-----");
                mState.setState(StateConstant.INPUT_TEXT);
                hadWork = true;
            }
        }
        else if(getState() == StateConstant.INPUT_TEXT){
            if(AccessibilityUtils.performInputText(mService, "entry", "hi everyone!")){
                mState.setState(StateConstant.SEND_MSG);
                Log.i(TAG, "inputMsg-----");
                hadWork = true;
            }
        }else if(getState() == StateConstant.SEND_MSG){
            if(AccessibilityUtils.performClick(mService, "send")){
                Log.i(TAG, "sendMsg-----");
                mState.setState(StateConstant.INIT_STATE);
                mWorkable = false;
                hadWork = true;
            }
        }
        if(hadWork){
            mLastWorkTime = System.currentTimeMillis();
        }
    }

    public void reset() {
        mWorkable = true;
    }
}
