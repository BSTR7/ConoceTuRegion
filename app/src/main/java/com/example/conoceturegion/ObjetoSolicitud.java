package com.example.conoceturegion;

import com.google.android.gms.maps.model.LatLng;

public class ObjetoSolicitud {
    private int id;
    private String nombresolicitud;
    private String email;
    private LatLng latlngSolicitud;
    private boolean estado;  // Agregado

    public ObjetoSolicitud() {
    }

    public ObjetoSolicitud(int id, String nombresolicitud, String email, LatLng latlngSolicitud, boolean estado) {
        this.id = id;
        this.nombresolicitud = nombresolicitud;
        this.email = email;
        this.latlngSolicitud = latlngSolicitud;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombresolicitud() {
        return nombresolicitud;
    }

    public void setNombresolicitud(String nombresolicitud) {
        this.nombresolicitud = nombresolicitud;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LatLng getLatlngSolicitud() {
        return latlngSolicitud;
    }

    public void setLatlngSolicitud(LatLng latlngSolicitud) {
        this.latlngSolicitud = latlngSolicitud;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean getEstado() {
        return estado;
    }
}