package com.example.administrator.marquee;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import static com.example.administrator.transfer.TransferHepler.TAG;

public class MarqueeActivity extends Activity {
    int mCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);

        LiveMarqueeTextView textView = findViewById(R.id.tv_marquee);
        EditText editText = findViewById(R.id.edit_ed);
        TextView showTextTv = findViewById(R.id.show_text_tv);
        Button setTextBtn = findViewById(R.id.set_text_btn);
        Button showTextBtn = findViewById(R.id.show_text_btn);

        findViewById(R.id.start_pause_marquee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.startMarquee(2, new MarqueeAnimatorListener(2) {
                    @Override
                    public void onAnimationTime(Animator animation, int time, int maxTimes) {
                        if(time == 1){
                            textView.setText("123456");
                        }else if(time == 2){
                            textView.setText("abcdefghiujjj");
                        }
                        Log.d("mainactivity", "onAnimationTime() called with: animation = [" + animation + "], time = [" + time + "], maxTimes = [" + maxTimes + "]");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        textView.setText("金豆123455");
                    }
                });
            }
        });
        setTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(editText.getText().toString());
            }
        });

        showTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextTv.setText("[" + textView.getText() + "]");
                testToolbar();
            }
        });
        findViewById(R.id.count_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.startAnimation(2000, new LiveMarqueeTextView.UpdateListener() {
                    @Override
                    public void onUpdate(float progress) {
                        textView.setText("金豆：" + (136 + (int)(progress * 1000)));
                        Log.d(TAG, "onUpdate() called with: progress = [" + progress + "]");
                        testCustMarquee();
                    }
                }, null);
            }
        });

        testCustMarquee();
        testRankView();
    }

    private void testRankView() {
        findViewById(R.id.test_rank_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDailyRankView rankView = findViewById(R.id.live_daily_rank_view);
                rankView.startRankUpdateAnimation();
            }
        });
    }

    private void testCustMarquee() {
        TextView textView = findViewById(R.id.test_marquee_tv);
        textView.setClickable(true);
        textView.setSingleLine();
        Marquee marquee = new Marquee(textView);
        marquee.start(5);
    }

    private void testToolbar(){
        ViewStub viewStub = findViewById(R.id.vs_toolbar);
        if(viewStub != null){
            viewStub.inflate();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("123232423");
        toolbar.setNavigationIcon(R.drawable.beauty);
    }
}
