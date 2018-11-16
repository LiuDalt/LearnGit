package com.example.accessibility.io;

import android.os.Environment;

import java.io.File;

public class FileCounstant {
    public static final String EXTERNAL_FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "access.whatsapp" + File.separator;
    public static String getExternalFileDir(){
        File dir = new File(EXTERNAL_FILE_DIR);
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }
        return EXTERNAL_FILE_DIR;
    }
}
