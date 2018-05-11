package com.example.administrator.aac;

public class Data {
    private int mValue = 0;
    private String mUnit1 = "  元";
    private String mUnit2 = " 美金";

    public Data(String unit1, String unit2) {
        mUnit1 = unit1;
        mUnit2 = unit2;
    }

    public Data(int value) {
        mValue = value;
    }

    public Data(){}

    public void setUnit1(String unit1) {
        mUnit1 = unit1;
    }

    public void setUnit2(String unit2) {
        mUnit2 = unit2;
    }

    public void setValue(int value) {
        mValue = value;
    }

    public int getNum() {
        return mValue;
    }

    public String getUnit1() {
        return mUnit1;
    }

    public String getUnit2() {
        return mUnit2;
    }
}
