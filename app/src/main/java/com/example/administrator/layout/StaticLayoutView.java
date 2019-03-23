package com.example.administrator.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class StaticLayoutView extends View {
    private String mText = "12312312323423423423423423423423423423423423423423423423423423442423423423423423423423423";
    public StaticLayoutView(Context context) {
        super(context);
    }

    public StaticLayoutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticLayoutView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!TextUtils.isEmpty(mText)) {
            TextPaint tp = new TextPaint();
            tp.setColor(Color.BLUE);
            tp.setStyle(Paint.Style.FILL);
            tp.setTextSize(50);
            StaticLayout myStaticLayout = new StaticLayout(mText, tp, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            myStaticLayout.draw(canvas);
            canvas.restore();
        }
    }

    public void setText(String text) {
        mText = text;
    }
}
