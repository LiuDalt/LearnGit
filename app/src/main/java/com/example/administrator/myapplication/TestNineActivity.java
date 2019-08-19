package com.example.administrator.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TestNineActivity extends AppCompatActivity {

    private String TAG ="testChunk";
    private byte[] mOriginChunk;
    private byte[] mApptChunk;
    private byte[] mCustomChunk;
    private String mPath;
    private int mNineRes;
    private int mImgRes;
    private Object mResource [][] = new Object[][]{
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/aa.png", R.drawable.a, R.drawable.aa},
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/bb.png", R.drawable.b, R.drawable.bb},
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/cc.png", R.drawable.c, R.drawable.cc},
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/dd.png", R.drawable.d, R.drawable.dd},
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/ee.png", R.drawable.e, R.drawable.ee},
            {Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/ff.png", R.drawable.f, R.drawable.ff},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nine);

        setData(0);
        addBtn();
        work();

        testBubble();
        testPathNine();
    }

    private void testPathNine() {
        Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/b.9.png");
        if(bitmap.getNinePatchChunk() != null) {
            NinePatchDrawable npd = new NinePatchDrawable(getResources(), bitmap, bitmap.getNinePatchChunk(), new Rect(), null);
            ImageView nine1 = findViewById(R.id.path_nine1);
            nine1.setImageDrawable(npd);
            ImageView nine2 = findViewById(R.id.path_nine2);
            nine2.setImageDrawable(npd);
            ImageView nine3 = findViewById(R.id.path_nine3);
            nine3.setImageDrawable(npd);
            ImageView nine4 = findViewById(R.id.path_nine4);
            nine4.setImageDrawable(npd);
            ImageView nine5 = findViewById(R.id.path_nine5);
            nine5.setImageDrawable(npd);
            Log.i(TAG, "getNinePatchChunk with path nine");
            return;
        } else {
            Log.i(TAG, "getNinePatchChunk null with path nine");
        }
        ImageView nine1 = findViewById(R.id.path_nine1);
        nine1.setImageBitmap(bitmap);
        ImageView nine2 = findViewById(R.id.path_nine2);
        nine2.setImageBitmap(bitmap);
        ImageView nine3 = findViewById(R.id.path_nine3);
        nine3.setImageBitmap(bitmap);
        ImageView nine4 = findViewById(R.id.path_nine4);
        nine4.setImageBitmap(bitmap);
        ImageView nine5 = findViewById(R.id.path_nine5);
        nine5.setImageBitmap(bitmap);
    }

    private void testBubble() {
        RoomChatBubble bubble1 = findViewById(R.id.bubble1);
        RoomChatBubble bubble2 = findViewById(R.id.bubble2);
        RoomChatBubble bubble3 = findViewById(R.id.bubble3);
        RoomChatBubble bubble4 = findViewById(R.id.bubble4);
        RoomChatBubble bubble5 = findViewById(R.id.bubble5);

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/00/ff.png";
        bubble1.setImagePath(path);
        bubble2.setImagePath(path);
        bubble3.setImagePath(path);
        bubble4.setImagePath(path);
        bubble5.setImagePath(path);

        ImageView leftTop = findViewById(R.id.lefttop);
        leftTop.setImageBitmap(bubble1.getImgLeftTop());
        ImageView rightTop = findViewById(R.id.righttop);
        rightTop.setImageBitmap(bubble1.getImgRightTop());
        ImageView leftBottom = findViewById(R.id.leftbottom);
        leftBottom.setImageBitmap(bubble1.getImgLeftBottom());
        ImageView rightBottom = findViewById(R.id.rightbottom);
        rightBottom.setImageBitmap(bubble1.getImgRightBottom());
    }

    private void addBtn() {
        LinearLayout layout = findViewById(R.id.btn_layout);
        for(int i = 0; i < mResource.length; i++) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 10;
            params.weight = 1;
            button.setLayoutParams(params);
            button.setAllCaps(false);
            button.setText("Test" + (i + 1));
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setData(finalI);
                    work();
                }
            });
            layout.addView(button);
        }
    }

    private void setData(int i) {
        mPath = (String) mResource[i][0];
        mNineRes = (int) mResource[i][1];
        mImgRes = (int) mResource[i][2];
    }

    private void work() {
        getOriginChunk();
        getApptChunk();
        getCustomChunk(mApptChunk);

        ImageView nomal1 = findViewById(R.id.test_nine_normal);
        nomal1.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal2 = findViewById(R.id.test_nine_norma2);
        nomal2.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal3 = findViewById(R.id.test_nine_norma3);
        nomal3.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal4 = findViewById(R.id.test_nine_norma4);
        nomal4.setBackground(getResources().getDrawable(mNineRes));

        ImageView nomal11 = findViewById(R.id.normal11);
        nomal11.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal12 = findViewById(R.id.normal12);
        nomal12.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal13 = findViewById(R.id.normal13);
        nomal13.setBackground(getResources().getDrawable(mNineRes));
        ImageView nomal14 = findViewById(R.id.normal14);
        nomal14.setBackground(getResources().getDrawable(mNineRes));

        ImageView img0 = findViewById(R.id.img0);
        img0.setBackground(getResources().getDrawable(mNineRes));
        ImageView img1 = findViewById(R.id.img1);
        img1.setBackground(getResources().getDrawable(mImgRes));
        ImageView img2 = findViewById(R.id.img2);
        img2.setBackground(getResources().getDrawable(mImgRes));
        ImageView img3 = findViewById(R.id.img3);
        img3.setBackground(getResources().getDrawable(mImgRes));
        ImageView img4 = findViewById(R.id.img4);
        img4.setBackground(getResources().getDrawable(mImgRes));

        if(mApptChunk != null) {
            ImageView pathAppt1 = findViewById(R.id.path_appt1);
            pathAppt1.setBackground(getPathApptDrawable());
            ImageView pathAppt2 = findViewById(R.id.path_appt2);
            pathAppt2.setBackground(getPathApptDrawable());
            ImageView pathAppt3 = findViewById(R.id.path_appt3);
            pathAppt3.setBackground(getPathApptDrawable());
            ImageView pathAppt4 = findViewById(R.id.path_appt4);
            pathAppt4.setBackground(getPathApptDrawable());
        }

        if(mApptChunk != null) {
            ImageView pathChunk1 = findViewById(R.id.path_chunk1);
            pathChunk1.setBackground(getPathChunkDrawable());
            ImageView pathChunk2 = findViewById(R.id.path_chunk2);
            pathChunk2.setBackground(getPathChunkDrawable());
            ImageView pathChunk3 = findViewById(R.id.path_chunk3);
            pathChunk3.setBackground(getPathChunkDrawable());
            ImageView pathChunk4 = findViewById(R.id.path_chunk4);
            pathChunk4.setBackground(getPathChunkDrawable());
        }

        ImageView customChunk1 = findViewById(R.id.custom_chunk1);
        customChunk1.setBackground(getCustomChunkDrawable());
        ImageView customChunk2 = findViewById(R.id.custom_chunk2);
        customChunk2.setBackground(getCustomChunkDrawable());
        ImageView customChunk3 = findViewById(R.id.custom_chunk3);
        customChunk3.setBackground(getCustomChunkDrawable());
        ImageView customChunk4 = findViewById(R.id.custom_chunk4);
        customChunk4.setBackground(getCustomChunkDrawable());
    }

    private void getApptChunk() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        mApptChunk = bitmap.getNinePatchChunk();
        printChunk("aaptChunk", mApptChunk);
    }

    private void getOriginChunk() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), mNineRes);
        mOriginChunk = bmp.getNinePatchChunk();
        printChunk("srceChunk", mOriginChunk);
    }

    private NinePatchDrawable getPathApptDrawable() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        NinePatchDrawable npd = new NinePatchDrawable(getResources(), bitmap, mApptChunk, new Rect(), null);
        return npd;
    }

