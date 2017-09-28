package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 21/09/17.
 */

public class Evento {

    private int idEvento;
    private String nombre;
    private String fotoPerfilURL;
    private int todaySales, weekSales, monthSales, totalSales;
    private double todayMoney, weekMoney, monthMoney, totalMoney;

    public Evento(int idEvento, String nombre, String fotoPerfilURL, int todaySales, int weekSales, int monthSales, int totalSales, double todayMoney, double weekMoney, double monthMoney, double totalMoney) {
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
    }

    public Evento(String nombre, String fotoPerfilURL, int todaySales, int weekSales, int monthSales, int totalSales, double todayMoney, double weekMoney, double monthMoney, double totalMoney) {
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
}
