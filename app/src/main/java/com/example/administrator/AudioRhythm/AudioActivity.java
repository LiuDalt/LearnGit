package com.example.administrator.AudioRhythm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.administrator.myapplication.R;

import java.util.Random;

public class AudioActivity extends AppCompatActivity {

    private AudioRhythmView mAudioView;
    private FrameLayout mSlideLy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mAudioView = findViewById(R.id.audio_rhythm_view);

        findViewById(R.id.show_wavs).setOnClickListener(v -> {
            mAudioView.setMusicPath("", 0);
        });

        findViewById(R.id.change_unavailable).setOnClickListener(v -> {
            mAudioView.updateUnavailableTime(new Random().nextInt(8) * 1000, true);
        });

    }
}
