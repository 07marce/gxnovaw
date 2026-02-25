package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class Buscar_trabajo extends AppCompatActivity {

    private LinearLayout navInicio, navBuscar, navPublicaciones, navChat, navPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_trabajo);

        // 1. Vincular los IDs de la barra (asegúrate que en el XML sean estos)
        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navPublicaciones = findViewById(R.id.nav_publicaciones);
        navChat = findViewById(R.id.nav_chat);
        navPerfil = findViewById(R.id.nav_perfil);

        // 2. Configurar el botón de volver que ya tenías
        LinearLayout btnVolver = findViewById(R.id.btn_volver_contenedor);
        if (btnVolver != null) {
            btnVolver.setOnClickListener(v -> finish());
        }

        // 3. Activar la navegación
        configurarNavegacion();
    }

    private void configurarNavegacion() {

        // --- BOTÓN PUBLICACIONES -> SIEMPRE A MIS_TRABAJOS ---
        if (navPublicaciones != null) {
            navPublicaciones.setOnClickListener(v -> {
                Intent i = new Intent(this, Mis_trabajos.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            });
        }

        // INICIO
        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                Intent i = new Intent(this, Inicio.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            });
        }

        // CHAT
        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                Intent i = new Intent(this, Chat.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            });
        }

        // PERFIL
        if (navPerfil != null) {
            navPerfil.setOnClickListener(v -> {
                Intent i = new Intent(this, Perfil.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            });
        }

        // BUSCAR (Ya estamos aquí)
        if (navBuscar != null) {
            navBuscar.setOnClickListener(v -> {
                // No hace nada para no recargar la misma pantalla
            });
        }
    }
}