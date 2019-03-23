package com.example.administrator.languagecountry;

public class LiveLanguageCountryHelper {
    String TAG = "LiveLanguageCountryHelper";
    private LiveLanguageCountry mLanguageCountry;
    private LiveLanguageCountryPullListener mPullListener;
    private static boolean sTest = true;
    public static String sTestJson= "{\"countries\": [\"US\",\"RU\",\"CN\",\"IN\",\"ID\",\"PK\",\"others\"],\"languages\": [\"en\",\"ru\",\"zh\",\"hi\",\"id\",\"ur\",\"others\"],\"map\": {\"zh\": [\"CN\"],\"en\": [\"US\"],\"hi\": [\"IN\"],\"id\": [\"ID\"],\"others\": [\"others\"],\"ru\": [\"RU\"],\"ur\": [\"PK\"]}}";
    public void setPullListener(LiveLanguageCountryPullListener pullListener) {
        mPullListener = pullListener;
    }

    public void doPull(){
        if(mLanguageCountry != null) {
            if (mPullListener != null) {
                mPullListener.onSuccess(mLanguageCountry);
            }
        }
        if(sTest){
            mLanguageCountry = LiveLanguageCountry.paseFromJson(sTestJson);
            if (mPullListener != null) {
                mPullListener.onSuccess(mLanguageCountry);
            }
        }
    }

    public interface LiveLanguageCountryPullListener{
        void onSuccess(LiveLanguageCountry languageCountry);
        void onFail();
    }
}
