package com.example.accessibility.data;


public class Group {
    public String mGroupLink;
    public int mSheetIndex;
    public int mRowIndex;
    public String mCountry;

    @Override
    public String toString() {
        return "sheetIndex = [" + mSheetIndex + "], rowIndex = [" + mRowIndex + "], country = [" + mCountry + "], groupLink = [" + mGroupLink + "]\n";
    }
}
