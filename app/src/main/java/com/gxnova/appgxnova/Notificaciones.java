package com.gxnova.appgxnova;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxnova.appgxnova.models.Notificacion;
import com.gxnova.appgxnova.network.ApiService;
import com.gxnova.appgxnova.network.NetworkClient;
import com.gxnova.appgxnova.network.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notificaciones extends AppCompatActivity {

    private ListView listViewNotificaciones;
    private ArrayAdapter<String> adapter;
    private List<String> items = new ArrayList<>();

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        apiService = NetworkClient.getApiService();
        sessionManager = new SessionManager(this);

        listViewNotificaciones = findViewById(R.id.list_notificaciones);
        if (listViewNotificaciones != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            listViewNotificaciones.setAdapter(adapter);
        }

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        String token = sessionManager.getBearerToken();
        if (token == null) {
            Toast.makeText(this, "Sin sesión activa", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService.getNotificaciones(token).enqueue(new Callback<List<Notificacion>>() {
            @Override
            public void onResponse(Call<List<Notificacion>> call, Response<List<Notificacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    items.clear();
                    if (response.body().isEmpty()) {
                        items.add("No tienes notificaciones.");
                    } else {
                        for (Notificacion n : response.body()) {
                            String estado = n.leida ? "✓ " : "🔔 ";
                            items.add(estado + n.titulo + "\n" + n.mensaje);
                        }
                    }
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Notificacion>> call, Throwable t) {
                Toast.makeText(Notificaciones.this, "Error al cargar notificaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}