package com.example.accessibility;

public class OperateState {
    private int mState = StateConstant.INIT_STATE;
    private OperateState mNextState;

    public int getState() {
        return mState;
    }

    public OperateState getNextState() {
        return mNextState;
    }

    public void setState(int state) {
        mState = state;
    }

    public void setNextState(OperateState nextState) {
        mNextState = nextState;
    }
}
