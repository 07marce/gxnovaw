package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

/** Modelo de trabajo / publicación. */
public class Trabajo {
    @SerializedName("id_trabajo")
    public int id;

    @SerializedName("titulo")
    public String titulo;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("presupuesto")
    public double presupuesto;

    @SerializedName("modalidad")
    public String modalidad;

    @SerializedName("estado")
    public String estado;

    @SerializedName("foto")
    public String foto;

    @SerializedName("id_categoria")
    public int idCategoria;

    @SerializedName("created_at")
    public String createdAt;
}
