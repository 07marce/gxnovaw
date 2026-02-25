package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity {

    // Definimos todas las variables de la barra
    private LinearLayout navInicio, navBuscar, navPublicaciones, navChat, navPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 1. VINCULACIÓN DE VISTAS (Usando los IDs del XML de 80dp)
        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navPublicaciones = findViewById(R.id.nav_publicaciones);
        navChat = findViewById(R.id.nav_chat); // Agregado para consistencia
        navPerfil = findViewById(R.id.nav_perfil);

        // 2. CONFIGURACIÓN DE CLICS
        configurarNavegacion();
    }

    private void configurarNavegacion() {

        // --- BOTÓN CLAVE: PUBLICACIONES ---
        // Lo cambiamos para que apunte a Mis_trabajos (el del diseño naranja)
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

        // BUSCAR
        if (navBuscar != null) {
            navBuscar.setOnClickListener(v -> {
                Intent i = new Intent(this, Buscar_trabajo.class);
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

        // CHAT (Estamos aquí, no hace falta que dispare un Intent nuevo)
        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                // Ya estás en Chat
            });
        }
    }
}