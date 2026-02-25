package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxnova.appgxnova.models.Trabajo;
import com.gxnova.appgxnova.network.ApiService;
import com.gxnova.appgxnova.network.NetworkClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Buscar_trabajo extends AppCompatActivity {

    private LinearLayout navInicio, navBuscar, navPublicaciones, navChat, navPerfil;
    private ListView listViewTrabajos;
    private ArrayAdapter<String> adapter;
    private List<String> titulosTrabajos = new ArrayList<>();

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_trabajo);

        apiService = NetworkClient.getApiService();

        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navPublicaciones = findViewById(R.id.nav_publicaciones);
        navChat = findViewById(R.id.nav_chat);
        navPerfil = findViewById(R.id.nav_perfil);

        LinearLayout btnVolver = findViewById(R.id.btn_volver_contenedor);
        if (btnVolver != null)
            btnVolver.setOnClickListener(v -> finish());

        // ListView para mostrar trabajos
        listViewTrabajos = findViewById(R.id.list_trabajos);
        if (listViewTrabajos != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulosTrabajos);
            listViewTrabajos.setAdapter(adapter);
        }

        configurarNavegacion();
        cargarTrabajos();
    }

    private void cargarTrabajos() {
        apiService.getTrabajos().enqueue(new Callback<List<Trabajo>>() {
            @Override
            public void onResponse(Call<List<Trabajo>> call, Response<List<Trabajo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    titulosTrabajos.clear();
                    for (Trabajo t : response.body()) {
                        String item = t.titulo + "\n" + t.modalidad + " - $" + (int) t.presupuesto;
                        titulosTrabajos.add(item);
                    }
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Buscar_trabajo.this, "Sin resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Trabajo>> call, Throwable t) {
                Toast.makeText(Buscar_trabajo.this, "Error al cargar trabajos: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void configurarNavegacion() {
        if (navPublicaciones != null)
            navPublicaciones.setOnClickListener(v -> startActivity(
                    new Intent(this, Mis_trabajos.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        if (navInicio != null)
            navInicio.setOnClickListener(
                    v -> startActivity(new Intent(this, Inicio.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        if (navChat != null)
            navChat.setOnClickListener(
                    v -> startActivity(new Intent(this, Chat.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        if (navPerfil != null)
            navPerfil.setOnClickListener(
                    v -> startActivity(new Intent(this, Perfil.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
    }
}