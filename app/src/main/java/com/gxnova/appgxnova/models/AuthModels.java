package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

public class AuthModels {

    /**
     * Body para POST /api/auth/login
     * El backend espera los campos: "correo" y "password"
     */
    public static class LoginRequest {
        @SerializedName("correo")
        public String correo;

        @SerializedName("password") // ← backend usa "password", NO "contrasena"
        public String password;

        public LoginRequest(String correo, String password) {
            this.correo = correo;
            this.password = password;
        }
    }

    /** Respuesta de POST /api/auth/login → { token, usuario } */
    public static class LoginResponse {
        @SerializedName("token")
        public String token;

        @SerializedName("usuario")
        public UsuarioCompleto usuario;

        @SerializedName("message")
        public String message;
    }

    /** Respuesta genérica de mensaje */
    public static class MessageResponse {
        @SerializedName("message")
        public String message;
    }

    // ─── Clases internas para la estructura de roles ──────────────────────────

    /** Objeto usuario completo devuelto tras login/register */
    public static class UsuarioCompleto {
        @SerializedName("id_usuario")
        public int id;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("correo")
        public String correo;

        @SerializedName("foto_perfil")
        public String fotoPerfil;

        /** Array con los roles asignados: [ { rol: { id_rol, nombre } } ] */
        @SerializedName("rolesAsignados")
        public java.util.List<RolAsignado> rolesAsignados;

        /** Devuelve el primer rol encontrado o cadena vacía */
        public String getPrimerRol() {
            if (rolesAsignados != null && !rolesAsignados.isEmpty()
                    && rolesAsignados.get(0).rol != null) {
                return rolesAsignados.get(0).rol.nombre;
            }
            return "";
        }
    }

    public static class RolAsignado {
        @SerializedName("rol")
        public RolInfo rol;
    }

    public static class RolInfo {
        @SerializedName("id_rol")
        public int idRol;

        @SerializedName("nombre")
        public String nombre;
    }
}
