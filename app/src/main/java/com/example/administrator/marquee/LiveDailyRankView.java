package com.example.administrator.marquee;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import static android.graphics.drawable.GradientDrawable.RECTANGLE;

public class LiveDailyRankView extends FrameLayout {
    private static String TAG = "LiveDailyRankView";

    private TextView mTextView;
    private ValueAnimator mAnimator;
    private View mNormalBgView;
    private GradientDrawable mNormalBgDrawable;
    private View mMarqueeBgView;
    private GradientDrawable mMarqueeDrawable;
    private FrameLayout.LayoutParams mNormalBgViewParams;
    private FrameLayout.LayoutParams mMarqueeBgViewParams;
    private ViewGroup.LayoutParams mParams;
    private int mInitWidth;
    private int mMaxWidth;
    private int mMarqueeTextColor;
    private String mNormalText;
    private String mMarqueeText;
    private int mNormalTextColor;
    private FrameLayout.LayoutParams mTextViewParams;
    private FrameLayout mTextViewLayout;
    private float mStartTvX;
    private int mStartTvLeft;
    private boolean mIsRankAnimationRunning = false;
    private boolean mIsIncomeAnimationRunning = false;
    private String mIncomePostText;
    private int mStartIncome;
    private int mEndIncome;

    public LiveDailyRankView(Context context) {
        super(context);
        init(null);
    }

    public LiveDailyRankView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LiveDailyRankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mMarqueeDrawable.setCornerRadius(getHeight() / 2);
                mNormalBgDrawable.setCornerRadius(getHeight() / 2);
                mMarqueeBgView.setBackground(mMarqueeDrawable);
                mNormalBgView.setBackground(mNormalBgDrawable);

                FrameLayout.LayoutParams params = (LayoutParams) mTextViewLayout.getLayoutParams();
                params.leftMargin = getHeight() / 2;
                params.rightMargin = getHeight() / 2;
                mTextViewLayout.setLayoutParams(params);

                mTextViewParams = (LayoutParams) mTextView.getLayoutParams();

