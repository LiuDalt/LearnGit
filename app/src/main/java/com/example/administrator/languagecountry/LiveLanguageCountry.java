package com.example.administrator.languagecountry;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LiveLanguageCountry {
    private List<LiveLanguage> mLanguages;
    private List<LiveCountry> mCountries;

    private LiveLanguageCountry(){}

    public List<LiveCountry> getCountries() {
        return mCountries;
    }

    public List<LiveLanguage> getLanguages() {
        return mLanguages;
    }

    public List<LiveCountry> findCountries(String languageCode){
        if(TextUtils.isEmpty(languageCode)){
            return null;
        }
        for(LiveLanguage liveLanguage : mLanguages){
            if(liveLanguage.languageCode.equals(languageCode)){
                return liveLanguage.mCountries;
            }
        }
        return null;
    }

    public List<LiveCountry> findCountries(LiveLanguage language){
        if(mLanguages == null || mLanguages.isEmpty() || language == null){
            return null;
        }
        return findCountries(language.languageCode);
    }

    public static LiveLanguageCountry paseFromJson(String jsonStr){
        try {
            LiveLanguageCountry languageCountry = new LiveLanguageCountry();
            JSONObject json = new JSONObject(jsonStr);
            JSONObject mapArray = json.optJSONObject("map");
            JSONArray languagesArray = json.optJSONArray("languages");
            languageCountry.mLanguages = getLanguagesFromJson(languagesArray, getLanguageCountryMap(mapArray));
            JSONArray countriesArray = json.optJSONArray("countries");
            languageCountry.mCountries = getCountriesFromJson(countriesArray);
            return languageCountry;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<LiveCountry> getCountriesFromJson(JSONArray countriesArray) {
        List<LiveCountry> countries = new ArrayList<>();
        for(int i = 0; i < countriesArray.length(); i++){
            LiveCountry country = new LiveCountry();
            country.countryCode = countriesArray.optString(i);
//            country.name = ResourceUtils.getStringByName(country.countryCode);
            if(TextUtils.isEmpty(country.name)){
                continue;
            }
            countries.add(country);
        }
        return countries;
    }

    private static Map<String, List<LiveCountry>> getLanguageCountryMap(JSONObject mapJson) {
        Map<String, List<LiveCountry>> languageCountryMap = new HashMap<>();
        Iterator<String> iterator = mapJson.keys();
        while (iterator.hasNext()) {
            List<LiveCountry> mapCountries = new ArrayList<>();
            String key = iterator.next();
            JSONArray mapCountryArray = mapJson.optJSONArray(key);
            for (int j = 0; j < mapCountryArray.length(); j++) {
                LiveCountry country = new LiveCountry();
                country.countryCode = mapCountryArray.optString(j);
//                country.name = ResourceUtils.getStringByName(country.countryCode);
                if (TextUtils.isEmpty(country.name)) {
                    continue;
                }
                mapCountries.add(country);
            }
            languageCountryMap.put(key, mapCountries);
        }
        return languageCountryMap;
    }

    private static List<LiveLanguage> getLanguagesFromJson(JSONArray languagesArray, Map<String, List<LiveCountry>> languageCountryMap) {
        List<LiveLanguage> languages = new ArrayList<>();

        Map<String, Integer> languageNameResMap = LiveLanguageCountryConstant.getLanguageNameResMap();
        for(int i = 0; i < languagesArray.length(); i++){
            LiveLanguage liveLanguage = new LiveLanguage();
            liveLanguage.languageCode = languagesArray.optString(i);
            if(!languageNameResMap.containsKey(liveLanguage.languageCode)){
                continue;
            }
//            liveLanguage.languageName = ResourceUtils.getString(languageNameResMap.get(liveLanguage.languageCode));
            if(!languageCountryMap.containsKey(liveLanguage.languageCode)){
                continue;
            }
            liveLanguage.mCountries = languageCountryMap.get(liveLanguage.languageCode);
            languages.add(liveLanguage);
        }
        return languages;
    }
}
