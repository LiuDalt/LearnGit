package com.example.administrator.drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.administrator.myapplication.R;



public class RingView extends View {
    public final static int RING_COLOR_DEF = Color.RED;
    public final static int RING_WIDTH_DEF = 10;
    private RingDrawable mRingDrawable;
    private int mRingColor = RING_COLOR_DEF;
    private int mRingWidth = RING_WIDTH_DEF;
    private int mRingArcType = RingDrawable.CIRCLE;
    private int mRingRadius;
    private Animation mAnimator;

    public RingView(Context context) {
        super(context);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RingView);
        mRingColor = ta.getColor(R.styleable.RingView_ring_color, RING_COLOR_DEF);
        mRingWidth = ta.getDimensionPixelSize(R.styleable.RingView_stroke_width, RING_WIDTH_DEF);
        mRingArcType = ta.getInt(R.styleable.RingView_arc_type, RingDrawable.CIRCLE);
        mRingRadius = ta.getDimensionPixelSize(R.styleable.RingView_radius, getWidth() / 2);
        ta.recycle();

        if(mRingArcType != RingDrawable.CIRCLE) {
            mAnimator = AnimationUtils.loadAnimation(getContext(), R.anim.ring_rotate);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.setFillAfter(false);
        }
    }

    private void initData() {
        if(mRingRadius == 0){
            mRingRadius = getWidth() / 2;
        }
        mRingDrawable = new RingDrawable(
                mRingColor,
                mRingRadius,
                mRingArcType,
                mRingWidth,
                new Point(getWidth() / 2, getHeight() / 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRingDrawable.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initData();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if(mRingArcType == RingDrawable.CIRCLE){
            return;
        }
        if(mAnimator == null){
            return;
        }
        if(visibility == VISIBLE){
            startAnimate();
        }else{
            stopAnimate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimate();
    }

    private void startAnimate(){
//        if(getVisibility() == VISIBLE && mAnimator != null){
//            startAnimation(mAnimator);
//        }
    }

    private void stopAnimate(){
//        if(mAnimator == null){
//            return;
//        }
//        mAnimator.cancel();
//        setAnimation(null);
    }
}
