package com.example.administrator.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class RingDrawable extends Drawable {
    public final static int QUARTER = 0;
    public final static int TWO_QUARTER = 1;
    public final static int THREE_QUARTER = 2;
    public final static int CIRCLE = 3;
    private final int mArcType;
    private final Point mCenter;
    private final int mStrokeWidth;
    private final int mColor;
    private final int mCircleRadius;
    private  RectF mRectStartCircle;
    private  RectF mRectEndCircle;
    private Path mPath;
    private final int mRadius;
    private Paint mPaint;
    private Path mStartPath = new Path();
    private Path mEndPath = new Path();

    public RingDrawable(int color, int radius, int arcType, int strokeWidth, Point center){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPath = new Path();
        mPath.moveTo(center.x + radius, center.y);
        RectF rectF = new RectF(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
        mArcType = arcType;
        mCenter = center;
        mRadius = radius;
        mStrokeWidth = strokeWidth;
        mColor = color;
        mCircleRadius = strokeWidth / 2;
        mRectStartCircle = new RectF(center.x + radius - mCircleRadius, center.y - mCircleRadius,
                center.x + radius - mCircleRadius + strokeWidth, center.y - mCircleRadius + strokeWidth);
        mStartPath.arcTo(mRectStartCircle, 180, 180);
        mStartPath.close();
        if(arcType == QUARTER){
            mPath.arcTo(rectF, 0, 90);
            mRectEndCircle = new RectF(center.x - mCircleRadius, center.y - mCircleRadius + radius,
                    center.x - mCircleRadius + strokeWidth, center.y - mCircleRadius + radius + strokeWidth);
            mEndPath.arcTo(mRectEndCircle, 90, 180);
            mEndPath.close();
        }else if(arcType == TWO_QUARTER){
            mPath.arcTo(rectF, 0, 180);
            mRectEndCircle = new RectF(center.x - mCircleRadius - radius, center.y - mCircleRadius,
                    center.x - mCircleRadius -radius + strokeWidth, center.y - mCircleRadius + strokeWidth);
            mEndPath.arcTo(mRectEndCircle, 180, 180);
            mEndPath.close();
        }else if(arcType == THREE_QUARTER){
            mPath.arcTo(rectF, 0, 270);
            mRectEndCircle = new RectF(center.x - mCircleRadius, center.y - mCircleRadius - radius,
                    center.x - mCircleRadius + strokeWidth, center.y - mCircleRadius - radius + strokeWidth);
            mEndPath.arcTo(mRectEndCircle, -90, 180);
            mEndPath.close();
        }else{
            mPath.addCircle(center.x, center.y, radius, Path.Direction.CCW);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
        if(mArcType != CIRCLE){
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(mStartPath, mPaint);
            canvas.drawPath(mEndPath, mPaint);
        }
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
