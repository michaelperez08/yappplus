package com.yapp.mrrabbit.yapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

/**
 * Created by michael on 21/09/17.
 */

public class DataAccess {

    public static final String BASE_URL="http://yappdevelopers.com/";
    public static final String YAPPEXPERIENCE_API_BASE_URL= BASE_URL+"webserviceplus/";
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
                        jsb.getDouble("weekMoney"), jsb.getDouble("monthMoney"), jsb.getDouble("totalMoney"), jsb.getBoolean("is_free"), "$");
                if(jsb.getInt("currency")==1){
                    evento.setMoneda("$");
                }else{
                    evento.setMoneda("₡");
                }
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return evento;
    }

    public int[] getLikesClicksImpresionesEvento(int idEvento){
        int[] likesClicksImpresiones = new int[3];
        String result = "";
        JSONObject jsb;

        String url = YAPPEXPERIENCE_API_BASE_URL+"get_counters?experience_id="+idEvento;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                jsb = JsonParser.getJsonResponse(result);

                /*evento = new Evento(idEvento, "Nombre", "foto", jsb.getInt("todaySales"), jsb.getInt("weekSales"), jsb.getInt("monthSales"), jsb.getInt("totalSales"), jsb.getDouble("todayMoney"),
                        jsb.getDouble("weekMoney"), jsb.getDouble("monthMoney"), jsb.getDouble("totalMoney"), jsb.getBoolean("is_free"));*/

                likesClicksImpresiones[0]=(jsb.getInt("printed"));
                likesClicksImpresiones[1]=(jsb.getInt("clicked"));
                likesClicksImpresiones[2]=(jsb.getInt("liked"));
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return likesClicksImpresiones;
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

    public static String formatearFechaDialogoEscaner(String fechaTiempo) throws ParseException {

        fechaTiempo = fechaTiempo.replace('T',' ');
        String[] vector_fechaTiempo = fechaTiempo.split("\\s+");

        String tiempoFormateado = "";
        String fechaFormateada = "";
        Date tiempo;
        Date fecha;

        DateFormat sdf_tiempo = new SimpleDateFormat("hh:mm:ss");
        DateFormat sdf_fecha = new SimpleDateFormat("yyyy-MM-dd");

        DateFormat sdf_tiempo_salida = new SimpleDateFormat("hh:mm a");
        DateFormat sdf_fecha_salida = new SimpleDateFormat("dd-MM-yyyy");
        try {
            tiempo = sdf_tiempo.parse(vector_fechaTiempo [1]);
            tiempoFormateado = sdf_tiempo_salida.format(tiempo);

            fecha = sdf_fecha.parse(vector_fechaTiempo [0]);
            fechaFormateada += sdf_fecha_salida.format(fecha);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaFormateada+" "+tiempoFormateado;
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
                            jsoTemp.getInt("yapp_commission_percentage"),jsoTemp.getInt("bank_commission_percentage"),jsoTemp.getDouble("organizer_money"), jsoTemp.getString("sold_out_msj"), jsoTemp.getInt("courtesy_scanned_tickets")));
                }

            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tipoTiquetes;
    }

    public ArrayList<Influencer> getInfluencersEvento(int idEvento){
        ArrayList<Influencer> listaInfluencers = null;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        String url = YAPPEXPERIENCE_API_BASE_URL + "referral_report_json?experience_id="+idEvento;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                listaInfluencers = new ArrayList<>();
                jsa = JsonParser.getJsonArrayFromObject("value", JsonParser.getJsonResponse(result));
                for(int i=0; i < jsa.length(); i++){
                    jsoTemp = jsa.getJSONObject(i);
                    listaInfluencers.add(new Influencer(jsoTemp.getString("Nombre_Completo"), jsoTemp.getString("Email_Primario"),jsoTemp.getInt("idUsuario"), jsoTemp.getInt("cantidadReferidos")));
                }

            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return listaInfluencers;
    }

    public ArrayList<Tiquete> getTiquetesEvento(int idEvento) {
        ArrayList<Tiquete> tiquetes = null;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        String cedula = "";
        String url = YAPPEXPERIENCE_API_BASE_URL + "get_events_tickets_yappplus?id_usuario="+MainActivity.id_usuario+"&id_evento="+idEvento;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if (result != null) {
                tiquetes = new ArrayList<>();
                jsoTemp = JsonParser.getJsonResponse(result);
                if(jsoTemp!=null) {
                    jsa = JsonParser.getJsonArrayFromObject("entradas", jsoTemp);
                    for (int i = 0; i < jsa.length(); i++) {
                        jsoTemp = jsa.getJSONObject(i);
                        cedula = getCedulaDeDescripcion(jsoTemp.getString("Descripcion"));
                        tiquetes.add(new Tiquete(jsoTemp.getInt("Id_Entrada"), jsoTemp.getInt("Id_Compra"), jsoTemp.getString("Nombre_Modulo"), jsoTemp.getString("Codigo_QR"),
                                jsoTemp.getBoolean("Canjeada"), jsoTemp.getBoolean("Canjeada"), jsoTemp.getString("Fecha_Escaneo"), jsoTemp.getString("Nombre_Completo_Compra"), cedula));
                    }
                }
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tiquetes;
    }

    public String getCedulaDeDescripcion(String descripcion){
        if(!descripcion.isEmpty()){
            int index = descripcion.lastIndexOf("Número de identificación:");
            if(index>0) {
                descripcion = descripcion.substring(index + 25).replace('t', ' ').trim();
            }else{
                descripcion = "";
            }
        }
        return descripcion;
    }


    public boolean envarExcel(int idEvento, String servicio){
        boolean enviado=false;
        String result = "";
        JSONObject jsoTemp;
        String url = "https://yappexperience.com/webserviceplus/" + servicio +"?experience_id="+idEvento;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                jsoTemp = JsonParser.getJsonResponse(result);
                int in = jsoTemp.getInt("result");
                enviado = jsoTemp.getInt("result") == 1;
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return enviado;
    }

    public boolean setTiquetesDisponibles(int idEvento, ArrayList<TipoTiquete> lista_temp_tipoTique){
        boolean succces=true;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        String url = "https://yappexperience.com/webserviceplus/modify_tickets_count?experience_id="+idEvento+"&modules_info="+generarParametrosURLPausarVenta(lista_temp_tipoTique);
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                jsa = JsonParser.getJsonArrayResponse(result);
                TipoTiquete temp_tt;
                for(int i=0; i < jsa.length(); i++) {
                    jsoTemp = jsa.getJSONObject(i);
                    temp_tt=lista_temp_tipoTique.get(i);
                    if(!jsoTemp.getBoolean(String.valueOf(temp_tt.getIdTiquete()))){
                        succces=false;
                    }
                }
            }
        } catch (JSONException | InterruptedException | ExecutionException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return succces;
    }

    public String generarParametrosURLPausarVenta(ArrayList<TipoTiquete> lista_temp_tipoTique){
        String parametrosURLPausarVenta = "";
        for (int i=0; i<lista_temp_tipoTique.size(); i++) {
            TipoTiquete temp_tipoTiquete = lista_temp_tipoTique.get(i);
            parametrosURLPausarVenta += temp_tipoTiquete.getIdTiquete()+":"+temp_tipoTiquete.getTiquetesDisponibles()+":"+temp_tipoTiquete.getMensaje()+"|";
        }
        parametrosURLPausarVenta = parametrosURLPausarVenta.substring(0,parametrosURLPausarVenta.length()-1);
        return  parametrosURLPausarVenta;
    }

    /*public boolean subirTiuetesEscaneadosAlServidor(String codigo, String fecha){
        boolean enviado=false;
        ArrayList<Tiquete> tiquetes = null;
        String result = "";
        JSONArray jsa;
        JSONObject jsoTemp;
        //fecha = fecha.replace(' ','T');
        String url = "https://yappexperience.com/boleteria_offline/escanear_entrada_offline_yappplus";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("id_evento", codigo);
        map.add("fecha_escaneo", fecha);
        HttpPostRequestTask hpt = new HttpPostRequestTask();
        hpt.setMap(map);
        try {
            result = (String) hpt.execute(url).get();
            if(result!=null) {
                jsoTemp = JsonParser.getJsonResponse(result);
                enviado = jsoTemp.getInt("resultado") == 1;
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return enviado;
    }*/

    public boolean subirTiuetesEscaneadosAlServidor(String codigo, String fecha){
        boolean enviado=false;
        ArrayList<Tiquete> tiquetes = null;
        String result = "";
        JSONObject jsoTemp;
        String url = "http://yappdevelopers.com/boleteria_offline/escanear_entrada_offline_yappplus?id_entrada="+codigo+"&fecha_escaneo="+fecha;
        try {
            result = (String) new HttpRequestTask().execute(url).get();
            if(result!=null) {
                jsoTemp = JsonParser.getJsonResponse(result);
                enviado = jsoTemp.getInt("resultado") == 1;
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return enviado;
    }

    public static double roundDouble(double a){
        return Math.round(a * 100.0) / 100.0;
    }

}
