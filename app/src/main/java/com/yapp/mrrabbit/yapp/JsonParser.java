package com.yapp.mrrabbit.yapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by michael on 21/09/17.
 */

public class JsonParser {

    public static final JSONObject getJsonResponse(String response){

        try {
            JSONObject obj = new JSONObject(response);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static final JSONArray getJsonArrayResponse(String response){

        try {
            JSONArray obj = new JSONArray(response);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static final JSONArray getJsonArrayFromObject(String key, JSONObject jsonObject){

        try {
            JSONArray obj = jsonObject.getJSONArray(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static final JSONObject getJsonObjectFromObject(String key, JSONObject jsonObject){

        try {
            JSONObject obj = jsonObject.getJSONObject(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static final JSONObject getJsonObjectFromArray(int pos, JSONArray jsonArray){

        try {
            JSONObject obj = jsonArray.getJSONObject(pos);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return null;
        }

    }

    public static final boolean getBoolean(String key,JSONObject jsonObject){

        try {
            boolean obj = jsonObject.getBoolean(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return false;
        }

    }

    public static final String getString(String key, JSONObject jsonObject){
        try {
            String obj = jsonObject.getString(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return "";
        }
    }

    public static final int getInt(String key, JSONObject jsonObject){
        try {
            int obj = jsonObject.getInt(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return -1;
        }
    }

    public static final Date getDate(String key, JSONObject jsonObject){
        try {
            String stringDate = jsonObject.getString(key);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            Date obj = format.parse(stringDate);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return new Date();
        } catch (ParseException e){
            return new Date();
        }
    }

    public static final double getDouble(String key, JSONObject jsonObject){
        try {
            double obj = jsonObject.getDouble(key);
            return obj;
        } catch (JSONException e) {
            //e.printStackTrace();
            return 0;
        }
    }
}
