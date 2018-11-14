package com.example.accessibility.data.excel;

import java.util.ArrayList;
import java.util.List;

public class ExcelInfo {
    public List<SheetInfo> mSheetInfos = new ArrayList<>();
    public String mPath;

    public int getSheetNums(){
        return mSheetInfos == null ? 0 : mSheetInfos.size();
    }

    public SheetInfo getSheetInfo(int index){
        if(index >= getSheetNums() || index < 0){
            return null;
        }
        return mSheetInfos.get(index);
    }

    @Override
    public String toString() {
        return "sheetSize=" + getSheetNums() + " sheetInfos=" + mSheetInfos.toString();
    }
}
