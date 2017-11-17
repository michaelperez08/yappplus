package com.yapp.mrrabbit.yapp;

/**
 * Created by michael on 28/09/17.
 */

public class TipoTiquete {

    private String nombre, mensaje;
    private int idTiquete;
    private int tiquetesTotales, tiquetesVendidos, tiquetesDisponibles, tiquetesEscaneados, tiquetesCortesia;
    private double dineroSubtotal, dineroComisionYapp, dineroComisionBanco;
    private int porcentageComisionYapp, porcentageComisionBanco;
    private double dineroOrganizador;

    public TipoTiquete(String nombre, int idTiquete, int tiquetesTotales, int tiquetesVendidos, int tiquetesDisponibles, int tiquetesEscaneados, int tiquetesCortesia, double dineroSubtotal, double dineroComisionYapp,
                       double dineroComisionBanco, int porcentageComisionYapp, int porcentageComisionBanco, double dineroOrganizador, String mensaje) {
        this.nombre = nombre;
        this.idTiquete = idTiquete;
        this.tiquetesTotales = tiquetesTotales;
        this.tiquetesVendidos = tiquetesVendidos;
        this.tiquetesDisponibles = tiquetesDisponibles;
        this.tiquetesEscaneados = tiquetesEscaneados;
        this.tiquetesCortesia = tiquetesCortesia;
        this.dineroSubtotal = dineroSubtotal;
        this.dineroComisionYapp = dineroComisionYapp;
        this.dineroComisionBanco = dineroComisionBanco;
        this.porcentageComisionYapp = porcentageComisionYapp;
        this.porcentageComisionBanco = porcentageComisionBanco;
        this.dineroOrganizador = dineroOrganizador;
        this.mensaje = mensaje;
    }

    public TipoTiquete(String nombre, int tiquetesTotales, int tiquetesVendidos, int tiquetesDisponibles, int tiquetesEscaneados, int tiquetesCortesia, double dineroSubtotal, double dineroComisionYapp,
                       double dineroComisionBanco, int porcentageComisionYapp, int porcentageComisionBanco, double dineroOrganizador, String mensaje) {
        this.nombre = nombre;
        this.tiquetesTotales = tiquetesTotales;
        this.tiquetesVendidos = tiquetesVendidos;
        this.tiquetesDisponibles = tiquetesDisponibles;
        this.tiquetesEscaneados = tiquetesEscaneados;
        this.tiquetesCortesia = tiquetesCortesia;
        this.dineroSubtotal = dineroSubtotal;
        this.dineroComisionYapp = dineroComisionYapp;
        this.dineroComisionBanco = dineroComisionBanco;
        this.porcentageComisionYapp = porcentageComisionYapp;
        this.porcentageComisionBanco = porcentageComisionBanco;
        this.dineroOrganizador = dineroOrganizador;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiquetesTotales() {
        return tiquetesTotales;
    }

    public void setTiquetesTotales(int tiquetesTotales) {
        this.tiquetesTotales = tiquetesTotales;
    }

    public int getTiquetesVendidos() {
        return tiquetesVendidos;
    }

    public void setTiquetesVendidos(int tiquetesVendidos) {
        this.tiquetesVendidos = tiquetesVendidos;
    }

    public int getTiquetesDisponibles() {
        return tiquetesDisponibles;
    }

    public void setTiquetesDisponibles(int tiquetesDisponibles) {
        this.tiquetesDisponibles = tiquetesDisponibles;
    }

    public int getTiquetesEscaneados() {
        return tiquetesEscaneados;
    }

    public void setTiquetesEscaneados(int tiquetesEscaneados) {
        this.tiquetesEscaneados = tiquetesEscaneados;
    }

    public int getTiquetesCortesia() {
        return tiquetesCortesia;
    }

    public void setTiquetesCortesia(int tiquetesCortesia) {
        this.tiquetesCortesia = tiquetesCortesia;
    }

    public double getDineroSubtotal() {
        return dineroSubtotal;
    }

    public void setDineroSubtotal(double dineroSubtotal) {
        this.dineroSubtotal = dineroSubtotal;
    }

    public double getDineroComisionYapp() {
        return dineroComisionYapp;
    }

    public void setDineroComisionYapp(double dineroComisionYapp) {
        this.dineroComisionYapp = dineroComisionYapp;
    }

    public double getDineroComisionBanco() {
        return dineroComisionBanco;
    }

    public void setDineroComisionBanco(double dineroComisionBanco) {
        this.dineroComisionBanco = dineroComisionBanco;
    }

    public int getPorcentageComisionYapp() {
        return porcentageComisionYapp;
    }

    public void setPorcentageComisionYapp(int porcentageComisionYapp) {
        this.porcentageComisionYapp = porcentageComisionYapp;
    }

    public int getPorcentageComisionBanco() {
        return porcentageComisionBanco;
    }

    public void setPorcentageComisionBanco(int porcentageComisionBanco) {
        this.porcentageComisionBanco = porcentageComisionBanco;
    }

    public int getIdTiquete() {
        return idTiquete;
    }

    public void setIdTiquete(int idTiquete) {
        this.idTiquete = idTiquete;
    }

    public double getDineroOrganizador() {
        return dineroOrganizador;
    }

    public void setDineroOrganizador(double dineroOrganizador) {
        this.dineroOrganizador = dineroOrganizador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
