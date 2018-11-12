package com.example.administrator.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;


public class BorderImageView extends ImageView {

    BorderDrawable mBorder;
    RingDrawable mRingDrawable;

    public BorderImageView(Context context) {
        super(context);

        init(context, null, 0, 0);
    }

    public BorderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        setWillNotDraw(false);
        mBorder = new BorderDrawable(Color.GREEN, getPaddingLeft(), getPaddingLeft() / 2);
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBorder.setBounds(0, 0, w, h);
        mRingDrawable = new RingDrawable(
                Color.RED,
                50,
                RingDrawable.THREE_QUARTER,
                10,
                new Point((int)getX() + getWidth() / 2, (int)getY() + getHeight() / 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBorder.draw(canvas);
        mRingDrawable.draw(canvas);
    }

}