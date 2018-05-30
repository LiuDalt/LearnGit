package com.example.administrator.AudioRhythm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.administrator.myapplication.ScreenUtils;

public class AudioWavView extends View {

    private int[] mWavsData;
    private float mUnitWidth = ScreenUtils.dpToPx(1);
    private float mSpace = ScreenUtils.dpToPx(1);
    private Paint mPaint;
    private int mBgWavColor = Color.parseColor("#80000000");
    private int mBgColor = Color.parseColor("#33ffffff");
    private int mPlayedWavColor = Color.parseColor("#FFEE36");
    private int mUnplayWavColor = Color.parseColor("#66FFEE36");
    private int mUnavailabeWavColor = Color.parseColor("#1FCECE");
    private int mDefaultWidth;
    private int mDefaultHeight;
    private float mMaxWavHeight;
    private int mPlayedIndex = 80;
    private int mUnplayIndex = 160;
    private int mUnavailableIndex = 0;

    public AudioWavView(Context context) {
        super(context);

        init();
    }

    public AudioWavView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AudioWavView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mDefaultWidth = ScreenUtils.getScreenWithSize(getContext()).mWidth * 9 / 10;
        mDefaultHeight = (int) ScreenUtils.dpToPx(100);
        mMaxWavHeight = (int) (mDefaultHeight * 0.8);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgWavColor);
    }

    public void setMaxWavHeight(float maxWavHeight) {
        mMaxWavHeight = maxWavHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWavs(canvas);
    }

    private void drawWavs(Canvas canvas) {
        mPaint.setColor(mBgColor);
        int height = getHeight();
        canvas.drawRect(0, 0, getWidth(), height, mPaint);
        if(mWavsData == null || mWavsData.length < 1){
            return;
        }
        int i = 0;
        mPaint.setColor(mUnavailabeWavColor);
        for(; i < mUnavailableIndex; i++){
            drawUnit(canvas, i);
        }
        if(mUnplayIndex > mPlayedIndex){
            mPaint.setColor(mPlayedWavColor);
            for(; i < mPlayedIndex; i++){
                drawUnit(canvas, i);
            }
            mPaint.setColor(mUnplayWavColor);
            for(; i <= mUnplayIndex; i++){
                drawUnit(canvas, i);
            }
        }else{
            mPaint.setColor(mPlayedWavColor);
            for(; i < mPlayedIndex; i++){
                drawUnit(canvas, i);
            }
        }
        mPaint.setColor(mBgWavColor);
        int len = mWavsData.length;
        for(; i < len; i++){
            drawUnit(canvas, i);
        }
    }

    private void drawUnit(Canvas canvas, int i) {
        float dstH;
        float centY = getHeight() / 2;
        dstH = mWavsData[i];
        if(dstH > mMaxWavHeight){
            dstH = mMaxWavHeight;
        }
        float dy = centY - dstH / 2;
        float left = i * (mSpace + mUnitWidth);
        canvas.drawRect(left, dy, left + mUnitWidth, dy + dstH, mPaint);
    }


    /**
     * unplayIndex must be larger than playedIndex
     */
    public void update(double unavailabePercent, double playedPercent, double unplayPercent){
        int playedIndex = (int) (playedPercent * mWavsData.length + 0.5);
        int unplayIndex = (int)(unplayPercent * mWavsData.length +0.5);
        int unavailabeIndex = (int) (unavailabePercent * mWavsData.length + 0.5);
        if(playedIndex < 0){
            playedIndex = 0;
        }
        if(playedIndex >= mWavsData.length){
            playedIndex = mWavsData.length - 1;
        }
        if(unplayIndex < 0){
            unplayIndex = 0;
        }
        if(unplayIndex >= mWavsData.length){
            unplayIndex = mWavsData.length - 1;
        }
        if(unavailabeIndex < 0){
            unavailabeIndex = 0;
        }
        if(unavailabeIndex >= mWavsData.length){
            unavailabeIndex = mWavsData.length - 1;
        }
        mPlayedIndex = playedIndex;
        mUnplayIndex = unplayIndex;
        mUnavailableIndex = unavailabeIndex;
        invalidate();
    }

    public void setBgWavColor(int bgWavColor) {
        mBgWavColor = bgWavColor;
    }

    public void setWavsData(int[] wavsData) {
        mWavsData = wavsData;
        mPlayedIndex = 0;
        mUnplayIndex = 0;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasureSize(mDefaultWidth, widthMeasureSpec);
        int height = getMeasureSize(mDefaultHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int getMeasureSize(int defaultSize, int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if(mode == MeasureSpec.UNSPECIFIED){
            return defaultSize;
        }else if(mode == MeasureSpec.AT_MOST){
            return Math.min(defaultSize, size);
        }
        return size;
    }
}
