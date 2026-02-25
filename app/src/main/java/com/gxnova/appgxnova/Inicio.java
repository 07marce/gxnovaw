package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Inicio extends AppCompatActivity {

    // Iconos de cabecera y botones de acción
    private ImageView btnNotificaciones;
    private CardView btnPublicar, btnTrabajar;

    // Barra de navegación inferior
    private LinearLayout navInicio, navBuscar, navPublicaciones, navChat, navPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        // --- 1. VINCULACIÓN DE VISTAS ---
        btnNotificaciones = findViewById(R.id.noti_icon);
        btnPublicar = findViewById(R.id.btn_publicar);
        btnTrabajar = findViewById(R.id.btn_trabajar);

        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navPublicaciones = findViewById(R.id.nav_publicaciones);
        navChat = findViewById(R.id.nav_chat);
        navPerfil = findViewById(R.id.nav_perfil);

        // --- 2. CONFIGURACIÓN DE CLICS ---

        // Notificaciones
        if (btnNotificaciones != null) {
            btnNotificaciones.setOnClickListener(v -> {
                Intent intent = new Intent(Inicio.this, Notificaciones.class);
                startActivity(intent);
            });
        }

        // Botón Publicar Empleo
        if (btnPublicar != null) {
            btnPublicar.setOnClickListener(v -> {
                Intent intent = new Intent(Inicio.this, Mis_trabajos.class);
                startActivity(intent);
            });
        }

        // Botón Trabajar
        if (btnTrabajar != null) {
            btnTrabajar.setOnClickListener(v -> {
                Intent intent = new Intent(Inicio.this, Buscar_trabajo.class);
                startActivity(intent);
            });
        }

        // --- 3. BARRA INFERIOR ---
        configurarBarraNavegacion();
    }

    private void configurarBarraNavegacion() {
        // Inicio - No hace nada porque ya estamos aquí
        navInicio.setOnClickListener(v -> {
            // Opcional: Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
        });

        // Buscar
        navBuscar.setOnClickListener(v -> {
            Intent intent = new Intent(this, Buscar_trabajo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Publicaciones
        navPublicaciones.setOnClickListener(v -> {
            Intent intent = new Intent(this, Mis_trabajos.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Chat
        navChat.setOnClickListener(v -> {
            Intent intent = new Intent(this, Chat.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Perfil
        navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, Perfil.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
    }
}