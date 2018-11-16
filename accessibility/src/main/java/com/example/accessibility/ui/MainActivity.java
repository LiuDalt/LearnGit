package com.example.accessibility.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.accessibility.R;
import com.example.accessibility.data.GroupManager;
import com.example.accessibility.data.db.DBHelper;
import com.example.accessibility.service.WAAccessibilityManager;
import com.example.accessibility.service.WhatsAppConstant;
import com.example.accessibility.sharepre.SharePreferenceConstant;
import com.example.accessibility.sharepre.SharePreferenceUtils;
import com.example.accessibility.sharepre.Type;
import com.example.accessibility.thread.ThreadUtils;

public class MainActivity extends Activity {
    static String TAG = "MainActivity";
    private EditText mStartEd;
    private EditText mEndEd;
    private View mSureInput;
    private TextView mTips;
    private TextView mMaxTips;
    private View mStartService;
    private int mGroupSize;
    private TextView mHistoryTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_ly);

        initView();


    }

    private void initView() {
        mStartEd = findViewById(R.id.ed_start);
        mEndEd = findViewById(R.id.ed_end);
        mSureInput = findViewById(R.id.sure_input);
        mTips = findViewById(R.id.tips);
        mMaxTips = findViewById(R.id.max_tips);
        mStartService = findViewById(R.id.start_service);
        mHistoryTips = findViewById(R.id.history_tips);

        mStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WAAccessibilityManager.getInstance().updateInfo(mGroupSize, WhatsAppConstant.OPERATE_TIME_DEFAULT);
                WAAccessibilityManager.getInstance().openService(MainActivity.this);
            }
        });

        mSureInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int start = Integer.parseInt(mStartEd.getText().toString());
                final int end = Integer.parseInt(mEndEd.getText().toString());
                mStartEd.setEnabled(false);
                mEndEd.setEnabled(false);
                mTips.setText("正在加载数据...");
                ThreadUtils.runOnBackgroundThread(new Runnable() {
                    @Override
                    public void run() {
                         mGroupSize = GroupManager.getInstance().loadData(start, end);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mGroupSize > 0) {
                                    mStartEd.setEnabled(false);
                                    mEndEd.setEnabled(false);
                                    mTips.setText("已经成功加载 " + mGroupSize + " 条数据");
                                    mStartService.setVisibility(View.VISIBLE);
                                    SharePreferenceUtils.put(SharePreferenceConstant.LAST_START, start, Type.INTEGER);
                                    SharePreferenceUtils.put(SharePreferenceConstant.LAST_END, end, Type.INTEGER);
                                }else{
                                    mStartEd.setEnabled(true);
                                    mEndEd.setEnabled(true);
                                    mTips.setText("数据加载失败");
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mEndEd.setEnabled(false);
        mStartEd.setEnabled(false);
        mSureInput.setEnabled(false);
        mTips.setText("正在加载数据库");
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                if(DBHelper.copyDBFromFile()) {
                    final int count = DBHelper.getInstance().getCount();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEndEd.setEnabled(true);
                            mStartEd.setEnabled(true);
                            mSureInput.setEnabled(true);
                            mMaxTips.setText("数据库总条数为：" + count);
                            mTips.setText("数据库加载完毕");
                        }
                    });
                }
            }
        });
        int lastStart = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_START, 0, Type.INTEGER);
        int lastEnd = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_END, 0, Type.INTEGER);
        int groupFull = (int) SharePreferenceUtils.get(SharePreferenceConstant.GROUP_FULL_COUNT, 0, Type.INTEGER);
        int onlyManagerSend = (int) SharePreferenceUtils.get(SharePreferenceConstant.ONLY_MANAGER_SEND_MSG, 0, Type.INTEGER);
        String str = "lastStart=" + lastStart + " lastEnd=" + lastEnd + " groupFull=" + groupFull + " onlyManagerSendMsg=" + onlyManagerSend;
        mHistoryTips.setText(str);
    }
}
