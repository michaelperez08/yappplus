package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 12/10/17.
 */

public class Influencers {

    private String nombre, correo, cantidad;

    public Influencers(String nombre, String correo, String cantidad) {
        this.nombre = nombre;
        this.correo = correo;
        this.cantidad = cantidad;
    }

    public Influencers() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