                mParams = getLayoutParams();
                mInitWidth = getWidth();
                mMaxWidth = getResources().getDimensionPixelSize(R.dimen.live_daily_rank_max_width);
            }
        });
    }

    private void init(AttributeSet attrs) {
        mMarqueeTextColor = getResources().getColor(R.color.live_daily_marquee_tv_color);
        addNormalBgView();
        addMarqueeBgView();
        addTextView();
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.LiveMarqueeTextView);
        if(ta != null) {
            mNormalTextColor = ta.getColor(R.styleable.LiveMarqueeTextView_marqueeTextColor,
                    getResources().getColor(R.color.live_daily_rank_normal_tv_color));
            mTextView.setTextColor(mNormalTextColor);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ta.getDimensionPixelSize(R.styleable.LiveMarqueeTextView_marqueeTextSize,
                    getResources().getDimensionPixelSize(R.dimen.sp11)));
            ta.recycle();
        }
        setNormalText("Daily Top.29");
        setMarqueeText("1110,000 beans from 10th 10,000 beans from 10th");
        setIncomeData(5000, 6000, " beans");
    }

    public void setNormalText(String normalText){
        mNormalText = normalText;
        mTextView.setText(mNormalText);
    }

    public void setMarqueeText(String marqueeText) {
        mMarqueeText = marqueeText;
    }

    private void addMarqueeBgView() {
        mMarqueeBgView = new View(getContext());
        mMarqueeBgViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMarqueeBgView.setLayoutParams(mMarqueeBgViewParams);
        int colors[] = { getResources().getColor(R.color.live_daily_rank_marquee_start_corlor),
                getResources().getColor(R.color.live_daily_rank_marquee_end_corlor) };
        mMarqueeDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        mMarqueeDrawable.setShape(RECTANGLE);
        mMarqueeDrawable.setCornerRadius(getWidth() / 2);
        mMarqueeBgView.setBackground(mMarqueeDrawable);
        mMarqueeBgView.setAlpha(0);
        addView(mMarqueeBgView);
    }

    private void addNormalBgView() {
        mNormalBgView = new View(getContext());
        mNormalBgViewParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mNormalBgView.setLayoutParams(mNormalBgViewParams);


        mNormalBgDrawable = new GradientDrawable();
        mNormalBgDrawable.setShape(RECTANGLE);
        mNormalBgDrawable.setCornerRadius(getWidth() / 2);
        mNormalBgDrawable.setColor(getResources().getColor(R.color.live_daily_rank_normal_color));
        mNormalBgView.setBackground(mNormalBgDrawable);

        addView(mNormalBgView);
    }

    private void addTextView() {
        mTextViewLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mTextViewLayout.setLayoutParams(layoutParams);
        addView(mTextViewLayout);

        mTextView = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(params);
        mTextView.setAllCaps(false);
        mTextView.setPadding(0, 0, 0, 0);
        mTextView.setSingleLine();
        mTextView.setFadingEdgeLength(0);
        mTextView.setGravity(Gravity.CENTER);
        mTextViewLayout.addView(mTextView);
    }

    public boolean isAnimationRunning(){
        return mIsRankAnimationRunning || mIsIncomeAnimationRunning;
    }

    public boolean startIncomeUpdateAnimation(){
        if(isAnimationRunning()){
            return false;
        }
        mAnimator = ValueAnimator.ofInt(0, 1000);
        mAnimator.setDuration(3400);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int time = (int) animation.getAnimatedValue();
                handleIncomeAnimationUpdate(time);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsIncomeAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsIncomeAnimationRunning = false;
                mTextView.setAlpha(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsIncomeAnimationRunning = false;
                mTextView.setAlpha(1);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
        return true;
    }

    public void setIncomeData(int startIncome, int endIncome, String incomeText){
        mIncomePostText = incomeText;
        mStartIncome = startIncome;
        mEndIncome = endIncome;
    }

    private void handleIncomeAnimationUpdate(int time) {
        if(time < 100){ //0-10f 黄色文字透明度消失
            mTextView.setAlpha(1 - time / 100f);
        }
        if(time >= 100 && time < 200){//10-20f 黄色文字透明度0-100
            int tempTime = time - 100;
            mTextView.setText(mStartIncome + mIncomePostText);
            mTextView.setAlpha(tempTime / 100f);
        }
        if(time >= 200 && time < 800){
            int tempTime = time - 200;
            int currIncome = (int) (mStartIncome + tempTime / 600f * (mEndIncome - mStartIncome) + 0.5);
            mTextView.setText(currIncome + mIncomePostText);
        }
        if(time >= 800 && time < 900){//10-20f 黄色文字透明度0-100
            int tempTime = time - 800;
            mTextView.setText(mEndIncome + mIncomePostText);
            mTextView.setAlpha(1 - tempTime / 100f);
        }
        if(time >= 900 && time < 1000){ //0-10f 黄色文字透明度消失
            int tempTime = time - 800;
            mTextView.setAlpha(tempTime / 100f);
            mTextView.setText(mNormalText);
        }
    }

    public boolean startRankUpdateAnimation(){
        if(isAnimationRunning()){
            return false;
        }
        mAnimator = ValueAnimator.ofInt(0, 1480);
        mAnimator.setDuration(5000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mStartTvX = mTextView.getX();
        mStartTvLeft = mTextViewParams.leftMargin;
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int time = (int) animation.getAnimatedValue();
                handleRankAnimationUpdate(time, true);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsRankAnimationRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mTextView.setAlpha(1);
                mMarqueeBgView.setAlpha(0);
                mNormalBgView.setAlpha(1);
                mParams.width = mInitWidth;
                setLayoutParams(mParams);
                mIsRankAnimationRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mTextView.setAlpha(1);
                mMarqueeBgView.setAlpha(0);
                mNormalBgView.setAlpha(1);
                mParams.width = mInitWidth;
                setLayoutParams(mParams);
                mIsRankAnimationRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
        return true;
    }

    private void handleRankAnimationUpdate(int time, boolean isShowMarquee) {
        if (time <= 100) {//0-10f  字体透明度100-0
            mTextView.setAlpha(1 - time * 1.0f / 100);
        }
        if (time >= 100 && time < 200) {//10-20f 延长宽度，渐变为黄色
            int tempTime = time - 100;
            mNormalBgView.setAlpha(1 - tempTime * 1.0f / 100);
            mMarqueeBgView.setAlpha(tempTime * 1.0f / 100);
            mParams.width = (int) (mInitWidth + (mMaxWidth - mInitWidth) * tempTime / 100f);
            setLayoutParams(mParams);
        }
        if(time >= 140 && time < 240f){//14-24f 白色文字透明度0-100
            int tempTime = time - 140;
            mTextView.setText(mMarqueeText);
            mTextViewParams.width = (int) mTextView.getPaint().measureText(mMarqueeText);
            mTextViewParams.gravity = Gravity.START | Gravity.LEFT | Gravity.CENTER_VERTICAL;
            mTextView.setTextColor(mMarqueeTextColor);
            mTextView.setAlpha(tempTime * 1.0f / 100);
            Log.d(TAG, "");
        }
        if(time >= 240 && time < 1140){//24-114f 走马灯 耗时3s
            int tempTime = time - 240;
            int textWidth = (int) mTextView.getPaint().measureText(mMarqueeText);
            int range = textWidth - getWidth() + getHeight();
            mTextView.setX(-tempTime / 900f * range);
        }
        if(time >= 1140 && time < 1240f){//114f - 1240f 停留

        }
        if(time >= 1240 && time < 1340){//124f-134f白色文字透明度消失
            int tempTime = time - 1240;
            mTextView.setAlpha(1 - tempTime * 1.0f / 100);
        }
        if(time >= 1280f && time <1380f){ //128f-128f 缩短为初始长度，颜色渐变为黑底
            int tempTime = time - 1280;
            mMarqueeBgView.setAlpha(1 - tempTime * 1.0f / 100);
            mNormalBgView.setAlpha(tempTime * 1.0f / 100);
            mParams.width = (int) ((mMaxWidth - mInitWidth) * (100 - tempTime) / 100f + mInitWidth);
            setLayoutParams(mParams);
        }
        if(time >= 1380 && time < 1480f){//黄色文字透明度0-100
            int tempTime = time - 1380;
            mTextView.setText(mNormalText);
            mTextViewParams.gravity = Gravity.CENTER;
            mTextView.setTextColor(mNormalTextColor);
            mTextViewParams.width = (int) mTextView.getPaint().measureText(mNormalText);
            mTextViewParams.leftMargin = mStartTvLeft;
            mTextView.setLayoutParams(mTextViewParams);
            mTextView.setAlpha(tempTime / 100f);
            mTextView.setX(mStartTvX);
        }
//        Log.d(TAG, "rootwidth=" + getWidth() + " tvleft=" + mTextView.getLeft() + " tvright="+ mTextView.getRight()
//                + " tvW=" + mTextView.getWidth() + " normalTextW=" + mTextView.getPaint().measureText(mNormalText)
//                + " marqueeTextW=" + mTextView.getPaint().measureText(mMarqueeText) + " tvX=" + mTextView.getX() + " tvalpha=" + mTextView.getAlpha() + " time=" + time);
    }

}
