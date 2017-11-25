package com.example.georgios.plans.model;

import android.app.Application;

import java.sql.Blob;
import java.sql.Timestamp;

/**
 * Created by IkosidekasDesktop on 22/11/2017.
 */

public class PlanEntity {

    private long idPlan;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private Timestamp fechaInicio;
    private Timestamp fechaFinal;
    private int costoPromedio;
    private int creadorPlan;
    private int detallePreferencia;
    private Blob imagenPlan;

    public long getIdPlan() {
        return idPlan;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public Timestamp getFechaFinal() {
        return fechaFinal;
    }

    public int getCostoPromedio() {
        return costoPromedio;
    }

    public int getCreadorPlan() {
        return creadorPlan;
    }

    public int getDetallePreferencia() {
        return detallePreferencia;
    }

    public Blob getImagenPlan() {
        return imagenPlan;
    }

    public void setIdPlan(long idPlan) {
        this.idPlan = idPlan;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFinal(Timestamp fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setCostoPromedio(int costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public void setCreadorPlan(int creadorPlan) {
        this.creadorPlan = creadorPlan;
    }

    public void setDetallePreferencia(int detallePreferencia) {
        this.detallePreferencia = detallePreferencia;
    }

    public void setImagenPlan(Blob imagenPlan) {
        this.imagenPlan = imagenPlan;
    }
}
