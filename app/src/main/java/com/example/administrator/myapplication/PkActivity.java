package com.example.administrator.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.pk.NonLinePKProgress;

public class PkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk);

        NonLinePKProgress pkProgress = findViewById(R.id.pk_progress);
        pkProgress.updatePercentA(50);
    }
}
