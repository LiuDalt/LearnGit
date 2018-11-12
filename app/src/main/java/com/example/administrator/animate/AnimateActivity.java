package com.example.administrator.animate;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

public class AnimateActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    static String TAG = "AnimateActivity";
    private EditText mTextView;
    private ViewGroup mContainer;
    private CaptionAnimateHelper helper;
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mLastRotate;
    private View.OnClickListener mTvClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);

        mTextView = findViewById(R.id.test_animate_tv);
        mContainer = findViewById(R.id.test_animate_container);

        mDetector = new GestureDetector(this, this);
        mScaleGestureDetector = new ScaleGestureDetector(this, this);
//        mContainer.setOnTouchListener(this);
        mTvClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCaptionTvClick();
            }
        };
        mTextView.clearFocus();
        mTextView.setEnabled(false);
        helper = new CaptionAnimateHelper();
        helper.setView(mContainer, mTextView);

//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("animateActivity", "onClick() called with: v = [" + v + "]");
//            }
//        });
        findViewById(R.id.test_show_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContainerBitmap();
                PointF pointF  = new PointF();
                pointF.x = 100;
                pointF.y = -100;
                pointF = helper.rotatePoint(pointF, 180);
                Log.i("testRotate11", "rotate x=" + pointF.x + " " + pointF.y);
                helper.setAngle(60);

                Path path = new Path();
                path.moveTo(100, 100);
                path.lineTo(-100, 100);
                path.lineTo(-100, -100);
                path.lineTo(100, -100);
                path.close();
                Log.d("testContains111", "isInregion=" + helper.isInRegion(new PointF(0, 0), path));

                PointF p1 = new PointF(0, 0);
                PointF p2 = new PointF(1, 1);
                PointF p3 = new PointF(0, 1);
                PointF p4 = new PointF(1, 0);
                Log.d("testInterset22","isInterset=" + helper.isInterset(p1, p2, p3, p4));
            }
        });

        findViewById(R.id.animate_revert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.revertMove();
            }
        });
        findViewById(R.id.animate_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.restoreMove();
            }
        });
        String stringArr[] = new String[5];
        stringArr[0] = mTextView.getText().toString();
        for(int i = 1; i < 5; i++){
            stringArr[i] = stringArr[i - 1] +  mTextView.getText().toString();
        }
        findViewById(R.id.addtext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(stringArr[index]);
                index++;
                index = index % 5;
                helper.printInfo();
            }
        });
        findViewById(R.id.deletetext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTextView.setText(stringArr[index]);
//                index--;
//                index = index % 5;
                View vi = findViewById(R.id.addtext);
                Log.d("settranslate", "viY=" + vi.getY());
//                vi.setTranslationY(100);
                vi.setScaleY(0.5f);
                Log.d("settranslate", "viY=" + vi.getY());
            }
        });
    }

    int index = 1;

    private void onCaptionTvClick() {

    }

    private static PointF calcNewPoint(PointF p, PointF pCenter, float angle) {
            // calc arc
            float l = (float) ((angle * Math.PI) / 180);

            //sin/cos value
            float cosv = (float) Math.cos(l);
            float sinv = (float) Math.sin(l);

            // calc new point
            float newX = (float) ((p.x - pCenter.x) * cosv - (p.y - pCenter.y) * sinv + pCenter.x);
            float newY = (float) ((p.x - pCenter.x) * sinv + (p.y - pCenter.y) * cosv + pCenter.y);
            return new PointF(newX, newY);
        }

    private void getContainerBitmap() {
        ImageView imageView = findViewById(R.id.test_animate_iv);
        imageView.setImageBitmap(helper.getBitmap());
    }


    @Override
    public boolean onDown(MotionEvent e) {
        Log.d(TAG, "onDown() called with: e = [" + e + "]");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d(TAG, "onShowPress() called with: e = [" + e + "]");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d(TAG, "onSingleTapUp() called with: e = [" + e + "]");
        Toast.makeText(this, "这里是点击效果",Toast.LENGTH_SHORT);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, e1.getPointerCount() + "  " + e2.getPointerCount() + " onScroll() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
        mTextView.setX(mTextView.getX() - distanceX);
        mTextView.setY(mTextView.getY() - distanceY);
        if(e2.getPointerCount() < 2){
            return false;
        }
        float rotate = helper.getAngle(new PointF(e2.getX(0), e2.getY(0)), new PointF(e2.getX(1), e2.getY(1)));
        if(mLastRotate == 0){
            mLastRotate = rotate;
        }
        float rotateDis = rotate - mLastRotate;
        mTextView.setRotation(mTextView.getRotation() + rotateDis);
        Log.d("onscall_angel", " " + (rotate - mLastRotate));
        mLastRotate = rotate;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress() called with: e = [" + e + "]");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG, "onScale() called with: detector = [" + detector.getScaleFactor() + "]");
        mTextView.setScaleX(detector.getScaleFactor());
        mTextView.setScaleY(detector.getScaleFactor());
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleBegin() called with: detector = [" + detector + "]");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd() called with: detector = [" + detector.getScaleFactor() + "]");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mDetector.onTouchEvent(event) || mScaleGestureDetector.onTouchEvent(event);
    }
}
