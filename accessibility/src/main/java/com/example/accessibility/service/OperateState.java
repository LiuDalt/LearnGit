package com.example.accessibility.service;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class OperateState {
    private int mState = StateConstant.INIT_STATE;
    private ActionInfo mActionInfo;
    private int mDuraiton = WAAccessibilityManager.UNIT_OPERATE_DURATION;
    private List<OperateState> mNextHitState;

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        setState(state, false);
    }

    public void setState(int state, boolean needSetNextHitState) {
        mState = state;
        mActionInfo = ActionInfo.getActionInfo(state);
        if(needSetNextHitState){
            initNextHitState();
        }
    }

    public void initNextHitState(){
        mNextHitState = obtainNextHitState(mState);
    }

    /**
     *END_STATE 和其他STATE不能同时出现，所以包含END_STATE的nextStateList只能有一个
     * @param state
     * @return
     */
    public static List<OperateState> obtainNextHitState(int state) {
        List<OperateState> hitStates = new ArrayList<>();
        if(state == StateConstant.INIT_STATE){
            hitStates.add(obtainState(StateConstant.JOIN_GROUP));
            hitStates.add(obtainState(StateConstant.INPUT_TEXT));
            hitStates.add(obtainState(StateConstant.ONLY_MANAGER_SEND_MSG));
            hitStates.add(obtainState(StateConstant.GROUP_FULL_OR_INVALID));
        }else if(state == StateConstant.JOIN_GROUP){
            hitStates.add(obtainState(StateConstant.GROUP_FULL_OR_INVALID));
            hitStates.add(obtainState(StateConstant.INPUT_TEXT));
            hitStates.add(obtainState(StateConstant.ONLY_MANAGER_SEND_MSG));
            hitStates.add(obtainState(StateConstant.JOIN_GROUP));
        }else if(state == StateConstant.GROUP_FULL_OR_INVALID){
            hitStates.add(obtainState(StateConstant.END_STATE));
        }else if(state == StateConstant.INPUT_TEXT){
            hitStates.add(obtainState(StateConstant.SEND_MSG));
            hitStates.add(obtainState(StateConstant.INPUT_TEXT));
        }else if(state == StateConstant.SEND_MSG){
            hitStates.add(obtainState(StateConstant.ALREADY_MSG_SENDED));
            hitStates.add(obtainState(StateConstant.SEND_MSG));
        }else if(state == StateConstant.SHOW_GROUP_MENU){
            hitStates.add(obtainState(StateConstant.SHOW_MORE_MENU));
            hitStates.add(obtainState(StateConstant.SHOW_GROUP_MENU));
        }else if(state == StateConstant.SHOW_MORE_MENU){
            hitStates.add(obtainState(StateConstant.SHOW_EXIT_DIALOG));
            hitStates.add(obtainState(StateConstant.SHOW_MORE_MENU));
        }else if(state == StateConstant.SHOW_EXIT_DIALOG){
            hitStates.add(obtainState(StateConstant.EXIT_GROUP));
            hitStates.add(obtainState(StateConstant.SHOW_EXIT_DIALOG));
        } else if(state == StateConstant.EXIT_GROUP){
            hitStates.add(obtainState(StateConstant.END_STATE));
            hitStates.add(obtainState(StateConstant.EXIT_GROUP));
        } else if(state == StateConstant.ONLY_MANAGER_SEND_MSG){
            hitStates.add(obtainState(StateConstant.SHOW_GROUP_MENU));
            hitStates.add(obtainState(StateConstant.ONLY_MANAGER_SEND_MSG));
        }else if(state == StateConstant.ALREADY_MSG_SENDED){
            hitStates.add(obtainState(StateConstant.SHOW_GROUP_MENU));
        }
        return hitStates;
    }

    /**
     *END_STATE 和其他STATE不能同时出现，所以包含END_STATE的nextStateList只能有一个
     * @param state
     * @return
     */
    public static List<OperateState> obtainNextHitStateNoExitForSend(int state) {
        List<OperateState> hitStates = new ArrayList<>();
        if(state == StateConstant.INIT_STATE){
            hitStates.add(obtainState(StateConstant.JOIN_GROUP));
            hitStates.add(obtainState(StateConstant.INPUT_TEXT));
            hitStates.add(obtainState(StateConstant.ONLY_MANAGER_SEND_MSG));
            hitStates.add(obtainState(StateConstant.GROUP_FULL_OR_INVALID));
        }else if(state == StateConstant.JOIN_GROUP){
            hitStates.add(obtainState(StateConstant.GROUP_FULL_OR_INVALID));
            hitStates.add(obtainState(StateConstant.INPUT_TEXT));
            hitStates.add(obtainState(StateConstant.ONLY_MANAGER_SEND_MSG));
            hitStates.add(obtainState(StateConstant.JOIN_GROUP));
        }else if(state == StateConstant.GROUP_FULL_OR_INVALID){
            hitStates.add(obtainState(StateConstant.END_STATE));
        }else if(state == StateConstant.INPUT_TEXT){
            hitStates.add(obtainState(StateConstant.SEND_MSG));
        }else if(state == StateConstant.SEND_MSG){
//            hitStates.add(obtainState(StateConstant.SHOW_GROUP_MENU));
            hitStates.add(obtainState(StateConstant.END_STATE));
        }else if(state == StateConstant.SHOW_GROUP_MENU){
            hitStates.add(obtainState(StateConstant.SHOW_MORE_MENU));
        }else if(state == StateConstant.SHOW_MORE_MENU){
            hitStates.add(obtainState(StateConstant.SHOW_EXIT_DIALOG));
        }else if(state == StateConstant.SHOW_EXIT_DIALOG){
            hitStates.add(obtainState(StateConstant.EXIT_GROUP));
        } else if(state == StateConstant.EXIT_GROUP){
            hitStates.add(obtainState(StateConstant.END_STATE));
        } else if(state == StateConstant.ONLY_MANAGER_SEND_MSG){
            hitStates.add(obtainState(StateConstant.SHOW_GROUP_MENU));
        }
        return hitStates;
    }

    private static OperateState obtainState(int state) {
        OperateState operateState = new OperateState();
        operateState.setState(state);
//        switch (state){
//            case StateConstant.INIT_STATE:
//                operateState.setState(StateConstant.INIT_STATE);
//                break;
//            case StateConstant.JOIN_GROUP:
//                operateState.setState(StateConstant.JOIN_GROUP);
//                break;
//            case StateConstant.INPUT_TEXT:
//                operateState.setState(StateConstant.INPUT_TEXT);
//                break;
//            case StateConstant.GROUP_FULL_OR_INVALID:
//                operateState.setState(StateConstant.GROUP_FULL_OR_INVALID);
//                break;
//            case StateConstant.SEND_MSG:
//                operateState.setDuraiton(WAAccessibilityManager.SEND_MSG_OPERATE_DURATION);
//                operateState.setState(StateConstant.SEND_MSG);
//                break;
//            case StateConstant.SHOW_GROUP_MENU:
//                operateState.setState(StateConstant.SHOW_GROUP_MENU);
//                break;
//            case StateConstant.SHOW_MORE_MENU:
//                operateState.setState(StateConstant.SHOW_MORE_MENU);
//                break;
//            case StateConstant.SHOW_EXIT_DIALOG:
//                operateState.setState(StateConstant.SHOW_EXIT_DIALOG);
//                break;
//            case StateConstant.EXIT_GROUP:
//                operateState.setState(StateConstant.EXIT_GROUP);
//                break;
//            case StateConstant.ONLY_MANAGER_SEND_MSG:
//                operateState.setState(StateConstant.ONLY_MANAGER_SEND_MSG);
//                break;
//            case StateConstant.END_STATE:
//                operateState.setState(StateConstant.END_STATE);
//                break;
//            case StateConstant.NO_CAMERA_DIALOG:
//                operateState.setState(StateConstant.NO_CAMERA_DIALOG);
//                break;
//            case StateConstant.ALREADY_MSG_SENDED:
//                operateState.setState(StateConstant.ALREADY_MSG_SENDED);
//                break;
//        }
        return operateState;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public OperateState hitNext(WAAccessibilityService service){
        for(OperateState operateState : mNextHitState){
            if(operateState.isEnd()){
                return operateState;
            }
            if(AccessibilityUtils.performAction(service, operateState.mActionInfo)){
                return operateState;
            }
            if(AccessibilityUtils.performAction(service, obtainState(StateConstant.NO_CAMERA_DIALOG).mActionInfo)){
                return null;
            }
        }
        return null;
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
            case StateConstant.GROUP_FULL_OR_INVALID:
                return "group_full";
            case StateConstant.ONLY_MANAGER_SEND_MSG:
                return "only_manager_send_msg";
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

    public void setEnd() {
        setState(StateConstant.END_STATE);
    }
}