///**源码注释
// * This chunk specifies how to split an image into segments for
// * scaling.
// *
// * There are J horizontal and K vertical segments.  These segments divide
// * the image into J*K regions as follows (where J=4 and K=3):
// *
// *      F0   S0    F1     S1
// *   +-----+----+------+-------+
// * S2|  0  |  1 |  2   |   3   |
// *   +-----+----+------+-------+
// *   |     |    |      |       |
// *   |     |    |      |       |
// * F2|  4  |  5 |  6   |   7   |
// *   |     |    |      |       |
// *   |     |    |      |       |
// *   +-----+----+------+-------+
// * S3|  8  |  9 |  10  |   11  |
// *   +-----+----+------+-------+
// *
// * Each horizontal and vertical segment is considered to by either
// * stretchable (marked by the Sx labels) or fixed (marked by the Fy
// * labels), in the horizontal or vertical axis, respectively. In the
// * above example, the first is horizontal segment (F0) is fixed, the
// * next is stretchable and then they continue to alternate. Note that
// * the segment list for each axis can begin or end with a stretchable
// * or fixed segment.
//    正如源码中，注释的一样，这个NinePatch Chunk把图片从x轴和y轴分成若干个区域，F区域代表了固定，S区域代表了拉伸。
//  mDivX,mDivY描述了所有S区域的位置起始，而mColor描述了，各个Segment的颜色，
// 通常情况下，赋值为源码中定义的NO_COLOR = 0x00000001就行了。就以源码注释中的例子来说，mDivX,mDivY,mColor如下：
//    mDivX = [ S0.start, S0.end, S1.start, S1.end];
//    mDivY = [ S2.start, S2.end, S3.start, S3.end];
//    mColor = [c[0],c[1],...,c[11]]
//    对于mColor这个数组，长度等于划分的区域数,是用来描述各个区域的颜色的，而如果我们这个只是描述了一个bitmap的拉伸方式的话，是不需要颜色的，即源码中NO_COLOR = 0x00000001
// **/
    private void getCustomChunk(byte chunk[]) {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        int[] xRegions = new int[]{bitmap.getWidth() / 2, bitmap.getWidth() / 2 + 1};
        int[] yRegions = new int[]{bitmap.getWidth() / 2, bitmap.getWidth() / 2 + 1};
        int NO_COLOR = 0x00000001;
        int colorSize = 9;
        int bufferSize = xRegions.length * 4 + yRegions.length * 4 + colorSize * 4 + 32;//chunk的长度固定为84= 8 + 8 + 36 + 32

        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.nativeOrder());
        // 第一个byte，要不等于0
        byteBuffer.put((byte) 1);//index 0 无意义

        //mDivX length
        byteBuffer.put((byte) 2);//index 1 mDivX 数组长度
        //mDivY length
        byteBuffer.put((byte) 2);//index 2 mDivY 数组长度
        //mColors length
        byteBuffer.put((byte) colorSize);//index 3 mColor数组长度

