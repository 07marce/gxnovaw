package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

/** Modelo de categoría. */
public class Categoria {
    @SerializedName("id_categoria")
    public int id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("descripcion")
    public String descripcion;

    /** Sobreescribe toString() para que los Spinners muestren el nombre. */
    @Override
    public String toString() {
        return nombre != null ? nombre : "";
    }
}
