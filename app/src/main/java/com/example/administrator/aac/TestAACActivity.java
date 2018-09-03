package com.example.administrator.aac;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class TestAACActivity extends AppCompatActivity {

    private ShareViewModel mModel;
    private SeekBar mSeekBar;
    private TextView mCurrStatusTv;
    private ConstraintLayout mRootLy;
    private Button mAddRemoveBtn;
    private Button mDynBtn;
    private Observer mDynBtnObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_aac);

        getLifecycle().addObserver(new MyLifeCycleObsever());
        initViews();
    }

    private void initViews() {
        mModel = ViewModelProviders.of(this).get(ShareViewModel.class);
        mSeekBar = findViewById(R.id.value_bar);
        mCurrStatusTv = findViewById(R.id.curr_status);
        mRootLy = findViewById(R.id.root_ly);
        mModel.getData().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data data) {
                mSeekBar.setProgress(data.getNum());
            }
        });

        //可以采用lambda表达式
//        mModel.getData().observe(this, data -> {
//            mSeekBar.setProgress(data.getNum());
//        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setData(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.change_value).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = mModel.getData().getValue().getNum();
                if(value > 95){
                    value = 0;
                }
                setData(value + 5);
            }
        });

        mCurrStatusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示当前Activity State
                mCurrStatusTv.setText("当前Activity状态是：" + getLifecycle().getCurrentState().name());
            }
        });

        initAddRemoveView();
    }

    private void initAddRemoveView() {
        mAddRemoveBtn = findViewById(R.id.add_remove_view);
        mDynBtnObserver = data -> {
            Log.i("aac---", "data change in dynView");
            if(mDynBtn != null) {
                mDynBtn.setText(mModel.getData().getValue().getNum() + " " + mModel.getData().getValue().getUnit2());
            }
        };
        mAddRemoveBtn.setOnClickListener(v ->{
            if(mDynBtn == null) {
                ConstraintSet constraintSet = new ConstraintSet();
                mDynBtn = new Button(TestAACActivity.this);
                mDynBtn.setId(R.id.dyn_btn);
                //添加view
                mRootLy.addView(mDynBtn);
                //克隆set
                constraintSet.clone(mRootLy);
                constraintSet.constrainWidth(mDynBtn.getId(), ConstraintLayout.LayoutParams.WRAP_CONTENT);
                constraintSet.constrainHeight(mDynBtn.getId(), ConstraintLayout.LayoutParams.WRAP_CONTENT);
                //放在父布局的底部
                constraintSet.connect(mDynBtn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                //放在mCurrStatusTv的右边，并且margin=30px
                constraintSet.connect(mDynBtn.getId(), ConstraintSet.LEFT, mCurrStatusTv.getId(), ConstraintSet.RIGHT, 30);
                //和mAddRemoveBtn上对齐
                constraintSet.connect(mDynBtn.getId(), ConstraintSet.TOP, mAddRemoveBtn.getId(), ConstraintSet.TOP);
                //应用约束
                constraintSet.applyTo(mRootLy);
                mModel.getData().observe(TestAACActivity.this, new Observer<Data>() {
                    @Override
                    public void onChanged(@Nullable Data data) {
                        Log.i("aac---", "data change in dynView");

                    }
                });
            }else{
                mDynBtn = null;
                mRootLy.removeView(mRootLy.findViewById(R.id.dyn_btn));
            }
        });
    }

    /**
     * 更新ViewModel的值，如果不想重新创建新的对象，可以直接取出原来的数据对象，重新set即可
     * @param value
     */
    private void setData(int value) {
        Data data = mModel.getData().getValue();
        data.setValue(value);
        mModel.setData(data);
    }
}
