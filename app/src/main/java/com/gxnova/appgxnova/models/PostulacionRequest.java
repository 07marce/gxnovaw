package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

/** Body para POST /postulaciones */
public class PostulacionRequest {
    @SerializedName("id_trabajo")
    public int idTrabajo;

    @SerializedName("mensaje")
    public String mensaje;

    public PostulacionRequest(int idTrabajo, String mensaje) {
        this.idTrabajo = idTrabajo;
        this.mensaje = mensaje;
    }
}
