package com.example.georgios.plans.model;

import android.app.Application;

import java.sql.Blob;

/**
 * Created by IkosidekasDesktop on 22/11/2017.
 */

public class UsuarioEntity {

    private long idUsuario;
    private String email;
    private String contrasena;
    private String nombres;
    private String apellidos;
    private String usuario;
    private String tipoId;
    private Blob fotoPerfil;
    private String numeroId;

    public long getIdUsuario() {
        return idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTipoId() {
        return tipoId;
    }

    public Blob getFotoPerfil() {
        return fotoPerfil;
    }

    public String getNumeroId() {
        return numeroId;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public void setFotoPerfil(Blob fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setNumeroId(String numeroId) {
        this.numeroId = numeroId;
    }
}
