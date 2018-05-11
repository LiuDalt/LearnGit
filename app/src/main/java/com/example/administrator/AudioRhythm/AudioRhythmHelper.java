package com.example.administrator.AudioRhythm;

import android.support.annotation.IntDef;

public class AudioRhythmHelper {
    public static final int STATE_BG = 0;
    public static final int STATE_BG_WAV = 1;
    public static final int STATE_PLAYED = 2;
    public static final int STATE_UNPLAY = 3;
    @IntDef({STATE_BG, STATE_BG_WAV, STATE_PLAYED, STATE_UNPLAY})
    @interface State{

    }
}
