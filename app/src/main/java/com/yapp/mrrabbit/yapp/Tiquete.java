package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 12/10/17.
 */

public class Tiquete {

    private int idTiquete, idCompra;
    private String tipoTiquete, codigoQR, fechaEscaneo, nombreComprador;
    private boolean canjeada, sincronizada;

    public Tiquete(int idTiquete, int idCompra, String tipoTiquete, String codigoQR, boolean canjeada, boolean sincronizada, String fechaEscaneo, String nombreComprador) {
        this.idTiquete = idTiquete;
        this.idCompra = idCompra;
        this.tipoTiquete = tipoTiquete;
        this.codigoQR = codigoQR;
        this.canjeada = canjeada;
        this.sincronizada = sincronizada;
        this.fechaEscaneo = fechaEscaneo;
        this.nombreComprador = nombreComprador;
    }

    public Tiquete() {
    }

    public int getIdTiquete() {
        return idTiquete;
    }

    public void setIdTiquete(int idTiquete) {
        this.idTiquete = idTiquete;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }

    public String getTipoTiquete() {
        return tipoTiquete;
    }

    public void setTipoTiquete(String tipoTiquete) {
        this.tipoTiquete = tipoTiquete;
    }

    public String getCodigoQR() {
        return codigoQR;
    }

    public void setCodigoQR(String codigoQR) {
        this.codigoQR = codigoQR;
    }

    public boolean isCanjeada() {
        return canjeada;
    }

    public boolean isSincronizada() {
        return sincronizada;
    }

    public void setSincronizada(boolean sincronizada) {
        this.sincronizada = sincronizada;
    }

    public void setCanjeada(boolean canjeada) {
        this.canjeada = canjeada;
    }

    public String getFechaEscaneo() {
        return fechaEscaneo;
    }

    public void setFechaEscaneo(String fechaEscaneo) {
        this.fechaEscaneo = fechaEscaneo;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }
}
