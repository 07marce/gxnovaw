package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

/** Modelo de usuario simplificado (respuesta login / perfil). */
public class UsuarioSimple {
    @SerializedName("id_usuario")
    public int id;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("correo")
    public String correo;

    @SerializedName("foto_perfil")
    public String fotoPerfil;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("telefono")
    public String telefono;

    @SerializedName("ciudad")
    public String ciudad;

    @SerializedName("pais")
    public String pais;

    /**
     * El rol viene como objeto nested en algunos endpoints; lo aplanamos aquí solo
     * como String auxiliar.
     */
    @SerializedName("rol")
    public String rol;
}
