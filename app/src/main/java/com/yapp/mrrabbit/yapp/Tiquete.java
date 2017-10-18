package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 12/10/17.
 */

public class Tiquete {

    private int idTiquete, idCompra;
    private String tipoTiquete, codigoQR;
    private boolean canjeada;

    public Tiquete(int idTiquete, int idCompra, String tipoTiquete, String codigoQR, boolean canjeada) {
        this.idTiquete = idTiquete;
        this.idCompra = idCompra;
        this.tipoTiquete = tipoTiquete;
        this.codigoQR = codigoQR;
        this.canjeada = canjeada;
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

    public void setCanjeada(boolean canjeada) {
        this.canjeada = canjeada;
    }
}
