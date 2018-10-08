package com.example.administrator.layoutopt;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;

public class LayoutOptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_opt);

        testViewStub();

        testToBitmap();
    }

    private void testToBitmap() {
        findViewById(R.id.test_img_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = findViewById(R.id.opt_rootly);
                view.setDrawingCacheEnabled(true);

                view.buildDrawingCache();

                Bitmap bitmap=view.getDrawingCache();
                ImageView imageView = findViewById(R.id.test_img_view);
                imageView.setImageBitmap(bitmap);
            }
        });

    }

    private void testViewStub() {
        findViewById(R.id.text_viewstub).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStub viewStub = findViewById(R.id.viewstub_inflate);
                if(viewStub != null) {//当inflate之后为空，可以以此判断是否inflate过
                    Log.i("testViewStub__", "viewStub is not inflated and inflate now");
                    viewStub.inflate();
                }else{
                    Log.i("testViewStub__", "viewStub is inflated and get View=" + findViewById(R.id.viewstub_view).toString());
                }
            }
        });
    }

}
