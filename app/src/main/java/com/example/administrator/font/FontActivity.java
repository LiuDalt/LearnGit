package com.example.administrator.font;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

public class FontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);

        TextView textViewCus1 = findViewById(R.id.custom_text1);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"hanyi_senty_candy_color.ttf"); // create a typeface from the raw ttf
        textViewCus1.setTypeface(typeface); // apply the typeface to the textview

        TextView textViewCus2 = findViewById(R.id.custom_text2);
        Typeface typeface2 = Typeface.createFromAsset(getAssets(),"senty_chalk.ttf"); // create a typeface from the raw ttf
        textViewCus2.setTypeface(typeface2); // apply the typeface to the textview
        TextView textViewCus3 = findViewById(R.id.custom_text3);
        Typeface typeface3 = Typeface.createFromAsset(getAssets(),"HYXinXiuTiW.ttf"); // create a typeface from the raw ttf
        textViewCus3.setTypeface(typeface3); // apply the typeface to the textview
    }
}
