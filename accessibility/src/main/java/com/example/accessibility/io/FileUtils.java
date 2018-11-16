package com.example.accessibility.io;

import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static boolean write(String str, String filePath){
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(filePath);
            if(!TextUtils.isEmpty(str)) {
                fos.write(str.getBytes());
            }else{
                fos.write("nothing".getBytes());
            }
            fos.close();
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}
