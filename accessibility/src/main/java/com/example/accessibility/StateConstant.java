package com.example.accessibility;

public class StateConstant {
    public final static int JOIN_GROUP = 0;
    public final static int INPUT_TEXT = JOIN_GROUP + 1;
    public final static int SEND_MSG = INPUT_TEXT + 1;
    public final static int SHOW_GROUP_MENU = SEND_MSG + 1;
    public final static int SHOW_MORE_MENU = SHOW_GROUP_MENU + 1;
    public final static int EXIT_GROUP = SHOW_MORE_MENU + 1;
    public static final int INIT_STATE = EXIT_GROUP + 1;
    public static final int END_STATE = INIT_STATE + 1;
}
