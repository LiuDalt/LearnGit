package com.example.administrator.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.aac.TestAACActivity;
import com.example.administrator.animate.AnimateActivity;
import com.example.administrator.drawable.DrawableActivity;
import com.example.administrator.font.FontActivity;
import com.example.administrator.json.JsonTest;
import com.example.administrator.languagecountry.LiveLanguageCountry;
import com.example.administrator.languagecountry.LiveLanguageCountryHelper;
import com.example.administrator.layoutopt.LayoutOptActivity;
import com.example.administrator.mannotation.IdInject;
import com.example.administrator.mannotation.IdInjectHelper;
import com.example.administrator.marquee.MarqueeActivity;
import com.example.administrator.myapplication.databinding.ItemLayoutBinding;
import com.example.administrator.rooms.RoomActivity;
import com.example.administrator.spannable.SpannableActivity;
import com.example.administrator.transfer.TransferHepler;
import com.example.learnmedia.CameraToMpegTest;
import com.example.learnmedia.convert.ExtractDecodeEditEncodeMuxTest2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @IdInject(R.id.test_transfer)
    private Button mTransfer;

    @IdInject(R.id.test_get_number)
    private Button mGetPhone;
    private TestData mTestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestData = new TestData();
        mTestData.map.put("123", 345);
        mTestData.map.put("345", 567);
        mRootLy = findViewById(R.id.rootly);
//        dataBinding();
        mRootLy.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                testScreen();
            }
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
        mRoomBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(intent);
        });

        testDrawLine();

        testTransfer();

        testGetNum();

        testDragRecyclerView();

        testVideoConvert();

        testVideoRecord();

        testLayoutOpt();

        testAnimate();

        testFont();

        testBottomSheetDialog();

        testDrawable();

        testSpannable();

        testMarquee();

        testGetAnr();

