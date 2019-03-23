package com.example.administrator.languagecountry;

import com.example.administrator.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class LiveLanguageCountryConstant {
    public static final String CODE_EN = "en";//英语
    public static final String CODE_RU = "ru";//俄语
    public static final String CODE_HI = "hi";//印地语
    public static final String CODE_ID = "id";//印尼语
    public static final String CODE_UR = "ur";//乌尔都语
    public static final String CODE_ZH = "zh";//中文
    public static final String CODE_PT = "pt";//葡萄牙语
    public static final String CODE_AR = "ar";//阿拉伯语
    public static final String CODE_ES = "es";//西班牙语
    public static final String CODE_VI = "vi";//越南语
    public static final String CODE_TH = "th";//泰语
    public static final String CODE_OTHERS = "others";//其他

    public static Map<String, Integer> getLanguageNameResMap(){
        Map<String, Integer> map = new HashMap<>();
        map.put(CODE_EN, R.string.app_name);
        map.put(CODE_RU, R.string.app_name);
        map.put(CODE_HI, R.string.app_name);
        map.put(CODE_ID, R.string.app_name);
        map.put(CODE_UR, R.string.app_name);
        map.put(CODE_ZH, R.string.app_name);
        map.put(CODE_PT, R.string.app_name);
        map.put(CODE_AR, R.string.app_name);
        map.put(CODE_ES, R.string.app_name);
        map.put(CODE_VI, R.string.app_name);
        map.put(CODE_TH, R.string.app_name);
        map.put(CODE_OTHERS, R.string.app_name);

        return map;
    }

}
