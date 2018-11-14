package com.example.accessibility.data.excel;


public class SheetInfo {
    public int mRows;
    public int mColums;
    public int mSheetIndex;
    public String mSheetName;

    @Override
    public String toString() {
        return "sheetName=" + mSheetName + " sheetIndex=" + mSheetIndex + " rows=" + mRows + " columns=" + mColums;
    }
}
