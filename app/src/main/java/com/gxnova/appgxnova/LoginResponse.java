package com.gxnova.appgxnova;

public class LoginResponse {
    private String token; // Para que la sesión se mantenga abierta
    private String nombre;
    private String email;

    // Getters para poder usar los datos después
    public String getNombre() { return nombre; }
    public String getToken() { return token; }
}