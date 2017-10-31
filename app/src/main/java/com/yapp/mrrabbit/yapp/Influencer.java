package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 26/10/17.
 */

public class Influencer {

    private String nombre, correo;
    private int idUsuario, cantidad;

    public Influencer(String nombre, String correo, int idUsuario, int cantidad) {
        this.nombre = nombre;
        this.correo = correo;
        this.idUsuario = idUsuario;
        this.cantidad = cantidad;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
