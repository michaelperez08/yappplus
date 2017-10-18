package com.yapp.mrrabbit.yapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String result = "";
        JSONObject jsb;

        String url = YAPPEXPERIENCE_API_BASE_URL+"get_experience_general_sales_info?experience_id="+idEvento+"&date="+getCurrentDate("yyyy-MM-dd");
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                jsb = JsonParser.getJsonResponse(result);

                evento = new Evento(idEvento, "Nombre", "foto", jsb.getInt("todaySales"), jsb.getInt("weekSales"), jsb.getInt("monthSales"), jsb.getInt("totalSales"), jsb.getDouble("todayMoney"),
                        jsb.getDouble("weekMoney"), jsb.getDouble("monthMoney"), jsb.getDouble("totalMoney"));
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return evento;
    }

    public static String getCurrentDate(String formato){
        DateFormat dateFormat = new SimpleDateFormat(formato);
        Date date = new Date();
        return  dateFormat.format(date);
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        Date date = new Date();
        return  dateFormat.format(date);
    }

    public ArrayList<TipoTiquete> getTipoTiquetesEvento(int idEvento){
        ArrayList<TipoTiquete> tipoTiquetes = null;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        String url = YAPPEXPERIENCE_API_BASE_URL + "get_experience_modules_sales_info?experience_id="+idEvento;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                tipoTiquetes = new ArrayList<>();
                jsa = JsonParser.getJsonArrayResponse(result);
                for(int i=0; i < jsa.length(); i++){
                    jsoTemp = jsa.getJSONObject(i);
                    tipoTiquetes.add(new TipoTiquete(jsoTemp.getString("name"), jsoTemp.getInt("module_id"), jsoTemp.getInt("total_tickets"), jsoTemp.getInt("sold_tickets"), jsoTemp.getInt("available_tickets"),
                            jsoTemp.getInt("scanned_tickets"), jsoTemp.getInt("courtesy_tickets"), jsoTemp.getDouble("subtotal_money"), jsoTemp.getDouble("yapp_commission_money"), jsoTemp.getDouble("bank_commission_money"),
                            jsoTemp.getInt("yapp_commission_percentage"),jsoTemp.getInt("bank_commission_percentage"),jsoTemp.getDouble("organizer_money")));
                }

            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tipoTiquetes;
    }

    public ArrayList<Tiquete> getTiquetesEvento(int idEvento){
        ArrayList<Tiquete> tiquetes = null;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        String url = "https://yappexperience.com/reporte/filtrar_transacciones_evento_yappplus";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id_evento", String.valueOf(idEvento));
        HttpPostRequestTask hpt = new HttpPostRequestTask();
        hpt.setMap(map);
        try {
            result = (String) hpt.execute(url).get();
            if(result!=null) {
                tiquetes = new ArrayList<>();
                jsa = JsonParser.getJsonArrayFromObject("value", JsonParser.getJsonResponse(result));
                for(int i=0; i < jsa.length(); i++) {
                    jsoTemp = jsa.getJSONObject(i);
                    tiquetes.add(new Tiquete(jsoTemp.getInt("Id_Entrada"), jsoTemp.getInt("Id_Compra"), jsoTemp.getString("nombre_entrada"), jsoTemp.getString("Codigo_QR"), jsoTemp.getBoolean("Canjeada")));
                }//int idTiquete, int idCompra, String tipoTiquete, String codigoQR, boolean canjeada
            }
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return  tiquetes;
    }

}
