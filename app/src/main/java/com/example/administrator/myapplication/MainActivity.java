package com.example.administrator.myapplication;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.administrator.myapplication.databinding.ItemLayoutBinding;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mRootLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRootLy = findViewById(R.id.rootly);
        dataBinding();

        test();
    }

    private void test() {
        Button button = findViewById(R.id.aaa);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runtime runtime = Runtime.getRuntime();
                File dir = Environment.getExternalStorageDirectory();
                String webpdir = dir.getAbsolutePath() +File.separator +"0webp";
                String src = dir.getAbsolutePath() + File.separator + "0img" + File.separator;
                File[] files = new File(src).listFiles();
                String dst =  dir.getAbsolutePath() + File.separator + "0temp" + File.separator + "a.webp";
                StringBuilder sb = new StringBuilder();
                String singleFileCmd = " -frame %s +%d+0+0+0-b";
                String webpMuxCmd = "%s/webpmux %s -loop %d -bgcolor 0,0,0,0 -o %s.webp";
                for (File file : files) {
                    sb.append(String.format(singleFileCmd, file.getAbsolutePath(), 1000 / 30));
                }

                try {
                    Process process = runtime.exec(String.format(webpMuxCmd, webpdir, sb, 30, dst));
                    process.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void dataBinding() {
        ItemLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_layout, mRootLy, true);
        binding.tv2.setText("branch1");
        binding.tv1.setText("master33333");
    }
}