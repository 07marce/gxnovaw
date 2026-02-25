package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxnova.appgxnova.models.UsuarioSimple;
import com.gxnova.appgxnova.network.ApiService;
import com.gxnova.appgxnova.network.NetworkClient;
import com.gxnova.appgxnova.network.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends AppCompatActivity {

    private LinearLayout layoutPersonal, layoutProfesional;
    private TextView tabPersonal, tabProfesional;

    // Campos del perfil
    private TextView txtNombre, txtCorreo, txtTelefono, txtCiudad, txtDescripcion;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        apiService = NetworkClient.getApiService();
        sessionManager = new SessionManager(this);

        // Tabs
        layoutPersonal = findViewById(R.id.layout_personal);
        layoutProfesional = findViewById(R.id.layout_profesional);
        tabPersonal = findViewById(R.id.tab_personal);
        tabProfesional = findViewById(R.id.tab_profesional);

        // Campos de texto a poblar
        txtNombre = findViewById(R.id.txt_nombre_usuario);
        txtCorreo = findViewById(R.id.txt_correo_usuario);
        txtTelefono = findViewById(R.id.txt_telefono_usuario);
        txtCiudad = findViewById(R.id.txt_ciudad_usuario);
        txtDescripcion = findViewById(R.id.txt_descripcion_usuario);

        if (tabPersonal != null)
            tabPersonal.setOnClickListener(v -> mostrarPersonal());
        if (tabProfesional != null)
            tabProfesional.setOnClickListener(v -> mostrarProfesional());

        configurarNavegacion();
        cargarPerfil();
    }

    private void cargarPerfil() {
        String token = sessionManager.getBearerToken();
        if (token == null)
            return;

        apiService.getPerfil(token).enqueue(new Callback<UsuarioSimple>() {
            @Override
            public void onResponse(Call<UsuarioSimple> call, Response<UsuarioSimple> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UsuarioSimple u = response.body();
                    if (txtNombre != null)
                        txtNombre.setText(u.nombre != null ? u.nombre : "—");
                    if (txtCorreo != null)
                        txtCorreo.setText(u.correo != null ? u.correo : "—");
                    if (txtTelefono != null)
                        txtTelefono.setText(u.telefono != null ? u.telefono : "—");
                    if (txtCiudad != null)
                        txtCiudad.setText(u.ciudad != null ? u.ciudad + (u.pais != null ? ", " + u.pais : "") : "—");
                    if (txtDescripcion != null)
                        txtDescripcion.setText(u.descripcion != null ? u.descripcion : "Sin descripción");
                }
            }

            @Override
            public void onFailure(Call<UsuarioSimple> call, Throwable t) {
                Toast.makeText(Perfil.this, "Error al cargar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarPersonal() {
        if (layoutPersonal != null)
            layoutPersonal.setVisibility(View.VISIBLE);
        if (layoutProfesional != null)
            layoutProfesional.setVisibility(View.GONE);
        if (tabPersonal != null) {
            tabPersonal.setBackgroundColor(android.graphics.Color.parseColor("#FF6A00"));
            tabPersonal.setTextColor(android.graphics.Color.WHITE);
        }
        if (tabProfesional != null) {
            tabProfesional.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
            tabProfesional.setTextColor(android.graphics.Color.parseColor("#666666"));
        }
    }

    private void mostrarProfesional() {
        if (layoutPersonal != null)
            layoutPersonal.setVisibility(View.GONE);
        if (layoutProfesional != null)
            layoutProfesional.setVisibility(View.VISIBLE);
        if (tabProfesional != null) {
            tabProfesional.setBackgroundColor(android.graphics.Color.parseColor("#FF6A00"));
            tabProfesional.setTextColor(android.graphics.Color.WHITE);
        }
        if (tabPersonal != null) {
            tabPersonal.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
            tabPersonal.setTextColor(android.graphics.Color.parseColor("#666666"));
        }
    }

    private void configurarNavegacion() {
        View navInicio = findViewById(R.id.nav_inicio);
        View navBuscar = findViewById(R.id.nav_buscar);
        View navPublicaciones = findViewById(R.id.nav_publicaciones);
        View navChat = findViewById(R.id.nav_chat);

        if (navInicio != null)
            navInicio.setOnClickListener(v -> {
                startActivity(new Intent(this, Inicio.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            });
        if (navBuscar != null)
            navBuscar.setOnClickListener(v -> {
                startActivity(new Intent(this, Buscar_trabajo.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            });
        if (navPublicaciones != null)
            navPublicaciones.setOnClickListener(v -> {
                startActivity(new Intent(this, Mis_trabajos.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            });
        if (navChat != null)
            navChat.setOnClickListener(v -> {
                startActivity(new Intent(this, Chat.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
            });
    }
}