//        testHandler();

        testRecyclerView();

        JsonTest.test();
        String str = getResources().getString(getResources().getIdentifier("overall_tab", "string", getPackageName()));
        Log.d("getIdentifier", "str=" + str);

        testPK();

        testNine();

    }

    private void testNine() {
        findViewById(R.id.test_nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestNineActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testPK() {
        findViewById(R.id.test_pk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PkActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testRecyclerView() {
        findViewById(R.id.test_recyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestRecyclerViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testHandler() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("testHandler", "msg in 5s----");
            }
        }, 5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("testHandler", "msg in 10s----");
            }
        }, 10000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("testHandler", "firstPostDelay----");
            }
        }, 1000);
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("testHandler", "sencondPost----");
            }
        });
        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                Log.i("testHandler", "ThirdPostAtFront----");
            }
        });

    }

    private void testGetAnr() {
        findViewById(R.id.get_anr_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File dir = getFilesDir();
                        File dataDir = dir.getParentFile().getParentFile().getParentFile().getParentFile();
                        String path = dataDir.getAbsolutePath() + "/anr/";
                        File dd = new File(dataDir, "anr");
                        File[] files = dd.listFiles();
                        for(File file : files){
                            Log.i("testGetAnr", "fileName=" + file.getName());
                        }
                        Runtime mRuntime = Runtime.getRuntime();
                        try {
                            //Process中封装了返回的结果和执行错误的结果
                            Process mProcess = mRuntime.exec("ls data/anr/");
                            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
                            StringBuffer mRespBuff = new StringBuffer();
                            char[] buff = new char[1024];
                            int ch = 0;
                            while ((ch = mReader.read(buff)) != -1) {
                                mRespBuff.append(buff, 0, ch);
                            }
                            mReader.close();
                            Log.i("testGetAnr", mRespBuff.toString());
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void testMarquee() {
        findViewById(R.id.test_marquee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarqueeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testSpannable() {
        findViewById(R.id.test_spannable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SpannableActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testDrawable() {
        findViewById(R.id.test_drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        dissmissDialog();
    }

    private void dissmissDialog() {
    }

    private void testBottomSheetDialog() {
        findViewById(R.id.test_bootomsheetdialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
                Window window = dialog.getWindow();
                window.setDimAmount(0);
                View view = MainActivity.this.getLayoutInflater().inflate(R.layout.md_dialog_snack, null, false);
                BottomSheetBehavior mBehavior = BottomSheetBehavior.from(view.findViewById(R.id.snack_ly));
                mBehavior.setPeekHeight(200);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                };
                dialog.setOnKeyListener(onKeyListener);
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(5000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(MainActivity.this.isFinishing() && dialog.getWindow() == null){
                                        return;
                                    }
                                    dialog.dismiss();
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void testFont() {
        findViewById(R.id.test_font).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FontActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testAnimate() {
        findViewById(R.id.animate_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnimateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void testLayoutOpt() {
        findViewById(R.id.layout_opt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LayoutOptActivity.class);
                startActivity(intent);
            }
        });
    }


    public static void setLanguage(Context context, String languageCode, String countryCode){
        Locale locale = new Locale(languageCode, countryCode);
        Resources resources = context.getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }

    private void testVideoRecord() {
        findViewById(R.id.test_record_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new CameraToMpegTest().testEncodeCameraToMp4();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void testVideoConvert() {
        findViewById(R.id.test_convert_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new ExtractDecodeEditEncodeMuxTest2().testExtractDecodeEditEncodeMuxAudioVideo();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void testDragRecyclerView() {
        findViewById(R.id.test_drag_recyclerView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecyclerViewDragActivity.class);
                startActivity(intent);
            }
        });
    }

    String TAG = "testGetNum";
    private void testGetNum() {
        findViewById(R.id.test_get_number).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View v) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "no permission---");
                    return;
                }
                String num = telephonyManager.getLine1Number();//获取当前正在使用的手机号码
                Toast.makeText(MainActivity.this, num, Toast.LENGTH_LONG).show();
                Log.i(TAG, "num=" + num);
                try {
                    SubscriptionManager subscriptionManager = SubscriptionManager.from(MainActivity.this);
                    Method getSubIdMethod = subscriptionManager.getClass().getDeclaredMethod("getSubId",new Class[]{int.class});
                    Class telephonyManagerClass = telephonyManager.getClass();
                    Method method = telephonyManagerClass.getMethod("getLine1Number", new Class[]{int.class});
                    int[] sub1 = (int[]) getSubIdMethod.invoke(subscriptionManager,0);
                    if(sub1 != null) {//getLine1Number获取卡槽1的手机号码
                        Log.i(TAG, "sub1 num=" + sub1[0] +  " " + method.invoke(telephonyManager, sub1[0]));
                    }
                    int[] sub2 = (int[]) getSubIdMethod.invoke(subscriptionManager,1);
                    if(sub2 != null){//getLine1Number获取卡槽2的号码
                        Log.i(TAG, "sub2 num=" + sub2[0] + "  " + method.invoke(telephonyManager, sub2[0]));
                    }
                    //获取当前激活的手机sim卡信息
                    List<SubscriptionInfo> infoList = subscriptionManager.getActiveSubscriptionInfoList();
                    Log.i(TAG, "active infolist=" + infoList.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bool1() || bool2() || bool3()){

        }
        testJSon();
    }

    private void testJSon() {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject tmpObj = null;
            for(int i = 0; i < 3; i++)
            {
                tmpObj = new JSONObject();
                tmpObj.put("name" , "name" + i);
                tmpObj.put("tete" , "tt" + i);
                jsonArray.put(tmpObj);
            }
            String personInfos = jsonArray.toString(); // 将JSONArray转换得到String
            jsonObject.put("personInfos" , personInfos);   // 获得JSONObject的String
            Log.e("tagtag", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void testTransfer() {
        mTransfer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TransferHepler.test();
                    }
                }).start();

            }
        });
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

    public static class TestData implements Parcelable{
        public Map map = new HashMap<String, Integer>();

        protected TestData(Parcel in) {
            in.readMap(map, HashMap.class.getClassLoader());
        }

        public TestData(){}

        public static final Creator<TestData> CREATOR = new Creator<TestData>() {
            @Override
            public TestData createFromParcel(Parcel in) {
                return new TestData(in);
            }

            @Override
            public TestData[] newArray(int size) {
                return new TestData[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeMap(map);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("testData", mTestData.toString());
        outState.putParcelable("testdata", mTestData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTestData = savedInstanceState.getParcelable("testdata");
        Log.i("testData", mTestData.toString());
    }

    public boolean bool2(){
        Log.d(TAG, "bool2() called");
        return true;
    }

    public boolean bool3(){
        Log.d(TAG, "bool3() called");
        return true;
    }
    public boolean bool1(){
        Log.d(TAG, "bool1() called");
        return true;
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
        try{
            int a = 1 / 0;
            button.setText(a);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void testAAC() {
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestAACActivity.class);
                startActivity(intent);
                LiveLanguageCountry.paseFromJson(LiveLanguageCountryHelper.sTestJson);
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