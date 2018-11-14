package com.example.accessibility.service;

import android.os.Build;
import android.support.annotation.RequiresApi;

public class OperateState {
    private int mState = StateConstant.INIT_STATE;
    private ActionInfo mActionInfo;
    private int mDuraiton = WAAccessibilityManager.UNIT_OPERATE_DURATION;

    public int getState() {
        return mState;
    }


    public void setState(int state) {
        mState = state;
        mActionInfo = ActionInfo.getActionInfo(state);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean perform(WAAccessibilityService service){
        return AccessibilityUtils.performAction(service, getActionInfo());
    }

    public void next(){
        if(getState() == StateConstant.INIT_STATE){
            setState(StateConstant.JOIN_GROUP);
        } else if(getState() == StateConstant.JOIN_GROUP){
            setState(StateConstant.INPUT_TEXT);
        } else if(getState() == StateConstant.INPUT_TEXT){
            setState(StateConstant.SEND_MSG);
        }else if(getState() == StateConstant.SEND_MSG){
            setDuraiton(WAAccessibilityManager.SEND_MSG_OPERATE_DURATION);
            setState(StateConstant.SHOW_GROUP_MENU);
        }else if(getState() == StateConstant.SHOW_GROUP_MENU){
            setDuraiton(WAAccessibilityManager.UNIT_OPERATE_DURATION);
            setState(StateConstant.SHOW_MORE_MENU);
        }else if(getState() == StateConstant.SHOW_MORE_MENU){
            setState(StateConstant.SHOW_EXIT_DIALOG);
        } else if(getState() == StateConstant.SHOW_EXIT_DIALOG){
            setState(StateConstant.EXIT_GROUP);
        }else if(getState() == StateConstant.EXIT_GROUP){
            setState(StateConstant.END_STATE);
        }
    }

    public ActionInfo getActionInfo() {
        return mActionInfo;
    }

    public String getStateStr() {
        switch (mState){
            case StateConstant.END_STATE:
                return "endState";
            case StateConstant.EXIT_GROUP:
                return "exitGroup";
            case StateConstant.INPUT_TEXT:
                return "inputText";
            case StateConstant.INIT_STATE:
                return "initState";
            case StateConstant.SEND_MSG:
                return "sendMsg";
            case StateConstant.SHOW_GROUP_MENU:
                return "showGroupMenu";
            case StateConstant.SHOW_MORE_MENU:
                return "showMoreMenu";
            case StateConstant.JOIN_GROUP:
                return "joinGroup";
            case StateConstant.SHOW_EXIT_DIALOG:
                return "showExitDialog";
            default:
                return "unkonwState";

        }
    }

    public void setDuraiton(int duraiton) {
        mDuraiton = duraiton;
    }

    public long getDuration() {
        return mDuraiton;
    }

    public boolean isEnd() {
        return mState == StateConstant.END_STATE;
    }
}
