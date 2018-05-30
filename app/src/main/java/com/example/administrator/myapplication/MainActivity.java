package com.example.administrator.myapplication;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.AudioRhythm.AudioActivity;
import com.example.administrator.aac.TestAACActivity;
import com.example.administrator.mannotation.IdInject;
import com.example.administrator.mannotation.IdInjectHelper;
import com.example.administrator.myapplication.databinding.ItemLayoutBinding;
import com.example.administrator.rooms.RoomActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRootLy;
    @IdInject(R.id.annotation_btn)
    private Button mAnnotationBtn;

    @IdInject(R.id.rootly)
    private LinearLayout mLinearLayout;

    @IdInject(R.id.dyn_view)
    private Button mDynAnnotationBtn;

    @IdInject(R.id.room_btn)
    private Button mRoomBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootLy = findViewById(R.id.rootly);
//        dataBinding();
        mRootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                testScreen();
            }
        });


        findViewById(R.id.test_audio).setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, AudioActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observable.fromArray("abcdfg").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ScreenUtils.copyToClipBoard(MainActivity.this, s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("clipboard", "error");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i("clipboard", ScreenUtils.getClipboardContent(MainActivity.this));
                    }
                });
            }
        });

        Log.i("pxdpsp", "30 dp to px = " + ScreenUtils.dpToPx(30));
        Log.i("pxdpsp", "30 px to dp = " + ScreenUtils.pxToDp(30));
        Log.i("pxdpsp", "30 sp to px = " + ScreenUtils.spToPx(30));
        Log.i("pxdpsp", "30 px to sp = " + ScreenUtils.pxToSp(30));

        Log.i("oriention", "angle = " + ScreenUtils.getScreenRotationAngle(this));

        Log.i("digets", "digest abbaa lowercase=" + DigestUtils.md5Hex("abbaa", true));
        Log.i("digets", "digest abbaa upercase=" + DigestUtils.md5Hex("abbaa", false));

        Log.i("device", " cpu型号=" + DeviceUtils.getCPUModel());
        Log.i("device", " cpu最大频率=" + DeviceUtils.getCPUMaxFreqKHz());
        Log.i("device", " cpu核数=" + DeviceUtils.getNumberOfCPUCores());
        Log.i("device", " cpu是否为高通=" + DeviceUtils.isQuadCpu());
        Log.i("device", " cpu是否为单核=" + DeviceUtils.isSingleCpu());

        Log.i("device", " 总内存=" + DeviceUtils.getTotalMemory(this));
        Log.i("device", " 剩余内存=" + DeviceUtils.getFreeMemory(this));

        testAAC();

        testAnnotation();
        mRoomBtn.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);
        });

        testDrawLine();
    }
    double progress = 0;
    private void testDrawLine() {

        DrawCornerLine line = findViewById(R.id.cust_progress_bar);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                progress += 0.0025;
                if(progress > 1){
                    progress = 0;
                }
                line.update(progress);
                handler.postDelayed(this, 25);
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void testAnnotation() {
        IdInjectHelper.inject(this);//通过注解获取view
        mAnnotationBtn.setText("这是个注解的button");
        Button button = new Button(this);
        button.setId(R.id.dyn_view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        mLinearLayout.addView(button);
        IdInjectHelper.inject(this, button.getId());//通过注解获取动态view
        mDynAnnotationBtn.setText("这是个动态的注解button");
    }

    private void testAAC() {
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestAACActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testScreen() {
        String tag = "screen111";
        Log.i(tag, "full screen activity width x height = " + mRootLy.getWidth() + " x " + mRootLy.getHeight());
        Log.i(tag, ScreenUtils.getScreenWithDisplay(this).toString());
        Log.i(tag, ScreenUtils.getScreenWithMetrics(this).toString());
        Log.i(tag, ScreenUtils.getScreenWithSize(this).toString());
        Log.i(tag, "real " + ScreenUtils.getRealScreenWithSize(this).toString());
        Log.i(tag, "real " + ScreenUtils.getRealScreenWithMetrics(this).toString());
        Log.i(tag, "statusBarHeight " + ScreenUtils.getStatusBarHeight(this));
        Log.i(tag, "actionBarHeight " + ScreenUtils.getActionBarHeight(this));
        Log.i(tag, "navigationBarHeight " + (ScreenUtils.getRealScreenWithMetrics(this).mHeight - ScreenUtils.getScreenWithMetrics(this).mHeight));
    }

    private void dataBinding() {
        ItemLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_layout, mRootLy, true);
        binding.tv2.setText("branch1");
        binding.tv1.setText("0000000");
    }
}