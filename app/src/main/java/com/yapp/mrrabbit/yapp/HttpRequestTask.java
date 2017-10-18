package com.yapp.mrrabbit.yapp;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
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

class HttpPostRequestTask extends AsyncTask<String, Void, String> {

    private String result;
    private String url;
    private MultiValueMap<String, String> map;

    @Override
    protected String doInBackground(String... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        try {
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            String greeting = (String) restTemplate.postForObject(url, request ,String.class);
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

    public MultiValueMap<String, String> getMap() {
        return map;
    }

    public void setMap(MultiValueMap<String, String> map) {
        this.map = map;
    }
}
