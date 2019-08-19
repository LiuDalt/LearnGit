package com.example.administrator.pk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


public class NonLinePKProgress extends View {

    private static final String TAG = "NonLinePKProgress" ;
    private Path mPathA;
    private Paint mPaint;
    private int mColorA = Color.parseColor("#20B4FF");
    private int mColorB = Color.parseColor("#FF2474");
    private int mPercentA = 50;//0-100
    private int mLineHalfWidth = 5;

    public NonLinePKProgress(Context context) {
        super(context);
        init();
    }

    public NonLinePKProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NonLinePKProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mPathA = new Path();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPaint.setColor(mColorB);
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), getHeight() / 2, getHeight() / 2, mPaint);
        mPaint.setColor(mColorA);
        canvas.drawPath(mPathA, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void updatePercentA(int percentA){
        Log.d(TAG, "updatePercentA() called with: percentA = [" + percentA + "]");
        mPercentA = percentA;
        if(getWidth() == 0) {
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.d(TAG, "onGlobalLayout() called");
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    updatePercentA(mPercentA);
                }
            });
            return;
        }
        int height = getHeight();
        int halfH = height / 2;
        int percentWidth = percentA * getRectWidth() / 100;
        int percentX = percentWidth + halfH;

        RectF rectCircleA = new RectF(0, 0, height, height);//左半圆所属矩形
        mPathA.reset();
        mPathA.moveTo(halfH, height);//左半圆底部坐标
        mPathA.arcTo(rectCircleA, 90, 180);//顺时针画半圆
        mPathA.lineTo(percentX + mLineHalfWidth, 0);//到斜线顶部坐标
        mPathA.lineTo(percentX - mLineHalfWidth, height);//到斜线底部坐标
        mPathA.close();//闭合

        invalidate();
    }

    private int getRectWidth() {
        return getWidth() - getHeight();
    }

    public int getPercentA() {
        return mPercentA;
    }
}
