package com.example.administrator.animate;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.administrator.myapplication.R;


public class CaptionAnimateHelper2 implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener{

    private static final String TAG = "AnimateHelper";
    private static final float SCALE_DISTANCE = 10;//放缩的条件为两次点距离差大于SCALE_DISTANCE
    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 20f;
    private static final int NONE = 0;
    private static final int TRANSLATE = 1;//仅平移
    private static final int COMBINED = 2;//组合的，包括平移，旋转，缩放
    private static final float NEAR_DISTANCE = 20;
    private static final float TOUCH_SCALE = 0.5f;//常规触摸区域相对于字体区域增大的比例
    private float mCurrScale = 1f;
    private int mMode = NONE;
    private ViewGroup mContainer;
    private TextView mView;
    private int mMaxX;
    private int mMaxY;
    private PointF mViewOriginPoint = new PointF();//view初始的xy坐标
    private PointF mLastPoint[] = new PointF[]{
            new PointF(), new PointF()
    };//上一次两个触摸点手指的坐标
    private float mCurrRotation = 0f;//当前旋转角度
    private PointF[] mCurrPoint;//当前两个触摸点坐标
    private boolean mIsCombinedToTranslate;//是否从两个触摸点变为一个触摸点：此时对应：组合模式->平移模式
    private View mLeftBottomView;
    private View mRightBottomView;
    private View mLeftTopView;
    private View mRightTopView;
    private AnimateListener mListener;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;
    private boolean mIsJustReset = true;

    public void setView(ViewGroup container, TextView view) {
        mView = view;
        mContainer = container;

        mLeftTopView = container.findViewById(R.id.left_top_view);
        mRightTopView = mContainer.findViewById(R.id.right_top_view);
        mRightBottomView = mContainer.findViewById(R.id.right_bottom_view);
        mLeftBottomView = mContainer.findViewById(R.id.left_bottom_view);

        mGestureDetector = new GestureDetector(container.getContext(), this);
        mScaleGestureDetector = new ScaleGestureDetector(container.getContext(), this);

        initView();
    }

    public void setViewScale(float viewScale){
        mView.setScaleX(viewScale);
        mView.setScaleY(viewScale);
        Log.d(TAG, "setScale() called with: scale = [" + viewScale + "]");
    }

