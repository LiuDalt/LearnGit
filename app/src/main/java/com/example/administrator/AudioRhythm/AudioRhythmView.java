package com.example.administrator.AudioRhythm;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.util.Random;

public class AudioRhythmView extends ConstraintLayout{
    private static final double PRE_PERCENT = 0.15;
    private AudioWavView mAudioWavView;
    private FrameLayout mRootLy;
    private LinearLayout mSlideView;
    private TextView mSlideTimeTv;
    private FrameLayout.LayoutParams mSlideLyParams;
    private int mMinSlideLeft;
    private int mMaxSlideLeft;
    private int mPreDistance;
    private double mPlayedPercent = 0;
    private double mUnplayedPercent = 1;
    private int mDuration = 15000;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mPlayedPercent += 50.0 / mDuration;
            if(mIsSliding) {
                if (mPlayedPercent > 1) {
                    mPlayedPercent = 1;
                    mHandler.removeCallbacks(this);
                    stopMusic();
                }
            }else {
                if (mPlayedPercent > mUnplayedPercent) {
                    mPlayedPercent = mUnplayedPercent;
                    mHandler.removeCallbacks(this);
                    stopMusic();
                }
            }
            mHandler.postDelayed(this, 50);
            updateAudioWav();
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper()){

    };
    private boolean mIsSliding = false;

    public AudioRhythmView(Context context) {
        super(context);

        init(context);
    }

    public AudioRhythmView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public AudioRhythmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.audio_rhythm_ly, this, true);

        mRootLy = findViewById(R.id.audio_rhythm_ly);
        mSlideView = findViewById(R.id.audio_slideview);
        mAudioWavView = findViewById(R.id.audio_view);
        mSlideTimeTv = findViewById(R.id.audio_time_tv);

        mRootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRootLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mRootLy.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mMinSlideLeft = (int) (mAudioWavView.getX() - mSlideView.getWidth() / 2);
                mMaxSlideLeft = (int) (mAudioWavView.getX() + mAudioWavView.getWidth() - mSlideView.getWidth() / 2);
                mAudioWavView.setWavsData(getTestWavData());
                setSlideViewMarginLeft(mMaxSlideLeft);
                updateAudioWav();

                startPlayMusic();
            }
        });
    }

    private void startPlayMusic() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 50);
    }

    private void stopMusic() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            onTouchStart(event);
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            onTouchMove(event);
            return true;
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            onTouchEnd(event);
            return true;
        }

        return super.onTouchEvent(event);
    }

    private void onTouchEnd(MotionEvent event) {
        int x = setSlideViewX(event.getX());
        mUnplayedPercent = calUnplayPercent(x + mSlideView.getWidth() / 2);
        calTouchEndPlayedPercent();
        updateAudioWav();
        mIsSliding = false;
        startPlayMusic();
    }

    private void calTouchEndPlayedPercent() {
        mPlayedPercent = mUnplayedPercent - PRE_PERCENT;
        if(mPlayedPercent < 0){
            mPlayedPercent = 0;
        }
    }

    private double calUnplayPercent(int x) {
        double len =  x - mAudioWavView.getX();
        return len / mAudioWavView.getWidth();
    }

    private void updateAudioWav() {
        mAudioWavView.update(mPlayedPercent, mUnplayedPercent);
    }

    private void onTouchStart(MotionEvent event) {
        mIsSliding = true;
        touchStartOrMove(event);
    }

    private int setSlideViewX(float x) {
        x = x - mSlideView.getWidth() / 2;
        if(x < mMinSlideLeft){
            x = mMinSlideLeft;
        }else if(x > mMaxSlideLeft){
            x = mMaxSlideLeft;
        }
        setSlideViewMarginLeft((int) x);
        return (int) x;
    }

    private void setSlideViewMarginLeft(int x) {
        if(mSlideLyParams == null){
            mSlideLyParams = (FrameLayout.LayoutParams) mSlideView.getLayoutParams();
        }
        mSlideLyParams.leftMargin = x;
        mSlideView.setLayoutParams(mSlideLyParams);

        double unplay = calUnplayPercent(x);
        int time = (int) (unplay * mDuration / 100);
        if(time > 100){
            mSlideTimeTv.setText(time / 10 + "s");
        }else{
            mSlideTimeTv.setText(time / 10.0 + "s");
        }
    }

    private void onTouchMove(MotionEvent event) {
        touchStartOrMove(event);
    }

    private void touchStartOrMove(MotionEvent event) {
        int x = setSlideViewX(event.getX());
        mUnplayedPercent = calUnplayPercent(x + mSlideView.getWidth() / 2);
        updateAudioWav();
    }

    private int[] getTestWavData() {
        int num = mAudioWavView.getWidth();
        int data[] = new int[num];
        Random random = new Random();
        for(int i = 0; i < num; i++) {
            data[i] = random.nextInt(240);
        }
        return data;
    }

    public void setMusicPath(String path) {
//        mAudioWavView.setWavsData(getTestWavData());
    }
}
