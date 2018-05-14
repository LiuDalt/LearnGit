package com.example.administrator.AudioRhythm;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
    private double mPlayedPercent = 0;
    private double mUnplayedPercent = 1;
    private double mUnavailabePercent = 0;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(!checkMusicManagerValid()){
                removeUpdateCallback();
                return;
            }
            mPlayedPercent += 50.0 / getDuration();
            if(mIsSliding) {
                if (mPlayedPercent > 1) {
                    mPlayedPercent = 1;
                    stopMusic();
                }
            }else {
                if (mPlayedPercent > mUnplayedPercent) {
                    mPlayedPercent = mUnplayedPercent;
                    stopMusic();
                }
            }
            mHandler.postDelayed(this, 50);
            updateAudioWav();
        }
    };

    private boolean checkMusicManagerValid() {
        return true;//TODO
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){

    };
    private boolean mIsSliding = false;
    private View mUnavailableMaskView;

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
        mUnavailableMaskView = findViewById(R.id.unavailabe_mask_view);

        mRootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRootLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mRootLy.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                mAudioWavView.setWavsData(getTestWavData());
                updateAudioWav();
                updateUnavailableTime(0);
            }
        });
    }

    private void updateUnavailabeMaskView() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUnavailableMaskView.getLayoutParams();
        params.width = (int) (mUnavailabePercent * mAudioWavView.getWidth());
        mUnavailableMaskView.setLayoutParams(params);
    }

    public void updateUnavailableTime(int time){
        mUnavailabePercent = time / getDuration();
        mPlayedPercent = mUnavailabePercent;
        double mMaxSlideLeft =  mAudioWavView.getX() + mAudioWavView.getWidth() - mSlideView.getWidth() / 2.0;
        setSlideViewMarginLeft((int) (mMaxSlideLeft + 0.5));
        updateUnavailabeMaskView();
        playMusic();
    }

    private void onStartPlayMusic() {
        removeUpdateCallback();
        //seek to
        mHandler.postDelayed(mRunnable, 50);
    }

    private void playMusic(){
        //seek to playedpercent
        onStartPlayMusic();
    }

    private void stopMusic() {
        //TODO
        removeUpdateCallback();
    }

    private void removeUpdateCallback(){
        mHandler.removeCallbacks(mRunnable);
    }

    private double getDuration(){
        return 15000;//TODO
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
        mUnplayedPercent = calUnplayPercent(x);
        calTouchEndPlayedPercent();
        updateAudioWav();
        mIsSliding = false;
        playMusic();
    }

    private void calTouchEndPlayedPercent() {
        mPlayedPercent = mUnplayedPercent - PRE_PERCENT;
        if(mPlayedPercent < mUnavailabePercent){
            mPlayedPercent = mUnavailabePercent;
        }
    }


    private double calUnplayPercent(int x) {
        double len =  x - mAudioWavView.getX() + mSlideView.getWidth() / 2;
        return len * 1.0 / mAudioWavView.getWidth();
    }

    private void updateAudioWav() {
        mAudioWavView.update(mPlayedPercent, mUnplayedPercent);
    }

    private void onTouchStart(MotionEvent event) {
        mIsSliding = true;
        touchStartOrMove(event);
    }

    private int setSlideViewX(double x) {
        x = x - mSlideView.getWidth() / 2.0;
        double mMinSlideLeft = mAudioWavView.getX() - mSlideView.getWidth() / 2.0 + mUnavailabePercent * mAudioWavView.getWidth();
        double mMaxSlideLeft =  mAudioWavView.getX() + mAudioWavView.getWidth() - mSlideView.getWidth() / 2.0;
        if(x < mMinSlideLeft){
            x = mMinSlideLeft;
        }else if(x > mMaxSlideLeft){
            x = mMaxSlideLeft;
        }
        x = (int) (x + 0.5);
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
        int time = (int) (unplay * getDuration() / 1000);
        mSlideTimeTv.setText(time  + "s");
        Log.i("audio----3", "x=" + x + " wavW=" + mAudioWavView.getWidth() + " wavX=" + mAudioWavView.getX() + " slideW/2=" + (mSlideView.getWidth() / 2));

    }

    private void onTouchMove(MotionEvent event) {
        touchStartOrMove(event);
    }

    private void touchStartOrMove(MotionEvent event) {
        int x = setSlideViewX(event.getX());
        mUnplayedPercent = calUnplayPercent(x);
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
        mAudioWavView.setWavsData(getTestWavData());
    }
}
