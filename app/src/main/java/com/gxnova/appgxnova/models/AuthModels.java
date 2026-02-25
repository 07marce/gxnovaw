package com.gxnova.appgxnova.models;

import com.google.gson.annotations.SerializedName;

// ─── AUTH ────────────────────────────────────────────────────────────────────

public class AuthModels {

    /** Body para POST /auth/login */
    public static class LoginRequest {
        @SerializedName("correo")
        public String correo;
        @SerializedName("contrasena")
        public String contrasena;

        public LoginRequest(String correo, String contrasena) {
            this.correo = correo;
            this.contrasena = contrasena;
        }
    }

    /** Respuesta de POST /auth/login */
    public static class LoginResponse {
        @SerializedName("token")
        public String token;
        @SerializedName("usuario")
        public UsuarioSimple usuario;
        @SerializedName("message")
        public String message;
    }

    /** Respuesta genérica de mensaje */
    public static class MessageResponse {
        @SerializedName("message")
        public String message;
    }
}
