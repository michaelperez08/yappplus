package com.yapp.mrrabbit.yapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by michael on 21/09/17.
 */

public class DataAccess {

    public static final String YAPPEXPERIENCE_API_BASE_URL="http://yappdevelopers.com/webserviceplus/";
    private static HttpRequestTask request;
    private static Context context = MainActivity.appContext;

    public Evento getInfoEventoSales(int idEvento){
        Evento evento = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String result = "";
        JSONObject jsb;

        System.out.println(dateFormat.format(date));
        String url = YAPPEXPERIENCE_API_BASE_URL+"get_experience_general_sales_info?experience_id="+idEvento+"&date="+dateFormat.format(date);
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            jsb = JsonParser.getJsonResponse(result);

            evento = new Evento(idEvento, "Nombre", "foto", jsb.getInt("todaySales"), jsb.getInt("weekSales"), jsb.getInt("monthSales"), jsb.getInt("totalSales"), jsb.getDouble("todayMoney"),
                    jsb.getDouble("weekMoney"), jsb.getDouble("monthMoney"), jsb.getDouble("totalMoney"));
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return evento;
    }



}
