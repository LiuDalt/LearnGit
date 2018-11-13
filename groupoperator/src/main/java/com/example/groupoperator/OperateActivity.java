package com.example.groupoperator;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OperateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operate);

        findViewById(R.id.start_join_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://chat.whatsapp.com/HiX9zDiiltQ0YtUDgU5ezG"));
//                intent.setData(Uri.parse("http://chat.whatsapp.com/invite/9yP7yfxbN5P7jCeuDGsmOS"));
                intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.HomeActivity"));
                startActivity(intent);
            }
        });
    }
}
