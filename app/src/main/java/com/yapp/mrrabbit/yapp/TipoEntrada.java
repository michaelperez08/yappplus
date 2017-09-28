package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 21/09/17.
 */

public class TipoEntrada {

    private int idEntrada;
    private String nombreEntrada;
    private int cantidadVenidad;
    private double dineroGenerado;
    private boolean cortesia;

    public TipoEntrada(int idEntrada, String nombreEntrada, int cantidadVenidad, double dineroGenerado, boolean cortesia) {
        this.idEntrada = idEntrada;
        this.nombreEntrada = nombreEntrada;
        this.cantidadVenidad = cantidadVenidad;
        this.dineroGenerado = dineroGenerado;
        this.cortesia = cortesia;
    }

    public TipoEntrada(String nombreEntrada, int cantidadVenidad, double dineroGenerado, boolean cortesia) {
        this.nombreEntrada = nombreEntrada;
        this.cantidadVenidad = cantidadVenidad;
        this.dineroGenerado = dineroGenerado;
        this.cortesia = cortesia;
    }

    public int getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(int idEntrada) {
        this.idEntrada = idEntrada;
    }

    public String getNombreEntrada() {
        return nombreEntrada;
    }

    public void setNombreEntrada(String nombreEntrada) {
        this.nombreEntrada = nombreEntrada;
    }

    public int getCantidadVenidad() {
        return cantidadVenidad;
    }

    public void setCantidadVenidad(int cantidadVenidad) {
        this.cantidadVenidad = cantidadVenidad;
    }

    public double getDineroGenerado() {
        return dineroGenerado;
    }

    public void setDineroGenerado(double dineroGenerado) {
        this.dineroGenerado = dineroGenerado;
    }

    public boolean isCortesia() {
        return cortesia;
    }

    public void setCortesia(boolean cortesia) {
        this.cortesia = cortesia;
    }
}
