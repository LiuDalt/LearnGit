package com.example.accessibility.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.accessibility.Constant;
import com.example.accessibility.R;
import com.example.accessibility.data.Group;
import com.example.accessibility.data.GroupManager;
import com.example.accessibility.io.FileCounstant;
import com.example.accessibility.io.FileUtils;
import com.example.accessibility.sharepre.SharePreferenceConstant;
import com.example.accessibility.sharepre.SharePreferenceUtils;
import com.example.accessibility.sharepre.Type;
import com.example.accessibility.thread.ThreadUtils;
import com.example.accessibility.time.Time;
import com.example.accessibility.time.TimeChageReceiver;
import com.example.accessibility.time.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class WAAccessibilityManager implements OperateListener{
    public static final int UNIT_OPERATE_DURATION = 500;
    public static final int SEND_MSG_OPERATE_DURATION = 5000;
    public static final String WA_ACCESSIBILITY_SERVICE = "com.example.accessibility/WAAccessibilityService";
    public static final int OPERATE_ERROR_COUNT = 5;
    private Handler mHandler;
    private OperateState mState;
    private String TAG = "AcessibilityManager";
    private boolean mWorkable = false;
    private boolean mWorkStarted = false;
    private long mLastWorkTime = 0;
    private HandlerThread mHandlerThread;
    private static WAAccessibilityManager sManager;
    private WAAccessibilityService mService;
    private int mNumPerSend;
    private long mOperateTime;//每天发消息的时间
    private int mOperateCount = 0;
    private boolean mNeddReStartForNextWindowChanged = false;
    private Runnable mOperateRunnable;
    private TimeChageReceiver mTimeReceiver;
    private boolean mHadOperateToday = false;
    private long mStartTime;
    private Group mCurrGroup;
    private int mOperateErrorCount = 0;
    private long mLastPerfomTime;
    private int mGoupFullCount = 0;
    private int mOnlyMangerSendMsgCount = 0;
    private Runnable mHeartRunnable;
    private ArrayList<String> mTextList;

    public static WAAccessibilityManager getInstance() {
        if(sManager == null){
            synchronized (WAAccessibilityManager.class){
                if(sManager == null){
                    sManager = new WAAccessibilityManager();
                }
            }
        }
        return sManager;
    }

    public void openService(Context context){
        openAccessibility(WA_ACCESSIBILITY_SERVICE, context);
    }

    private WAAccessibilityManager() {

    }



    /**
     * 该辅助功能开关是否打开了
     * @param serviceName：指定辅助服务名字
     * @param context：上下文
     * @return
     */
    private boolean isAccessibilitySettingsOn(String serviceName, Context context) {
        int accessibilityEnable = 0;
        try {
            accessibilityEnable = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        } catch (Exception e) {
            Log.e(TAG, "get accessibility enable failed, the err:" + e.getMessage());
        }
        if (accessibilityEnable == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            String settingValue = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(serviceName)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        }else {
            Log.d(TAG,"Accessibility service disable");
        }
        return false;
    }

    /**
     * 跳转到系统设置页面开启辅助功能
     * @param accessibilityServiceName：指定辅助服务名字
     * @param context：上下文
     */
    private void openAccessibility(String accessibilityServiceName, Context context){
        if (!isAccessibilitySettingsOn(accessibilityServiceName,context)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);
        }
    }

    public void start(){
        mWorkStarted = true;
        mWorkable = true;
        mNeddReStartForNextWindowChanged = false;
        start(StateConstant.INIT_STATE);
    }

    private void start(int state){
        mState = new OperateState();
        mState.setState(state, true);
        mWorkStarted = true;
        mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        onOperateStart();
        Log.d(TAG, "start() called with: state = [" + mState.getStateStr() + "]");
    }

    public void setService(WAAccessibilityService WAAccessibilityService) {
        mService = WAAccessibilityService;
        setHandler();
        registerTimeReceiver();
        checkStartWhatsApp();
//        startHeartBeat();
    }

    private void startHeartBeat() {
        if(mHeartRunnable == null) {
            mHeartRunnable = new Runnable() {
                @Override
                public void run() {
                    int lastStart = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_START, 0, Type.INTEGER);
                    int lastEnd = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_END, 0, Type.INTEGER);
                    String str = "hearbeat:operateCount=" + mOperateCount + " lastStart=" + lastStart + " lastEnd=" + lastEnd + " groupFull=" + mGoupFullCount + " onlyManagerSendMsg=" + mOnlyMangerSendMsgCount;
                    Log.i(TAG, "hearbeat--" + str);
                    FileUtils.write(str, FileCounstant.getExternalFileDir() + "heartbeat .txt");
                }
            };
        }
        ThreadUtils.runOnBackgroundThreadFixRate(mHeartRunnable, 15000);
    }

    private void setHandler() {
        mHandlerThread = new HandlerThread("TaskThread");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void handleMessage(Message msg) {
                if(mWorkable){
                    work();
                }
            }
        };
    }

    private void registerTimeReceiver() {
        mTimeReceiver = new TimeChageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);//每分钟变化
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);//设置了系统时区
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);//设置了系统时间
        mService.registerReceiver(mTimeReceiver, intentFilter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean work() {
        if(!mWorkable || !mWorkStarted){
            return false;
        }
        boolean hadWork = false;
        OperateState state = mState.hitNext(mService);
        if(state != null){
            mState = state;
            statisErrorState();
            Log.i(TAG, "work state=" + mState.getStateStr());
            mState.initNextHitState();
            hadWork = true;
        }
        if(hadWork){
            mLastWorkTime = System.currentTimeMillis();
        }
        if(mState.isEnd()){
            reset();
            onOperateEnd();
        }else{
            mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        }

        return hadWork;
    }

    private void statisErrorState() {
        if(mState.getState() == StateConstant.GROUP_FULL_OR_INVALID){
            int count = (int) SharePreferenceUtils.get(SharePreferenceConstant.GROUP_FULL_COUNT, 0, Type.INTEGER);
            count++;
            SharePreferenceUtils.put(SharePreferenceConstant.GROUP_FULL_COUNT, count, Type.INTEGER);
            mGoupFullCount++;
        }else if(mState.getState() == StateConstant.ONLY_MANAGER_SEND_MSG){
            int count = (int) SharePreferenceUtils.get(SharePreferenceConstant.ONLY_MANAGER_SEND_MSG, 0, Type.INTEGER);
            count++;
            SharePreferenceUtils.put(SharePreferenceConstant.ONLY_MANAGER_SEND_MSG, count, Type.INTEGER);
            mOnlyMangerSendMsgCount++;
        }
        SharePreferenceUtils.put(SharePreferenceConstant.OPREATE_COUNT, mOperateCount, Type.INTEGER);
    }

    public void reset() {
        mWorkable = false;
        mWorkStarted = false;
        mOperateErrorCount = 0;
    }

    public void enableWork(boolean enable){
        mWorkable = enable;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void destroy() {
        mHandler.removeCallbacks(mOperateRunnable);
        mHandlerThread.quitSafely();
        mService.unregisterReceiver(mTimeReceiver);

    }

    public void startWhatsApp() {
        if(mService == null){
            return;
        }
        mNeddReStartForNextWindowChanged = true;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mCurrGroup.mGroupLink));
        intent.setComponent(new ComponentName(WhatsAppConstant.WHATSAPP, WhatsAppConstant.WHATSAPP_HOME_ACTIVITY));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mService.startActivity(intent);
    }

    public void startWhatsApp(final int delay, final boolean needReObtainData) {
        if(mOperateRunnable == null){
            mOperateRunnable = new Runnable() {
                @Override
                public void run() {
                    if(mOperateCount == 0) {
                        mHadOperateToday = true;
                        SharePreferenceUtils.put(SharePreferenceConstant.LAST_START_OPERATE_TIME,
                                TimeUtils.getCurrTime().toSharePreferenceStr(),
                                Type.STRING);
                        SharePreferenceUtils.put(SharePreferenceConstant.GROUP_FULL_COUNT, 0, Type.INTEGER);
                        SharePreferenceUtils.put(SharePreferenceConstant.ONLY_MANAGER_SEND_MSG, 0, Type.INTEGER);
                    }
                    mStartTime = System.currentTimeMillis();
                    Log.i(TAG, "TimeStatist:start " + mOperateCount + " time=" + System.currentTimeMillis());
                    if(needReObtainData) {
                        mCurrGroup = GroupManager.getInstance().obtainGroup();
                    }
                    if(mCurrGroup == null){
                        mCurrGroup = GroupManager.getInstance().obtainGroup();
                        if(mCurrGroup == null) {
                            Log.i(TAG, "group null-----");
                            return;
                        }
                    }
                    Log.i(TAG, "startGroupLink:" + mCurrGroup.toString());
                    startWhatsApp();
                }
            };
        }
        mHandler.postDelayed(mOperateRunnable, delay);
    }

    public void updateInfo(int numPerSend, long time) {
        mNumPerSend = numPerSend;
        mOperateTime = time;
    }

    public boolean isOperateContinue() {
        return mOperateCount < mNumPerSend;
    }

    public boolean isNeddReStartForNextWindowChanged() {
        return mNeddReStartForNextWindowChanged;
    }

    @Override
    public void onOperateStart() {
    }

    @Override
    public void onOperateEnd() {
        long time = System.currentTimeMillis();
        Log.i(TAG, "TimeStatist:end " + mOperateCount + " time=" + time + " cost=" + (time - mStartTime));
        mOperateCount++;
        if(isOperateContinue()) {
            startWhatsApp(1000, true);
        }else{
            Log.i(TAG, "operateEnd-----" + mOperateCount);
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    int lastStart = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_START, 0, Type.INTEGER);
                    int lastEnd = (int) SharePreferenceUtils.get(SharePreferenceConstant.LAST_END, 0, Type.INTEGER);
                    String str = "lastStart=" + lastStart + " lastEnd=" + lastEnd + " groupFull=" + mGoupFullCount + " onlyManagerSendMsg=" + mOnlyMangerSendMsgCount;
                    Log.i(TAG, "operateStatis= " + str);
                    FileUtils.write(str, FileCounstant.getExternalFileDir() + TimeUtils.getCurrTime().toSharePreferenceStr() + " .txt");
                }
            });
        }
    }

    public void checkStartWhatsApp() {
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Time time = TimeUtils.getCurrTime();
//                if(time.mHour != mOperateTime){
//                    return;
//                }
//                String lastTimeStr = (String) SharePreferenceUtils.get(SharePreferenceConstant.LAST_START_OPERATE_TIME, "", Type.STRING);
                boolean needStart = true;
//                if(TextUtils.isEmpty(lastTimeStr)){
//                   needStart = true;
//                   Log.i(TAG, "never start---and need start");
//                }else{
//                    Time lastTime = Time.parseFromSharePreferenceStr(lastTimeStr);
//                    if(time.mDay != lastTime.mDay){
//                        needStart = true;
//                        Log.i(TAG, "had start---and need start lastStart=" + lastTime.toString());
//                    }
//                }
                if(needStart){
                    startWhatsApp(500, true);
                }
            }
        });
    }

    public String getInputText() {
        if(mTextList == null) {
            mTextList = new ArrayList<String>();
            Set<String> set = (Set<String>) SharePreferenceUtils.get(SharePreferenceConstant.TEXT_SET, null, Type.STRING_SET);
            if (set == null) {
                for(String txt : Constant.mTexts) {
                    mTextList.add(txt);
                }
            }else{
                Iterator<String> iterator = set.iterator();
                while (iterator.hasNext()){
                    mTextList.add(iterator.next());
                }
            }

        }
        Random random = new Random();
        if(mTextList.size() > 1) {
           return mTextList.get(Math.abs(random.nextInt(100) % mTextList.size()));
        }
        return mTextList.get(0);
    }
}
