package com.yapp.mrrabbit.yapp;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by michael on 21/09/17.
 */

public class Evento {

    private int idEvento;
    private String nombre;
    private String fotoPerfilURL, moneda;
    private int todaySales, weekSales, monthSales, totalSales;
    private double todayMoney, weekMoney, monthMoney, totalMoney;
    private ArrayList<TipoTiquete> tipoTiquetes;
    private ArrayList<Influencer> influencers;
    private ArrayList<Tiquete> tiquetesEvento;
    private boolean gratis;
    private int likes, clicks, impresiones;

    public Evento(int idEvento, String nombre, String fotoPerfilURL, int todaySales, int weekSales, int monthSales, int totalSales, double todayMoney, double weekMoney, double monthMoney, double totalMoney,
                  boolean gratis, String moneda) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.fotoPerfilURL = fotoPerfilURL;
        this.todaySales = todaySales;
        this.weekSales = weekSales;
        this.monthSales = monthSales;
        this.totalSales = totalSales;
        this.todayMoney = todayMoney;
        this.weekMoney = weekMoney;
        this.monthMoney = monthMoney;
        this.totalMoney = totalMoney;
        this.gratis = gratis;
        this.moneda = moneda;
    }

    public Evento(String nombre, String fotoPerfilURL, int todaySales, int weekSales, int monthSales, int totalSales, double todayMoney, double weekMoney, double monthMoney, double totalMoney,
                  boolean gratis, String moneda) {
        this.nombre = nombre;
        this.fotoPerfilURL = fotoPerfilURL;
        this.todaySales = todaySales;
        this.weekSales = weekSales;
        this.monthSales = monthSales;
        this.totalSales = totalSales;
        this.todayMoney = todayMoney;
        this.weekMoney = weekMoney;
        this.monthMoney = monthMoney;
        this.totalMoney = totalMoney;
        this.gratis = gratis;
        this.moneda = moneda;
    }

    public Evento() {

    }

    public int getTotalTiquetesEscaneados(){
        int total = 0;
        for (TipoTiquete temp_ticket: tipoTiquetes) {
            total += temp_ticket.getTiquetesEscaneados();
        }
        return  total;
    }

    public int getTotalTiquetesVendidos(){
        int total = 0;
        for (TipoTiquete temp_tipo_ticket: tipoTiquetes) {
            total += temp_tipo_ticket.getTiquetesVendidos();
        }
        return  total;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfilURL() {
        return fotoPerfilURL;
    }

    public void setFotoPerfilURL(String fotoPerfilURL) {
        this.fotoPerfilURL = fotoPerfilURL;
    }

    public int gettodaySales() {
        return todaySales;
    }

    public void settodaySales(int todaySales) {
        this.todaySales = todaySales;
    }

    public int getweekSales() {
        return weekSales;
    }

    public void setweekSales(int weekSales) {
        this.weekSales = weekSales;
    }

    public int getmonthSales() {
        return monthSales;
    }

    public void setmonthSales(int monthSales) {
        this.monthSales = monthSales;
    }

    public int gettotalSales() {
        return totalSales;
    }

    public void settotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public double gettodayMoney() {
        return todayMoney;
    }

    public void settodayMoney(double todayMoney) {
        this.todayMoney = todayMoney;
    }

    public double getweekMoney() {
        return weekMoney;
    }

    public void setweekMoney(double weekMoney) {
        this.weekMoney = weekMoney;
    }

    public double getmonthMoney() {
        return monthMoney;
    }

    public void setmonthMoney(double monthMoney) {
        this.monthMoney = monthMoney;
    }

    public double gettotalMoney() {
        return totalMoney;
    }

    public void settotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public ArrayList<TipoTiquete> getTipoTiquetes() {
        return tipoTiquetes;
    }

    public void setTipoTiquetes(ArrayList<TipoTiquete> tipoTiquetes) {
        this.tipoTiquetes = tipoTiquetes;
    }

    public ArrayList<Influencer> getInfluencers() {
        return influencers;
    }

    public void setInfluencers(ArrayList<Influencer> influencers) {
        this.influencers = influencers;
    }

    public ArrayList<Tiquete> getTiquetesEvento() {
        return tiquetesEvento;
    }

    public void setTiquetesEvento(ArrayList<Tiquete> tiquetesEvento) {
        this.tiquetesEvento = tiquetesEvento;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getImpresiones() {
        return impresiones;
    }

    public void setImpresiones(int impresiones) {
        this.impresiones = impresiones;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public boolean isGratis() {
        return gratis;
    }

    public void setGratis(boolean gratis) {
        this.gratis = gratis;
    }
}
