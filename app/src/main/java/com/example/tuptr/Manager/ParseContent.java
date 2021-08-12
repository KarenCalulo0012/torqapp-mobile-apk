package com.example.tuptr.Manager;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseContent {

    private final String KEY_SUCCESS = "status";
    private final String KEY_MSG = "message";
    private final String KEY_AddressList = "addressList";
    private final String KEY_DATA = "Data";
    private  ArrayList<HashMap<String, String>> hashMap;
    private Activity activity;
    PreferenceHelper preferenceHelper;

    ArrayList<HashMap<String, String>> arraylist;

    public ParseContent(Activity activity) {
        this.activity = activity;
        preferenceHelper = new PreferenceHelper(activity);

    }

    public boolean isSuccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString(KEY_SUCCESS).equals("true")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getErrorMessage(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString(KEY_MSG);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

}
