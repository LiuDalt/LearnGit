package com.example.administrator.marquee;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.myapplication.R;

@SuppressLint("AppCompatCustomView")
public class LiveMarqueeTextView extends LinearLayout {

    private static final String TAG = "LiveMarqueeTextView";
    private static final int INFINITE = -1;
    private ValueAnimator mAnimator;
    private TextView mTextView;
    private int mMarqueeTimesOnCreate = 0;

    public LiveMarqueeTextView(Context context) {
        super(context);
        init(null);
    }

    public LiveMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LiveMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attrs){
        setOrientation(HORIZONTAL);
        mTextView = new TextView(getContext()){

            @Override
            public boolean isSelected() {
                return true;
            }

            @Override
            protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
                super.onFocusChanged(focused, direction, previouslyFocusedRect);
            }
        };
        mTextView.setPadding(0, 0, 0, 0);
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextView.setLayoutParams(params);
        params.gravity = Gravity.CENTER;
        mTextView.setGravity(Gravity.CENTER);
        addView(mTextView);

        if(attrs != null){
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LiveMarqueeTextView);
            mMarqueeTimesOnCreate = ta.getInt(R.styleable.LiveMarqueeTextView_marqueeTimesOnCreate, INFINITE);
            String text = ta.getString(R.styleable.LiveMarqueeTextView_text);
            if(!TextUtils.isEmpty(text)) {
                mTextView.setText(text);
            }
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ta.getDimensionPixelSize(R.styleable.LiveMarqueeTextView_marqueeTextSize, 20));
            mTextView.setTextColor(ta.getColor(R.styleable.LiveMarqueeTextView_marqueeTextColor, Color.BLACK));
            ta.recycle();
        }
        mTextView.setSingleLine();
        mTextView.setFocusable(true);
        mTextView.setFocusableInTouchMode(true);
        mTextView.setClickable(true);
        mTextView.setFadingEdgeLength(0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mMarqueeTimesOnCreate != 0) {
            startMarquee(mMarqueeTimesOnCreate, null);
        }
    }

    /**
     * SP
     * @param size
     */
    public void setTextSize(float size){
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTextColor(int color){
        mTextView.setTextColor(color);
    }

    private void updateWidth() {
        if(TextUtils.isEmpty(getText())){
            return;
        }
        String text = getText().toString();
        TextPaint paint = mTextView.getPaint();
        int textW = (int) paint.measureText(text);
        if(mTextView.getLayoutParams() != null) {
            mTextView.getLayoutParams().width = textW;
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        if(params != null){
            params.width = textW + getPaddingLeft() + getPaddingRight();
            setLayoutParams(params);
        }
    }

    public CharSequence getText() {
        CharSequence sequence = mTextView.getText();
        if(sequence != null && sequence.length() > 2 && isMarquee()){
            return sequence.subSequence(0, sequence.length() -1);
        }
        return mTextView.getText();
    }

    public void setText(String text) {
        setText(text, true);
    }

    public void setText(String text, boolean needUpdateWidth) {
        mTextView.setText(text);
        if(needUpdateWidth) {
            updateWidth();
        }
    }

    private void endAnimation() {
        if(mAnimator != null && mAnimator.isRunning()){
            mAnimator.end();
        }
    }

    public void startMarquee(int times, MarqueeAnimatorListener animatorListener){
        startAnimation(5000, new UpdateListener() {
            @Override
            public void onUpdate(float progress) {
                float width = mTextView.getPaint().measureText(mTextView.getText().toString());
                float dstX;
                if (progress <= 0.5f) {
                    dstX = -progress * width * 2;
                } else {
                    dstX = (1 - progress) * 2 * width;
                }
                mTextView.setX(dstX + getPaddingLeft());
                Log.i(TAG, "startMarquee:dstX=" + dstX + " progress=" + progress);
            }
        }, animatorListener, times);
    }

    private void initAnimator() {
        if(mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(0, 200);
            mAnimator.setInterpolator(new LinearInterpolator());
        }
    }

    public boolean isMarquee(){
        return mTextView.getEllipsize() == TextUtils.TruncateAt.MARQUEE;
    }

    public void startAnimation(int duration, final UpdateListener updateListener,
                               final Animator.AnimatorListener animationListener){
        startAnimation(duration, updateListener, animationListener, 0);
    }

    public void startAnimation(int duration, final UpdateListener updateListener,
                               final Animator.AnimatorListener animationListener, int animateTimes){
        if(duration <= 0){
            return;
        }
        if(updateListener == null){
            return;
        }
        initAnimator();
        if(mAnimator.isRunning()){
            endAnimation();
        }
        mAnimator.setDuration(duration);
        if(animateTimes > 1) {
            mAnimator.setRepeatCount(animateTimes - 1);
        }
        if(animateTimes == INFINITE){
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                updateListener.onUpdate(value / 200.0f);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationStart(animation);
                }
                Log.d(TAG, "onAnimationStart() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationEnd(animation);
                }
                mAnimator.removeAllListeners();
                mAnimator.removeAllUpdateListeners();
                mAnimator.setRepeatCount(0);
                mAnimator.setDuration(0);
                Log.d(TAG, "onAnimationEnd() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationCancel(animation);
                }
                Log.d(TAG, "onAnimationCancel() called with: animation = [" + animation + "]");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animationListener != null) {
                    animationListener.onAnimationRepeat(animation);
                }
                Log.d(TAG, "onAnimationRepeat() called with: animation = [" + animation + "]");
            }
        });
        mAnimator.start();
    }

    public interface UpdateListener {
        void onUpdate(float progress);
    }
}
