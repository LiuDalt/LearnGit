package com.example.administrator.marquee;

import android.util.TimeUtils;
import android.view.Choreographer;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public final class Marquee {
    // TODO: Add an option to configure this
    private static final float MARQUEE_DELTA_MAX = 0.07f;
    private static final int MARQUEE_DELAY = 1200;
    private static final int MARQUEE_DP_PER_SECOND = 30;

    private static final byte MARQUEE_STOPPED = 0x0;
    private static final byte MARQUEE_STARTING = 0x1;
    private static final byte MARQUEE_RUNNING = 0x2;

    private final WeakReference<TextView> mView;
    private final Choreographer mChoreographer;

    private byte mStatus = MARQUEE_STOPPED;
    private final float mPixelsPerSecond;
    private float mMaxScroll;
    private float mMaxFadeScroll;
    private float mGhostStart;
    private float mGhostOffset;
    private float mFadeStop;
    private int mRepeatLimit;

    private float mScroll;
    private long mLastAnimationMs;

    Marquee(TextView v) {
        final float density = v.getContext().getResources().getDisplayMetrics().density;
        mPixelsPerSecond = MARQUEE_DP_PER_SECOND * density;
        mView = new WeakReference<TextView>(v);
        mChoreographer = Choreographer.getInstance();
    }

    private Choreographer.FrameCallback mTickCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            tick();
        }
    };

    private Choreographer.FrameCallback mStartCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            mStatus = MARQUEE_RUNNING;
//            mLastAnimationMs = mChoreographer.getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
            tick();
        }
    };

    private Choreographer.FrameCallback mRestartCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long frameTimeNanos) {
            if (mStatus == MARQUEE_RUNNING) {
                if (mRepeatLimit >= 0) {
                    mRepeatLimit--;
                }
                start(mRepeatLimit);
            }
        }
    };

    void tick() {
        if (mStatus != MARQUEE_RUNNING) {
            return;
        }

        mChoreographer.removeFrameCallback(mTickCallback);

        final TextView textView = mView.get();
        if (textView != null && (textView.isFocused() || textView.isSelected())) {
//            long currentMs = mChoreographer.getFrameTime();
            long deltaMs = 50;
//            mLastAnimationMs = currentMs;
            float deltaPx = deltaMs / 1000f * mPixelsPerSecond;
            mScroll += deltaPx;
            if (mScroll > mMaxScroll) {
                mScroll = mMaxScroll;
                mChoreographer.postFrameCallbackDelayed(mRestartCallback, MARQUEE_DELAY);
            } else {
                mChoreographer.postFrameCallback(mTickCallback);
            }
            textView.invalidate();
        }
    }

    void stop() {
        mStatus = MARQUEE_STOPPED;
        mChoreographer.removeFrameCallback(mStartCallback);
        mChoreographer.removeFrameCallback(mRestartCallback);
        mChoreographer.removeFrameCallback(mTickCallback);
        resetScroll();
    }

    private void resetScroll() {
        mScroll = 0.0f;
        final TextView textView = mView.get();
        if (textView != null) textView.invalidate();
    }

    void start(int repeatLimit) {
        if (repeatLimit == 0) {
            stop();
            return;
        }
        mRepeatLimit = repeatLimit;
        final TextView textView = mView.get();
        if (textView != null && textView.getLayout() != null) {
            mStatus = MARQUEE_STARTING;
            mScroll = 0.0f;
            final int textWidth = textView.getWidth() - textView.getCompoundPaddingLeft()
                    - textView.getCompoundPaddingRight();
            final float lineWidth = textView.getLayout().getLineWidth(0);
            final float gap = textWidth / 3.0f;
            mGhostStart = lineWidth - textWidth + gap;
            mMaxScroll = mGhostStart + textWidth;
            mGhostOffset = lineWidth + gap;
            mFadeStop = lineWidth + textWidth / 6.0f;
            mMaxFadeScroll = mGhostStart + lineWidth + lineWidth;

            textView.invalidate();
            mChoreographer.postFrameCallback(mStartCallback);
        }
    }

    float getGhostOffset() {
        return mGhostOffset;
    }

    float getScroll() {
        return mScroll;
    }

    float getMaxFadeScroll() {
        return mMaxFadeScroll;
    }

    boolean shouldDrawLeftFade() {
        return mScroll <= mFadeStop;
    }

    boolean shouldDrawGhost() {
        return mStatus == MARQUEE_RUNNING && mScroll > mGhostStart;
    }

    boolean isRunning() {
        return mStatus == MARQUEE_RUNNING;
    }

    boolean isStopped() {
        return mStatus == MARQUEE_STOPPED;
    }
}
