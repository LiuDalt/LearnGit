package com.example.administrator.pk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.administrator.myapplication.R;

public class NonLinePKBoard extends RelativeLayout {
    public NonLinePKBoard(Context context) {
        super(context);
        init(context);
    }

    public NonLinePKBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NonLinePKBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.non_line_pk_board, this, true);
    }

}
