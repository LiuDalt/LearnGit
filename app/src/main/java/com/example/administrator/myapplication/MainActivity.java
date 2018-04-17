package com.example.administrator.myapplication;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.databinding.ItemLayoutBinding;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRootLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootLy = findViewById(R.id.rootly);
        dataBinding();
    }

    private void dataBinding() {
        ItemLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_layout, mRootLy, true);
        binding.tv1.setText("master");
        binding.tv2.setText("branch1");
    }
}