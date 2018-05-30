package com.example.administrator.AudioRhythm;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.ScreenUtils;

import java.util.Random;


public class AudioRhythmView extends RelativeLayout  implements LifecycleObserver{
    private static final double PRE_TIME = 3000;
    private static final double SIDE_OFFSET = 100;
    private AudioWavView mAudioWavView;
    private RelativeLayout mRootLy;
    private ImageView mSlideImgView;
    private TextView mTimeTv;
    private FrameLayout.LayoutParams mSlideLyParams;
    private TextView mLastPointTv;
    private double mPlayedPercent = 0;
    private double mUnplayedPercent = 1;
    private double mUnavailabePercent = 0;
    private Lifecycle mLifecycle;
//    private MusicManager mMusicManager;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(!checkMusicManagerValid()){
                removeUpdateCallback();
                return;
            }
            mPlayedPercent += 50.0 / getDuration();
//            mPlayedPercent = mMusicManager.getCurrentMs() * 1.0 / getDuration();
            if(mIsSliding) {
                if (mPlayedPercent > 1) {
                    mPlayedPercent = 1;
                    replayMusic();
                }
            }else {
                if (mPlayedPercent > mUnplayedPercent) {
                    mPlayedPercent = mUnplayedPercent;
                    replayMusic();
                }
            }
            mHandler.postDelayed(this, 50);
            updateAudioWav();
        }
    };
    private int mUnavailableTime;

    private void replayMusic() {
        calPrePosPlayedPercent();
        playMusic();
    }

    private boolean checkMusicManagerValid() {
//        if(mMusicManager == null){
//            return false;
//        }
//        if(!mMusicManager.isInPlay()){
//            return false;
//        }
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
        mSlideImgView = findViewById(R.id.audio_slideview);
        mAudioWavView = findViewById(R.id.audio_view);
        mTimeTv = findViewById(R.id.audio_time_tv);
        mUnavailableMaskView = findViewById(R.id.unavailabe_mask_view);
        mAudioWavView.setMaxWavHeight(ScreenUtils.dpToPx(82));
        mLastPointTv = findViewById(R.id.wav_last_point_tv);
        mRootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT  >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRootLy.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mRootLy.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                mAudioWavView.setWavsData(getWavData());
                updateAudioWav();
                setSlideViewMarginLeft((int) (calMaxSlideLeft() + 0.5));
                updateUnavailableTime(0, true);
            }
        });
    }


    public void setLifecycle(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
        if(lifecycle != null){
            mLifecycle.addObserver(this);
        }
    }

    private void updateUnavailabeMaskView() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mUnavailableMaskView.getLayoutParams();
        params.width = (int) (mUnavailabePercent * mAudioWavView.getWidth() + 0.5);
        mUnavailableMaskView.setLayoutParams(params);
    }

    public void updateUnavailableTime(int time, boolean needPlay){
        mUnavailabePercent = time / getDuration();
        mPlayedPercent = mUnavailabePercent;
        updateUnavailabeMaskView();
        if(needPlay) {
            playMusic();
        }
    }

    private void onStartPlayMusic() {
        removeUpdateCallback();
        mHandler.postDelayed(mRunnable, 50);
    }

    private void playMusic(){
//        mMusicManager.seekTo((int) (mPlayedPercent * getDuration()));
//        mMusicManager.play();
        onStartPlayMusic();
    }

    private void stopMusic() {
        removeUpdateCallback();
//        mMusicManager.pause();
    }

    private void removeUpdateCallback(){
        mHandler.removeCallbacks(mRunnable);
    }

    private double getDuration(){
//        if(mMusicManager != null && mMusicManager.getMediaPlayer() != null){
//            return mMusicManager.getMediaPlayer().getDuration();
//        }
        return 15000;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        onDestroy();
        Log.i("audio---5", "onDetachedFromWindow=" + getCurrSelectedPos());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.i("audio---5", "onAttachWindow=" + getCurrSelectedPos());
    }

    private void onDestroy(){
//        if(mMusicManager != null){
//            mMusicManager.destroy();
//            mMusicManager = null;
//        }
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
        calPrePosPlayedPercent();
        updateAudioWav();
        mIsSliding = false;
        playMusic();
        Log.i("audio---3", "touchend--mUnplayedPercent=" + mUnplayedPercent);
    }

    //计算刻度前置三秒的位置：这里pre_percent是临时设置
    private void calPrePosPlayedPercent() {
        mPlayedPercent = mUnplayedPercent - PRE_TIME / getDuration();
        if(mPlayedPercent < mUnavailabePercent){
            mPlayedPercent = mUnavailabePercent;
        }
    }

    private double calUnplayPercent(int x) {
        double len =  x - mAudioWavView.getX() + mSlideImgView.getWidth() / 2;
        return len * 1.0 / mAudioWavView.getWidth();
    }

    private void updateAudioWav() {
        mAudioWavView.update(mUnavailabePercent, mPlayedPercent, mUnplayedPercent);
    }

    private void onTouchStart(MotionEvent event) {
        mIsSliding = true;
        touchStartOrMove(event);
    }

    private int setSlideViewX(double x) {
        x = x - mSlideImgView.getWidth() / 2.0;
        double mMinSlideLeft = calMinSlideLeft();
        double mMaxSlideLeft =  calMaxSlideLeft();
        if(x < mMinSlideLeft){
            x = mMinSlideLeft;
        }else if(x > mMaxSlideLeft){
            x = mMaxSlideLeft;
        }
        x = (int) (x + 0.5);
        setSlideViewMarginLeft((int) x);

        return (int) x;
    }

    private double calMinSlideLeft() {
        return mAudioWavView.getX() - mSlideImgView.getWidth() / 2.0 + mUnavailabePercent * mAudioWavView.getWidth() + calSideOffset();
    }

    private double calMaxSlideLeft(){
        return  mAudioWavView.getX() + mAudioWavView.getWidth() - mSlideImgView.getWidth() / 2.0 - calSideOffset();
    }

    //产品要求端点出有0.1s偏移
    private double calSideOffset(){
        return SIDE_OFFSET / getDuration() * mAudioWavView.getWidth();
    }
    private void setSlideViewMarginLeft(int x) {
        if(mSlideLyParams == null){
            mSlideLyParams = (FrameLayout.LayoutParams) mSlideImgView.getLayoutParams();
        }
        mSlideLyParams.leftMargin = x;
        mSlideImgView.setLayoutParams(mSlideLyParams);

        double unplay = calUnplayPercent(x);
        Log.i("audio---3", "unplay=" + unplay);
        setTimeText(unplay);
        adjustTvPos(mTimeTv, x);
        adjustTvPos(mLastPointTv, x);

    }

    private void setTimeText(double currPos) {
        double time = currPos * getDuration() / 100 + 0.5;
        mTimeTv.setText(((int)time) / 10.0  + "s");
    }

    private void adjustTvPos(View view, int slideViewX) {
        RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        int width = view.getWidth();
        int left = (int) (slideViewX + mSlideImgView.getWidth() / 2  - view.getWidth() / 2 + 0.5);
        if(left + view.getWidth() > mAudioWavView.getRight()){
            left = mAudioWavView.getRight() - view.getWidth();
        }
        if(left < mAudioWavView.getX()){
            left = (int) mAudioWavView.getX();
        }
        params.leftMargin = left;
        view.setLayoutParams(params);
    }

    private void onTouchMove(MotionEvent event) {
        touchStartOrMove(event);
    }


    private void touchStartOrMove(MotionEvent event) {
        int x = setSlideViewX(event.getX());
        mUnplayedPercent = calUnplayPercent(x);
        updateAudioWav();
    }

    private int[] getWavData() {
        int num = (int) (mAudioWavView.getWidth() / ScreenUtils.dpToPx(2));
        int data[] = new int[ num];
        Random random = new Random();
        float maxHeight = ScreenUtils.dpToPx(82) + 1;
        for(int i = 0; i < num; i++) {
            data[i] = random.nextInt((int) maxHeight);
            if(data[i] < 15){
                data[i] = 15;
            }
        }
        Log.i("audio----8", "screenw=" + ScreenUtils.getScreenWithSize(getContext()).mWidth +" width=" + mAudioWavView.getWidth() + " num=" + num + " screenPix:" + ScreenUtils.dpToPx(2) + " slideW=" + mSlideImgView.getWidth());
        return data;
    }

    public void setMusicPath(final String path, final int time) {
//        mAudioWavView.setWavsData(getWavData());
//        if(mMusicManager == null){
//            mMusicManager = new MusicManager(getContext());
//        }
//        mMusicManager.setOnMusicStatChangeListener(this);
//        mUnavailableTime = time;
//        mMusicManager.setPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                ThreadUtils.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateUnavailableTime(time, true);
//                    }
//                });
//                Log.i("audio----", "onPrepared------");
//            }
//        });
//        mMusicManager.play(path, false);
        Log.i("audio---2", "set musicPath------" + path);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onViewResume(){
        playMusic();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onViewPause(){
        stopMusic();
    }

    //返回选择的刻度值
    public int getCurrSelectedPos(){
        return (int) (mUnplayedPercent * getDuration());
    }

    public void onStart() {
//        ThreadUtils.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                updateUnavailableTime(mUnavailableTime, true);
//            }
//        });

        onStartPlayMusic();
        Log.i("audio---2", "onpstart------");
    }

    public void onPause() {
        Log.i("audio---2", "onpause------");
    }

    public void onResume() {
        onStartPlayMusic();
        Log.i("audio---2", "onResume------");
    }

    public void onComplete() {
        Log.i("audio---2", "onComplete------");
        replayMusic();
    }

    public void onError() {
        removeUpdateCallback();
        Log.i("audio---2", "onError------");
    }
}
