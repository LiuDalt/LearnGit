package com.example.administrator.mannotation;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

public class TestAnnotation1 {
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;

    public static final String STR1 = "s1";
    public static final String STR2 = "s2";
    public static final String STR3 = "s3";

    @IntDef({ONE, TWO, THREE})
    @interface IntDataType{
    }

    @StringDef({STR1, STR2, STR3})
    @interface StrDataType{
    }
    static class Data {
        private int mDataI;

        public String mDataS;

        public void setDataI(@IntDataType int dataI) {
            mDataI = dataI;
        }

        public void setDataS(@StrDataType String dataS) {
            mDataS = dataS;
        }
    }

    static void main(String[] args){
        Data data = new Data();
        data.setDataI(TestAnnotation1.ONE);//编译正确
        data.setDataS(TestAnnotation1.STR3);//编译正确

        data.setDataI(2);//编译报错
        data.setDataS("s1");//编译报错
    }

}
