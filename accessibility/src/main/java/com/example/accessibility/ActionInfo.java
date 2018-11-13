package com.example.accessibility;

public class ActionInfo {
    public static final int ACTION_CLICK = 0;
    public static final int ACTION_INPUT = 1;

    public String mResStr;//优先级1 //view资源 id
    public String mText;//优先级2 // view text
    public String mParentResStr;//优先级3 view 父布局
    public String mDescription;//优先级4 view的描述
    public int[] mNodeIndex = null;//优先级5 view的索引
    public String mClassName;//优先级6 view的类名

    public Object mExtra;//参数信息
    public boolean mIsNeedCheckClassName = false;

    public int mActionType = ACTION_CLICK;

    public boolean isClick() {
        return mActionType == ACTION_CLICK;
    }

    public boolean isInput(){
        return mActionType == ACTION_INPUT;
    }

    public static ActionInfo getActionInfo(int state){
        ActionInfo actionInfo = new ActionInfo();
        switch (state){
            case StateConstant.END_STATE:
                break;
            case StateConstant.EXIT_GROUP:
                actionInfo.mClassName = "Button";
                actionInfo.mNodeIndex = new int[]{1, 2};
                break;
            case StateConstant.INPUT_TEXT:
                actionInfo.mResStr = "entry";
                actionInfo.mExtra = "hi everyone!";
                actionInfo.mActionType = ActionInfo.ACTION_INPUT;
                break;
            case StateConstant.INIT_STATE:
                break;
            case StateConstant.SEND_MSG:
                actionInfo.mResStr = "send";
                break;
            case StateConstant.SHOW_GROUP_MENU:
                actionInfo.mDescription = "更多选项";
                actionInfo.mParentResStr = "action_bar_root";
                break;
            case StateConstant.SHOW_MORE_MENU:
                actionInfo.mNodeIndex = new int[]{0, 5};
                break;
            case StateConstant.JOIN_GROUP:
                actionInfo.mResStr = "invite_accept";
                break;
            case StateConstant.SHOW_EXIT_DIALOG:
                actionInfo.mNodeIndex = new int[]{0, 2};
                break;
            default:
                break;

        }
        return actionInfo;
    }
}
