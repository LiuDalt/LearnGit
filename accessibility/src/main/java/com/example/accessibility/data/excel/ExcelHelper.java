package com.example.accessibility.data.excel;

import android.util.Log;

import com.example.accessibility.data.Group;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class ExcelHelper {
    private static String TAG = "ExcelHelper";

    public static List<Group> loadGroups(String excelFilePath, int sheetIndex, int startIndex, int num){
        List<Group> groups = new ArrayList<>();
        try {
            Workbook workbook = Workbook.getWorkbook(new File(excelFilePath));
            Sheet sheet = workbook.getSheet(sheetIndex);
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();
            if(startIndex >= sheetRows){
                startIndex = startIndex % sheetRows;
            }
            int maxIndex = startIndex + num;
            for (int i = startIndex; i < maxIndex; i++) {
                Group group = new Group();
                group.mGroupLink = sheet.getCell(0, i % sheetRows).getContents();
                group.mRowIndex = i % sheetRows;
                group.mSheetIndex = sheetIndex;
                group.mCountry = sheet.getCell(1, i % sheetRows).getContents();
                groups.add(group);
            }
            Log.i(TAG, "sheetNum=" + workbook.getNumberOfSheets() + " sheetIndex=" + sheetIndex  + " rows=" + sheetRows + " columns=" + sheetColumns);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "read excel exception " + e);
        }
        return groups;
    }

    public static ExcelInfo getExcelInfo(String excelPath) {
        ExcelInfo excelInfo = new ExcelInfo();
        excelInfo.mPath = excelPath;
        try {
            Workbook workbook = Workbook.getWorkbook(new File(excelPath));
            int size = workbook.getNumberOfSheets();
            for(int i = 0; i < size; i++){
                Sheet sheet = workbook.getSheet(i);
                SheetInfo sheetInfo = new SheetInfo();
                sheetInfo.mColums = sheet.getColumns();
                sheetInfo.mSheetName = sheet.getName();
                sheetInfo.mRows = sheet.getRows();
                sheetInfo.mSheetIndex = i;
                excelInfo.mSheetInfos.add(sheetInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelInfo;
    }
}
