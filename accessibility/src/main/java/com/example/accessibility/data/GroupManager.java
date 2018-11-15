package com.example.accessibility.data;

import android.os.Environment;
import android.util.Log;

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

    public void updateInfo(int sheetIndex, int maxRead, int startIndex){
        mSheedIndex = sheetIndex;
        mStartIndex = startIndex;
        mMaxRead = maxRead;
        if(mNumPerRead > mMaxRead){
            mNumPerRead = mMaxRead;
        }
        mGroups.clear();
        loadData();
    }

    private GroupManager() {
        mExcelPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "access.whatsapp" + File.separator + "group.xls";
    }

    public void init(){
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                mExcelInfo = ExcelHelper.getExcelInfo(mExcelPath);
                Log.i(TAG, "excelInfo::" + mExcelInfo.toString());
            }
        });
    }

    public void loadData(){
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                List<Group> groups = ExcelHelper.loadGroups(mExcelPath, mSheedIndex, mStartIndex, mNumPerRead);
                mStartIndex = (mStartIndex + groups.size()) % mExcelInfo.getSheetInfo(mSheedIndex).mRows;
                mGroups.addAll(groups);
                mReadCount += groups.size();
                Log.i(TAG, groups.toString());
            }
        });
    }

    public Group obtainGroup() {
        mLastGroup = mGroups.remove(0);
        if(mGroups.size() <= MIN_GROUP_SIZE && mReadCount < mMaxRead){
            loadData();
        }
        return mLastGroup;
    }
}