    private void initView() {
        mContainer.setOnTouchListener(this);
        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                    mContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else{
                    mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mMaxX = mContainer.getWidth();
                mMaxY = mContainer.getHeight();
                mViewOriginPoint.x = mView.getX();
                mViewOriginPoint.y = mView.getY();
            }
        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            mIsJustReset = true;
            return false;
        }
        return mGestureDetector.onTouchEvent(event) || mScaleGestureDetector.onTouchEvent(event);
//        int action = event.getActionMasked();
//        printAction(event);
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                if(isInTouchRect(new PointF(event.getX(), event.getY()))) {
//                    mMode = TRANSLATE;
//                    mLastPoint[0].x = event.getX();
//                    mLastPoint[0].y = event.getY();
//                    notifyAnimateStart();
//                }
//                return true;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                getMultiPoint(event, mLastPoint);
//                if(isInTouchRect(mLastPoint)) {
//                    mCurrRotation = getAngle(mLastPoint[0], mLastPoint[1]);
//                    mMode = COMBINED;
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                if(event.getPointerCount() == 2 && mMode == COMBINED){
//                    mMode = TRANSLATE;
//                    mIsCombinedToTranslate = true;
//                }
//                return true;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                mMode = NONE;
//                notifyAnimateEnd();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                onMove(event);
//                return true;
//        }
//
//        mIsCombinedToTranslate = false;
//        return false;
    }

    private void notifyAnimateStart() {
        if(mListener != null){
            mListener.onAnimateStart();
        }
    }

    private void notifyAnimateEnd() {
        if(mListener != null){
            mListener.onAnimateEnd();
        }
    }

    private boolean isNearView(float x, float y) {
        if(mView.getX() - x > NEAR_DISTANCE){
            return false;
        }
        if(x - mView.getX() - mView.getWidth() > NEAR_DISTANCE){
            return false;
        }
        if(mView.getY() - y > NEAR_DISTANCE){
            return false;
        }
        if(y - mView.getY() - mView.getHeight() > NEAR_DISTANCE){
            return false;
        }
        return true;
    }


    private void getMultiPoint(MotionEvent event, PointF[] pointFS) {
        pointFS[0].x = event.getX(0);
        pointFS[0].y = event.getY(0);
        pointFS[1].x = event.getX(1);
        pointFS[1].y = event.getY(1);
    }

    private float calculateDistance(PointF first, PointF second){
        float xDis = first.x - second.x;
        float yDis = first.y - second.y;
        return (float) Math.sqrt(xDis * xDis + yDis * yDis);
    }

    int angle = -30;

    public void setAngle(int angle) {
        this.angle = angle;
        setRotatation(angle);
        isInTouchRect(new PointF());
    }

    private boolean isInTouchRect(PointF... pointS){
        if(pointS == null || pointS.length == 0){
            return false;
        }
        float touchW = mView.getWidth() * (1 + TOUCH_SCALE);
        float touchH = mView.getHeight() * (1 + TOUCH_SCALE);
        //以原始view中心作为坐标原点
        PointF originLeftTop = new PointF(-touchW / 2,  -touchH / 2);
        PointF originRightTop  = new PointF(touchW / 2, -touchH / 2);
        PointF originRightBottom  = new PointF(touchW / 2,touchH / 2);
        PointF originLeftBottom  = new PointF(-touchW / 2, touchH / 2);

        //先计算放缩后的坐标
        scalePoint(originLeftTop, mCurrScale);
        scalePoint(originRightTop, mCurrScale);
        scalePoint(originRightBottom, mCurrScale);
        scalePoint(originLeftBottom, mCurrScale);

        //再计算旋转后坐标
        originLeftTop = rotatePoint(originLeftTop, mView.getRotation());
        originRightTop = rotatePoint(originRightTop, mView.getRotation());
        originRightBottom = rotatePoint(originRightBottom, mView.getRotation());
        originLeftBottom = rotatePoint(originLeftBottom, mView.getRotation());

        float xTranslate, yTranslate;
        //view原始中心点平移
        xTranslate = mView.getX() + mView.getWidth() / 2;
        yTranslate = mView.getY() + mView.getHeight() / 2;
        translatePoint(originLeftTop, xTranslate, yTranslate);
        translatePoint(originRightTop, xTranslate, yTranslate);
        translatePoint(originRightBottom, xTranslate, yTranslate);
        translatePoint(originLeftBottom, xTranslate, yTranslate);

        Path path = new Path();
        path.moveTo(originLeftTop.x, originLeftTop.y);
        path.lineTo(originRightTop.x, originRightTop.y);
        path.lineTo(originRightBottom.x, originRightBottom.y);
        path.lineTo(originLeftBottom.x, originLeftBottom.y);
        path.close();

        mLeftTopView.setX(originLeftTop.x - mLeftTopView.getWidth() / 2);
        mLeftTopView.setY(originLeftTop.y - mLeftTopView.getHeight() / 2);

        mRightTopView.setX(originRightTop.x - mRightTopView.getWidth() /  2);
        mRightTopView.setY(originRightTop.y - mRightTopView.getWidth() / 2);

        mRightBottomView.setX(originRightBottom.x - mRightBottomView.getWidth() / 2);
        mRightBottomView.setY(originRightBottom.y - mRightBottomView.getHeight() / 2);

        mLeftBottomView.setX(originLeftBottom.x - mLeftBottomView.getWidth() / 2);
        mLeftBottomView.setY(originLeftBottom.y - mLeftBottomView.getHeight() / 2);

//        Log.d(TAG, "isInTouchRect() (" + originLeftTop.x + ", " + originLeftTop.y + ") ("
//                + originRightTop.x + ", " + originRightTop.y + ") ("
//                + originRightBottom.x + ", " + originRightBottom.y + ") ("
//                + originLeftBottom.x + ", " + originLeftBottom.y + ")  " + pointS.toString());

        for(PointF point : pointS){
            if(isInRegion(point, path)){
                return true;
            }
        }

        if(pointS.length == 2){
            if(isInterset(pointS[0], pointS[1], originLeftTop, originRightTop)){
                return true;
            }
            if(isInterset(pointS[0], pointS[1], originRightTop, originRightBottom)){
                return true;
            }
            if(isInterset(pointS[0], pointS[1], originRightBottom, originLeftBottom)){
                return true;
            }
            if(isInterset(pointS[0], pointS[1], originLeftBottom, originLeftTop)){
                return true;
            }
        }

        return false;
    }

    public boolean isInterset(PointF p1, PointF p2, PointF p3, PointF p4) {
        Log.d(TAG, "isInterset() called with: p1 = [" + p1 + "], p2 = [" + p2 + "], p3 = [" + p3 + "], p4 = [" + p4 + "]");
        double x1 = p1.x, y1 = p1.y, x2 = p2.x, y2 = p2.y;
        double x3 = p3.x, y3 = p3.y, x4 = p4.x, y4 = p4.y;

        float maxX12 = findMaxMinXY(true, true, p1, p2);
        float minX12 = findMaxMinXY(true, false, p1, p2);
        float maxX34 = findMaxMinXY(true, true, p3, p4);
        float minX34 = findMaxMinXY(true, false, p3, p4);

        float maxY12 = findMaxMinXY(false, true, p1, p2);
        float minY12 = findMaxMinXY(false, false, p1, p2);
        float maxY34 = findMaxMinXY(false, true, p3, p4);
        float minY34 = findMaxMinXY(false, false, p3, p4);

        if(maxY12 < minY34){
            return false;
        }
        if(maxY34 < minY12){
            return false;
        }

        if(maxX12 < minX34){
            return false;
        }
        if(maxX34 < minX12){
            return false;
        }

        if(x1 == x2 && x3 == x4){
            if(x1 != x3){
                return false;
            }
            return true;
        }
        double a = (y1 - y2) / (x1 - x2);
        double b = (x1 * y2 - x2 * y1) / (x1 - x2);
        Log.d(TAG, "isInterset 直线方程为: y=" + a + "x + " + b);

        double c = (y3 - y4) / (x3 - x4);
        double d = (x3 * y4 - x4 * y3) / (x3 - x4);
        Log.d(TAG, "isInterset 直线方程为: y=" + c + "x + " + d);
        if(a == c){
            if(b != d){
                return false;
            }
            return true;
        }
        double x = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
//        double y = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
//                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));
//        Log.d(TAG, "isInterset 他们的交点为: (" + x + "," + y + ")");
        if(x <= maxX12 && x >= minX12 && x <= maxX34 && x >= minX34){
            return true;
        }
        return false;
    }

    private float findMaxMinXY(boolean findX, boolean isFindMax, PointF... pointFS) {
        float dst = pointFS[0].y;
        if(findX){
            dst = pointFS[0].x;
        }
        for(PointF pointF : pointFS){
            if(findX && isFindMax) {
                if (pointF.x > dst) {
                    dst = pointF.x;
                }
            }else if(findX && !isFindMax){
                if (pointF.x < dst) {
                    dst = pointF.x;
                }
            }else if(!findX && isFindMax){
                if (pointF.y > dst) {
                    dst = pointF.y;
                }
            }else{
                if (pointF.y < dst) {
                    dst = pointF.y;
                }
            }
        }
        return dst;
    }

    private void translatePoint(PointF point, float xTranslate, float yTranslate) {
        point.x = point.x + xTranslate;
        point.y = point.y + yTranslate;
    }

    public PointF rotatePoint(PointF p, float angle) {
        float arc = (float) ((angle * Math.PI) / 180);
        float cosv = (float) Math.cos(arc);
        float sinv = (float) Math.sin(arc);
        float newX =  p.x * cosv - p.y * sinv;
        float newY =  p.x * sinv + p.y* cosv;
        return new PointF(newX, newY);
    }

    private PointF scalePoint(PointF point, float scale) {
        point.y = point.y * scale;
        point.x = point.x * scale;
        return point;
    }

    public boolean isInRegion(PointF point, Path path){
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right,(int) rectF.bottom));
        if (region.contains((int)point.x, (int)point.y)) {
            return true;
        }
        return false;
    }

    private void printAction(MotionEvent event) {
        int count = event.getPointerCount();
        for(int i = 0; i < count; i++){
            Log.d(TAG + "_print", "point=" + i + " x=" + event.getX(i) + " y=" + event.getY(i) + " action=" + event.getAction() + " actionIndex=" + event.getActionIndex() + " maxX=" + mMaxX + " mMaxY=" + mMaxY);
        }
    }

    private void onMove(MotionEvent event) {
        if(mMode == NONE){
            Log.d(TAG, "onMove NOne");
            return;
        }
        if(mIsCombinedToTranslate){
            mIsCombinedToTranslate = false;
            mLastPoint[0].x = event.getX();
            mLastPoint[0].y = event.getY();
            return;
        }
        if(mMode == COMBINED) {
            mCurrPoint = new PointF[]{new PointF(), new PointF()};
            getMultiPoint(event, mCurrPoint);
        }
        handleTranslate(event);
        if(mMode == COMBINED){
            handleScale(event);
            handleRotate(event);
        }
        mLastPoint = mCurrPoint;
        isInTouchRect(new PointF());
        Log.d(TAG +"_view", "x=" + mView.getX() + " y=" + mView.getY() + " w=" + mView.getWidth()
                + " h=" + mView.getHeight() + " viewR=" + mView.getRotation() + " currR=" + mCurrRotation + " currScale=" + mCurrScale);
    }

    private void handleRotate(MotionEvent event) {
        getMultiPoint(event, mCurrPoint);
        float rotation = getAngle(mCurrPoint[0], mCurrPoint[1]);
        if(mIsJustReset){
            mCurrRotation = rotation;
            mIsJustReset = false;
            return;
        }
        float dis = rotation - mCurrRotation;
        if(Math.abs(dis) >= 0){
            setRotatation(dis);
            Log.d(TAG, "handleRotate() lastRotation=" + mCurrRotation + " currRotation=" + rotation + " dis=" + dis);
            mCurrRotation = rotation;
        }
    }

    private void setRotatation(float rotation) {
        mView.setRotation(mView.getRotation() + rotation);
        Log.d(TAG, "setRotatation() called with: rotation = [" + rotation + "]");
    }

    public float getAngle(PointF first, PointF second) {
        double delta_x = (first.x - second.x);
        double delta_y = (first.y - second.y);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private void handleScale(MotionEvent event) {
        float lastDis = calculateDistance(mLastPoint[0], mLastPoint[1]);
        float currDis = calculateDistance(mCurrPoint[0], mCurrPoint[1]);
        if(currDis - lastDis > SCALE_DISTANCE){
            if(mCurrScale < 2){
                mCurrScale += 0.05f;
            }else if (mCurrScale < 6){
                mCurrScale += 0.1f;
            }else if (mCurrScale < 12){
                mCurrScale += 0.15f;
            }else if(mCurrScale < 20){
                mCurrScale += 0.2f;
            }else{
                mCurrScale += 0.25f;
            }
            if(mCurrScale > MAX_SCALE){
                mCurrScale = MAX_SCALE;
            }else{
                mLastPoint = mCurrPoint;
            }
            setViewScale(mCurrScale);
        }else if(currDis - lastDis < -SCALE_DISTANCE){
            if(mCurrScale < 2){
                mCurrScale -= 0.05f;
            }else if (mCurrScale < 6){
                mCurrScale -= 0.1f;
            }else if (mCurrScale < 12){
                mCurrScale -= 0.15f;
            }else if(mCurrScale < 20){
                mCurrScale -= 0.2f;
            }else{
                mCurrScale -= 0.25f;
            }
            if(mCurrScale < MIN_SCALE){
                mCurrScale = MIN_SCALE;
            }else{
                mLastPoint = mCurrPoint;
            }
            setViewScale(mCurrScale);
        }
    }




    private void handleTranslate(MotionEvent event) {
        float disX, disY;
        if(mMode == TRANSLATE){
            disX = event.getX() - mLastPoint[0].x;
            disY = event.getY() - mLastPoint[0].y;
            mLastPoint[0].x = event.getX();
            mLastPoint[0].y = event.getY();
        }else{
            PointF lastCenter = getCenterPoint(mLastPoint[0], mLastPoint[1]);
            PointF currCenter = getCenterPoint(mCurrPoint[0], mCurrPoint[1]);
            disX = currCenter.x - lastCenter.x;
            disY = currCenter.y - lastCenter.y;
        }
        setTranslate(disX, disY);
    }

    private void setTranslate(float disX, float disY) {
        mView.setX(mView.getX() + disX);
        mView.setY(mView.getY() + disY);
        Log.d(TAG, "setTranslate() called with: disX = [" + disX + "], disY = [" + disY + "]");
    }

    private PointF getCenterPoint(PointF first, PointF second) {
        PointF pointF = new PointF();
        pointF.x = (first.x + second.x) / 2;
        pointF.y = (first.y + second.y) / 2;
        return pointF;
    }

    public Bitmap getBitmap() {
        mContainer.destroyDrawingCache();
        mContainer.setDrawingCacheEnabled(true);
        mContainer.buildDrawingCache();
        Bitmap bitmap = mContainer.getDrawingCache();
        return bitmap;
    }

    public void setListener(AnimateListener listener) {
        mListener = listener;
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
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, e1.getPointerCount() + "  " + e2.getPointerCount() + " onScroll() called with: e1 = [" + e1 + "], e2 = [" + e2 + "], distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
        handleTranslateForScroll(distanceX, distanceY);
        handleRotateForScroll(e2);
        return false;
    }

    private void handleTranslateForScroll(float distanceX, float distanceY) {
        mView.setX(mView.getX() - distanceX);
        mView.setY(mView.getY() - distanceY);
        Log.d(TAG, "handleTranslateForScroll() called with: distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
    }

    private void handleRotateForScroll(MotionEvent e2) {
        handleRotate(e2);
//        if(e2.getPointerCount() < 2){
//            return;
//        }
//        float rotate = getAngle(new PointF(e2.getX(0), e2.getY(0)), new PointF(e2.getX(1), e2.getY(1)));
//        if(mCurrRotation == 0){
//            mCurrRotation = rotate;
//        }
//        float rotateDis = rotate - mCurrRotation;
//        Log.d(TAG, "handleRotateForScroll() called with: rotate=" + mView.getRotation() + " rotateDis=" + rotateDis);
//        if(Math.abs(rotateDis) > 15){
//            rotateDis = 2;
//        }
////        if(rotateDis > 0) {
////            mView.setRotation(mView.getRotation() + 2f);
////        }else if(rotateDis < 0){
////            mView.setRotation(mView.getRotation() - 2f);
////        }
//        mView.setRotation(mView.getRotation() + rotateDis);
//        mCurrRotation += rotateDis;

    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(TAG, "onLongPress() called with: currRotation=" + mCurrRotation);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(TAG, "onScale() called with: detector = [" + detector.getScaleFactor() + "]");
        handleScaleForDetect(detector);

        return false;
    }

    private void handleScaleForDetect(ScaleGestureDetector detector) {
        float scale = detector.getScaleFactor();
        if(mCurrScale == 0){
            mCurrScale = scale;
        }
        mView.setScaleX(mView.getScaleX() + scale - mCurrScale);
        mView.setScaleY(mView.getScaleY() + scale - mCurrScale);
        mCurrScale = scale;
        Log.d(TAG, "handleScaleForDetect() called with: detector = [" + detector + "] scale=" + mView.getScaleX());
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleBegin() called with: detector = [" + detector + "]");
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d(TAG, "onScaleEnd() called with: detector = [" + detector.getScaleFactor() + "]");
        handleScaleForDetect(detector);
//        mCurrRotation = 0;
        mCurrScale = 0;
    }

    interface AnimateListener {
        void onAnimateStart();
        void onAnimating();
        void onAnimateEnd();
    }
}
