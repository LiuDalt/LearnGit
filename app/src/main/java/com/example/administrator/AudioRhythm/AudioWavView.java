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
    private int mUnitWidth = 1;
    private int mSpace = 0;
    private Paint mPaint;
    private int mBgWavColor = Color.parseColor("#696969");
    private int mBgColor = Color.parseColor("#D3D3D3");
    private int mPlayedWavColor = Color.parseColor("#7FFFAA");
    private int mUnplayWavColor = Color.parseColor("#FFD700");
    private int mDefaultWidth;
    private int mDefaultHeight;
    private int mMaxWavHeight;
    private int mPlayedIndex = 80;
    private int mUnplayIndex = 160;

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
        mDefaultHeight = ScreenUtils.dpToPx(100);
        mMaxWavHeight = (int) (mDefaultHeight * 0.8);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgWavColor);
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
        int dstH;
        int centY = getHeight() / 2;
        dstH = mWavsData[i];
        if(dstH > mMaxWavHeight){
            dstH = mMaxWavHeight;
        }
        int dy = centY - dstH / 2;
        canvas.drawRect(i, dy, i + mUnitWidth, dy + dstH, mPaint);
    }

    private void setPaintColor(int index) {
        if(mPlayedIndex < mUnplayIndex) {
            if (index == mPlayedIndex) {
                mPaint.setColor(mUnplayWavColor);
            }
            if (index == mUnplayIndex) {
                mPaint.setColor(mBgWavColor);
            }
        }else{
            if(index == mPlayedIndex){
                mPaint.setColor(mBgWavColor);
            }
        }
    }

    /**
     * unplayIndex must be larger than playedIndex
     */
    public void update(double playedPercent, double unplayPercent){
        int playedIndex = (int) (playedPercent * mWavsData.length + 0.5);
        int unplayIndex = (int)(unplayPercent * mWavsData.length +0.5);
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
        mPlayedIndex = playedIndex;
        mUnplayIndex = unplayIndex;
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
