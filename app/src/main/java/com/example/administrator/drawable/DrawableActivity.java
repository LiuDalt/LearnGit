package com.example.administrator.drawable;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.myapplication.R;

public class DrawableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        ImageView img1 = findViewById(R.id.drawableimg1);
        ImageView img2 = findViewById(R.id.drawableimg2);
        ImageView img3 = findViewById(R.id.drawableimg3);

    }
}
