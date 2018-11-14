package com.example.accessibility.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.example.accessibility.data.GroupManager;

public class WAAccessibilityManager implements OperateListener{
    public static final int UNIT_OPERATE_DURATION = 1000;
    public static final int SEND_MSG_OPERATE_DURATION = UNIT_OPERATE_DURATION * 5;
    public static final String WA_ACCESSIBILITY_SERVICE = "com.example.accessibility/WAAccessibilityService";
    private final Handler mHandler;
    private OperateState mState;
    private String TAG = "AcessibilityManager";
    private boolean mWorkable = false;
    private boolean mWorkStarted = false;
    private long mLastWorkTime = 0;
    private HandlerThread mHandlerThread;
    private static WAAccessibilityManager sManager;
    private WAAccessibilityService mService;
    private int mNumPerSend;
    private long mOperateDuration;
    private int mOperateCount = 0;
    private boolean mNeddReStartForNextWindowChanged = false;
    private Runnable mOperateRunnable;

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
        if(mWorkStarted || mWorkable){
            return;
        }else {
            mWorkStarted = true;
            mWorkable = true;
        }
        mNeddReStartForNextWindowChanged = false;
        start(StateConstant.JOIN_GROUP);
    }

    private void start(int state){
        mState = new OperateState();
        mState.setState(state);
        mWorkStarted = true;
        mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        onOperateStart();
        Log.d(TAG, "start() called with: state = [" + mState.getStateStr() + "]");
    }

    public void setService(WAAccessibilityService WAAccessibilityService) {
        mService = WAAccessibilityService;
        startWhatsApp(500);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public boolean work() {
        if(!mWorkable || !mWorkStarted){
            return false;
        }
        boolean hadWork = false;
        if(mState.perform(mService)){
            hadWork = true;
            mState.next();
        }
        if(hadWork){
            mLastWorkTime = System.currentTimeMillis();
            Log.d(TAG, "work() called " + mState.getStateStr() + " end");
        }
        if(mState.isEnd()){
            reset();
            Log.i(TAG, "operate end----" + mOperateCount);
            onOperateEnd();
        }
        mHandler.sendEmptyMessageDelayed(mState.getState(), mState.getDuration());
        return hadWork;
    }


    public void reset() {
        mWorkable = false;
        mWorkStarted = false;
    }

    public void enableWork(boolean enable){
        mWorkable = enable;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void destroy() {
        mHandler.removeCallbacks(mOperateRunnable);
        mHandlerThread.quitSafely();

    }

    public void startWhatsApp() {
        if(mService == null){
            return;
        }
        mNeddReStartForNextWindowChanged = true;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(GroupManager.getInstance().obtainGroup().mGroupLink));
        intent.setComponent(new ComponentName(WhatsAppConstant.WHATSAPP, WhatsAppConstant.WHATSAPP_HOME_ACTIVITY));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mService.startActivity(intent);
    }

    public void startWhatsApp(final int delay) {
        if(mOperateRunnable == null){
            mOperateRunnable = new Runnable() {
                @Override
                public void run() {
                    WAAccessibilityManager.getInstance().startWhatsApp();
                }
            };
        }
        mHandler.postDelayed(mOperateRunnable, delay);
    }

    public void updateInfo(int numPerSend, long operateDuration) {
        mNumPerSend = numPerSend;
        mOperateDuration = operateDuration;
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
        mOperateCount++;
        if(isOperateContinue()) {
            startWhatsApp(1000);
        }
    }
}
