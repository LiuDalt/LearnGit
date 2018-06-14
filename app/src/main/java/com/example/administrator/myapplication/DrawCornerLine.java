package com.example.administrator.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class DrawCornerLine extends View {
    private double mProgress;
    private Paint mPaint;
    private int mStartColor = Color.GREEN;
    private int mEndColor = Color.YELLOW;
    private int mBgColor = Color.GRAY;

    public DrawCornerLine(Context context) {
        super(context);

        init(context);
    }


    public DrawCornerLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawCornerLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void update(double progress){
        mProgress = progress;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        triPathDraw(canvas);
    }

    private void triPathDraw(Canvas canvas) {
        mPaint.setColor(mBgColor);
        int height = getHeight();
        int width = getWidth();
        RectF rectF = new RectF(0, 0, width, height);
        mPaint.setShader(null);
        canvas.drawRoundRect(rectF, height / 2, height / 2, mPaint);//先绘制背景
        float radius = height / 2;//左右半圆的半径
        double progressW = mProgress * width;//当前进度对应的长度
        RectF rectLeft = new RectF(0, 0, height, height);
//        mPaint.setColor(mStartColor);
        LinearGradient linearGradient = new LinearGradient(0, 0, (int)progressW, height, mStartColor,calGradientColor((float) mProgress, mStartColor, mEndColor), Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        mPaint.setStyle(Paint.Style.FILL);
        if(progressW < radius){//当进度处于图1的时候
            double disW = radius - progressW;
            float angle = (float) Math.toDegrees(Math.acos(disW / radius));
            //绘制图1 弧AD对应的棕色区域，注意第三个参数设置为false
            // 表示绘制不经过圆心（即图一效果）的月牙部分，设置为true则绘制的是扇形，angle是绘制角度
            canvas.drawArc(rectLeft, 180 - angle, angle * 2, false, mPaint);
        }else if(progressW <= width - radius){//当进度处于图2的时候
            canvas.drawArc(rectLeft, 90, 180, true, mPaint);//绘制弧AD半圆
            RectF rectMid = new RectF(radius + 1, 0, (float) progressW, height);
            canvas.drawRect(rectMid, mPaint);//绘制ABCD矩形进度
        }else{//图4对应部分
            canvas.drawArc(rectLeft, 90, 180, true, mPaint);//绘制左端半圆

            RectF rectMid = new RectF(radius + 1, 0, width - radius, height);
            canvas.drawRect(rectMid, mPaint);//绘制中间的矩形部分

            //得到图四中F->C->B->E->F的闭合区域，注意此处是从头F为起点，减少坐标计算
            double disW = progressW - (width - radius);
            float angle = (float) Math.toDegrees(Math.acos(disW / radius));
            RectF rectRight = new RectF(width - height, 0, width, height);
            Path path = new Path();
            path.arcTo(rectRight, angle, 90 - angle);
            path.lineTo(width - radius, 0);
            path.arcTo(rectRight, 270, 90 - angle);
            path.close();
            canvas.drawPath(path, mPaint);//绘制path

//            //如图三：根据半圆BC和矩形GHIJ，根据paint.setXfermode取SRC_OUT，就能得到封闭区域BEFC的部分
//            int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);//相当于新建一个图层
//            RectF rectDst = new RectF((float) progressW, 0, width, height);//图3中的
//            mPaint.setColor(Color.GRAY);//此处可以用任意颜色，但该颜色不能包含透明度
//            canvas.drawRect(rectDst, mPaint);//绘制Dst
//            mPaint.setColor(mStartColor);
//            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//            RectF rectSrc = new RectF(width - height, 0, width, height);
//            canvas.drawArc(rectSrc, -90, 180, true, mPaint);//绘制Src
//            mPaint.setXfermode(null);
//            canvas.restoreToCount(sc);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int defaultWidth = (int) (ScreenUtils.getScreenWithDisplay(getContext()).mWidth * 0.9);
        int defaultHeight = (int) ScreenUtils.dpToPx(50);
        int width = getMeasureSize(defaultWidth, widthMeasureSpec);
        int height = getMeasureSize(defaultHeight, heightMeasureSpec);

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

    private int calGradientColor(float progress, int colorLeft, int colorRight) {
        int redS = Color.red(colorLeft);
        int greenS = Color.red(colorLeft);
        int blueS = Color.blue(colorLeft);
        int redE = Color.red(colorRight);
        int greenE = Color.green(colorRight);
        int blueE = Color.blue(colorRight);

        int dstRed = (int) (redS * (1 - progress) + progress * redE);
        int dstGreen = (int) (greenS * (1 - progress) + progress * greenE);
        int dstBlue = (int) (blueS * (1 - progress) + progress * blueE);
        return Color.argb(255, dstRed, dstGreen, dstBlue);
    }

}
