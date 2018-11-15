package com.example.accessibility.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.accessibility.R;
import com.example.accessibility.data.GroupManager;
import com.example.accessibility.service.WAAccessibilityManager;
import com.example.accessibility.service.WhatsAppConstant;

public class MainActivity extends AppCompatActivity {
    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.open_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WAAccessibilityManager.getInstance().openService(MainActivity.this);
            }
        });

        findViewById(R.id.test_load_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupManager.getInstance().loadData();
            }
        });
        GroupManager.getInstance().updateInfo(0,WhatsAppConstant.NUM_PER_READ_DEFAULT, 0);
        WAAccessibilityManager.getInstance().updateInfo(WhatsAppConstant.NUM_PER_READ_DEFAULT, WhatsAppConstant.OPERATE_TIME_DEFAULT);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}
