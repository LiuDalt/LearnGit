package com.example.accessibility.data;

import android.os.Environment;
import android.util.Log;

import com.example.accessibility.data.db.DBHelper;
import com.example.accessibility.data.excel.ExcelHelper;
import com.example.accessibility.data.excel.ExcelInfo;
import com.example.accessibility.thread.ThreadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupManager {
    private static final String TAG = "GroupManager";
    private static final int MIN_GROUP_SIZE = 2;
    private static GroupManager sManager;
    private ExcelInfo mExcelInfo;
    private int mStartIndex = 0;
    private int mNumPerRead = 50;
    private List<Group> mGroups = new ArrayList<>();
    private String mExcelPath;
    private int mSheedIndex = 0;
    private Group mLastGroup;
    private int mMaxRead;
    private int mReadCount = 0;

    public static GroupManager getInstance() {
        if(sManager == null){
            synchronized (GroupManager.class){
                if(sManager == null){
                    sManager = new GroupManager();
                }
            }
        }
        return sManager;
    }


    private GroupManager() {
        mExcelPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "access.whatsapp" + File.separator + "group.xls";
    }

    public int loadData(int start, int end){
        long timeStart = System.currentTimeMillis();
        List<Group> groups = DBHelper.getInstance().query(start, end);
        mGroups.addAll(groups);
        mReadCount += groups.size();
        Log.i(TAG,  "load group size---" + mGroups.size() + " costTime=" + (System.currentTimeMillis() - timeStart));
        return mGroups.size();
    }

    public Group obtainGroup() {
        if(mGroups.size() == 0){
            return null;
        }
        mLastGroup = mGroups.remove(0);
        return mLastGroup;
    }
}
