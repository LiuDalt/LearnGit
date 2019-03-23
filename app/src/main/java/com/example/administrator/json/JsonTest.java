package com.example.administrator.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTest {
    public static void test(){
        String str = "{\"maps\": [\"aaa\",\"bbb\"]}";
        try {
            JSONObject json = new JSONObject(str);
            JSONArray mapArray = json.optJSONArray("maps");
            String aaa = mapArray.optString(0);
            String bbb = mapArray.optString(1);
            Log.i("JsonTest", mapArray + " " + aaa + " " + bbb);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
