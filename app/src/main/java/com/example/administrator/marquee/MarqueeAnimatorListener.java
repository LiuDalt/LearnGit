package com.example.administrator.marquee;

import android.animation.Animator;

public abstract class MarqueeAnimatorListener implements Animator.AnimatorListener {

    private int mCurrTime = 0;
    private int mMaxTime;
    public MarqueeAnimatorListener(int maxTimes){
        mMaxTime = maxTimes;
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animator animation, boolean isReverse) {

    }


    @Override
    public void onAnimationEnd(Animator animation, boolean isReverse) {

    }

    @Override
    public void onAnimationStart(Animator animation) {
        mCurrTime = 1;
        onAnimationTime(animation, mCurrTime, mMaxTime);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
        mCurrTime++;
        onAnimationTime(animation, mCurrTime, mMaxTime);
    }
    public abstract void onAnimationTime(Animator animation, int time, int maxTimes);
}
