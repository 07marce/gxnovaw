package com.gxnova.appgxnova;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/register")
    Call<Void> registrarUsuario(@Body RegisterRequest request);

    @POST("auth/login")
    Call<LoginResponse> loginUsuario(@Body LoginRequest request); // Aquí ya espera respuesta
}