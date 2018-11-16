package com.example.accessibility.service;

/**
 * 当前流程的步骤状态
 */
public class StateConstant {
    public final static int JOIN_GROUP = 0;//加群操作
    public final static int INPUT_TEXT = JOIN_GROUP + 1;//输入文字
    public final static int SEND_MSG = INPUT_TEXT + 1;//发送信息
    public final static int SHOW_GROUP_MENU = SEND_MSG + 1;//打开群菜单
    public final static int SHOW_MORE_MENU = SHOW_GROUP_MENU + 1;//打开更多菜单
    public final static int SHOW_EXIT_DIALOG = SHOW_MORE_MENU + 1;//弹出退群弹窗
    public final static int EXIT_GROUP = SHOW_EXIT_DIALOG + 1;//退出群
    public static final int INIT_STATE = EXIT_GROUP + 1;//初始状态
    public static final int END_STATE = INIT_STATE + 1;//结束状态
    public static final int GROUP_FULL_OR_INVALID = END_STATE + 1;//群满操作，点击确定消失弹窗  群链接被取消，无法加群 例如https://chat.whatsapp.com/KwKkanzkw5aHZ6cv9S917Y
    public static final int ONLY_MANAGER_SEND_MSG = GROUP_FULL_OR_INVALID + 1;//只有管理员发送消息
    public static final int NO_CAMERA_DIALOG = ONLY_MANAGER_SEND_MSG + 1;
}
