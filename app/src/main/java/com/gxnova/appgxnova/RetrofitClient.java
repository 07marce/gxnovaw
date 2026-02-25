package com.gxnova.appgxnova;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    // La URL base de tu backend en Railway
    private static final String BASE_URL = "https://backend-gxnova-production.up.railway.app/";

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Esto traduce Java a JSON
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}