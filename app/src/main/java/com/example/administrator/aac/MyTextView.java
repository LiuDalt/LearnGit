package com.example.administrator.aac;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MyTextView extends AppCompatTextView{
    private ShareViewModel mModel;
    private Observer<String> mObserver;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        init(getContext());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //移除observer
        mModel.getMapData().removeObserver(mObserver);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init(Context context) {
        mModel = ViewModelProviders.of((FragmentActivity) getContext()).get(ShareViewModel.class);
        //直接观察viewmodel的livedata，观察数据变化
//        mModel.getData().observe((FragmentActivity) getContext(), new Observer<Data>() {
//            @Override
//            public void onChanged(@Nullable Data data) {
//                String unit = data.getUnit2();
//                setText(data.getNum() + unit);
//            }
//        });
        //观察viewmodel的Transformations.map（） data，可以得到转换的String
        mObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String des) {
                setText(des);
            }
        };
        mModel.getMapData().observe((LifecycleOwner) getContext(), mObserver);
    }
}
