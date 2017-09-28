package com.yapp.mrrabbit.yapp;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by michael on 21/09/17.
 */

public class HttpRequestTask extends AsyncTask<String, Void, String> {

    private String result;
    private String url;

    @Override
    protected String doInBackground(String... params) {
        try {
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String greeting = restTemplate.getForObject(url, String.class);
            return greeting;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String greeting) {
        result = greeting;

    }

    public String getResult() {
        return result;
    }
}
