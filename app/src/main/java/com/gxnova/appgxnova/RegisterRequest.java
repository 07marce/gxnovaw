package com.gxnova.appgxnova;

public class RegisterRequest {
    // Estas variables deben llamarse igual a como las espera el Backend en Node.js
    private String nombre;
    private String apellido;
    private String celular;
    private String busqueda;
    private String email;
    private String password;

    // Este es el "Constructor": sirve para llenar la clase con datos
    public RegisterRequest(String nombre, String apellido, String celular, String busqueda, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.celular = celular;
        this.busqueda = busqueda;
        this.email = email;
        this.password = password;
    }

    // Nota: Retrofit usa estas variables para armar el JSON automáticamente
}