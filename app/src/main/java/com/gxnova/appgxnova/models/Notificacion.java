package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

/** Modelo de notificación. */
public class Notificacion {
    @SerializedName("id_notificacion")
    public int id;

    @SerializedName("titulo")
    public String titulo;

    @SerializedName("mensaje")
    public String mensaje;

    @SerializedName("leida")
    public boolean leida;

    @SerializedName("created_at")
    public String createdAt;
}
