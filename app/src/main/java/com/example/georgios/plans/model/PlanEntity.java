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
    private String fechaInicio;
    private String fechaFinal;
    private long costoPromedio;
    private long creadorPlan;
    private long detallePreferencia;
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

    public long getCostoPromedio() {
        return costoPromedio;
    }

    public long getCreadorPlan() {
        return creadorPlan;
    }

    public long getDetallePreferencia() {
        return detallePreferencia;
    }

    public void setCostoPromedio(long costoPromedio) {
        this.costoPromedio = costoPromedio;
    }

    public void setCreadorPlan(long creadorPlan) {
        this.creadorPlan = creadorPlan;
    }

    public void setDetallePreferencia(long detallePreferencia) {
        this.detallePreferencia = detallePreferencia;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
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