//        //skip
        byteBuffer.putInt(0);//index 4-7 无意义
        byteBuffer.putInt(0);//index 8-11 无意义
//        //skip
//        byteBuffer.putInt(chunk[4]);//index 4-7 无意义
//        byteBuffer.putInt(chunk[8]);//index 8-11 无意义

//        //padding:对应.9图 右边和下边的黑线
//        byteBuffer.putInt(chunk[12]);//index 12-15 mPadding.left
//        byteBuffer.putInt(chunk[16]);//index 16-19 mPadding.right
//        byteBuffer.putInt(chunk[20]);//index 20-23 mPadding.top
//        byteBuffer.putInt(chunk[24]);//index 24-27 mPadding.bottom
        //padding:对应.9图 右边和下边的黑线
        byteBuffer.putInt(0);//index 12-15 mPadding.left
        byteBuffer.putInt(0);//index 16-19 mPadding.right
        byteBuffer.putInt(0);//index 20-23 mPadding.top
        byteBuffer.putInt(0);//index 24-27 mPadding.bottom

//        //skip
        byteBuffer.putInt(0); //index 28-31 无意义

//        // mDivX
        byteBuffer.putInt(0);//index 32-35  配置拉伸点x坐标 start
        byteBuffer.putInt(bitmap.getWidth() - 1);//index 36-39  配置拉伸点x坐标 end

        // mDivY
        byteBuffer.putInt(yRegions[0]);//index 40-43  配置拉伸点y坐标 start
        byteBuffer.putInt(yRegions[1]);//index 44-47  配置拉伸点y坐标 end

//        // mDivX
//        byteBuffer.putInt(chunk[32]);//index 32-35  配置拉伸点x坐标 start
//        byteBuffer.putInt(chunk[36]);//index 36-39  配置拉伸点x坐标 end
////
////        // mDivY
//        byteBuffer.putInt(chunk[40]);//index 40-43  配置拉伸点y坐标 start
//        byteBuffer.putInt(chunk[44]);//index 44-47  配置拉伸点y坐标 end

        // mColors
        for (int i = 0; i < colorSize; i++) {  //index 48-83  mColor 数组: 9个int
            byteBuffer.putInt(NO_COLOR);
        }
        // mColors
//        for (int i = 48; i < bufferSize; i++) {  //index 48-83  mColor 数组: 9个int
//            byteBuffer.put((byte) 0);
//        }
        mCustomChunk = byteBuffer.array();
//        mCustomChunk = chunk;
        printChunk("custChunk", mCustomChunk);
    }

    private void printChunk(String pre, byte chunk[]) {
        if(chunk == null) {
            Log.i(TAG, pre +" null");
            return;
        }
        pre += "len=" + chunk.length;
        for(byte b : chunk) {
            pre += " " + b;
        }
        Log.i(TAG, pre);
    }

    private NinePatchDrawable getPathChunkDrawable() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        NinePatchDrawable npd = new NinePatchDrawable(getResources(), bitmap, mApptChunk, new Rect(), null);
        return npd;
    }

    private NinePatchDrawable getCustomChunkDrawable() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        NinePatchDrawable npd = new NinePatchDrawable(getResources(), bitmap, mCustomChunk, new Rect(mCustomChunk[12], mCustomChunk[16], mCustomChunk[20], mCustomChunk[24]), null);
//        NinePatchDrawable npd = new NinePatchDrawable(getResources(), bitmap, mCustomChunk, new Rect(), null);
        return npd;
    }

    private NinePatchDrawable getNineDrawable(){
        try {
            InputStream stream = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/0a/test_nine.png");
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            byte[] chunk = bitmap.getNinePatchChunk();
            boolean result = NinePatch.isNinePatchChunk(chunk);
            NinePatchDrawable patchy = new NinePatchDrawable(bitmap, chunk, new Rect(), null);
            Log.i("dsgfs", "result=" + result +" drawable=" + patchy);
            return patchy;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
