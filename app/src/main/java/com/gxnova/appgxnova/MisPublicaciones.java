package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MisPublicaciones extends AppCompatActivity {

    private TextView tabActivas, tabExpiradas;
    private View navChat; // Cambiado a View porque en el XML es un RelativeLayout
    private LinearLayout navInicio, navBuscar, navPerfil, btnVolver;
    private Button btnCrearPublicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Asegúrate que tu archivo XML se llame exactamente así
        setContentView(R.layout.activity_trabajos_publicados);

        // 2. VINCULACIÓN CON PROTECCIÓN
        tabActivas = findViewById(R.id.tab_activas);
        tabExpiradas = findViewById(R.id.tab_expiradas);
        btnVolver = findViewById(R.id.btn_volver_contenedor);
        btnCrearPublicacion = findViewById(R.id.btn_crear_publicacion_centro);

        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navChat = findViewById(R.id.nav_chat); // Aquí estaba el choque
        navPerfil = findViewById(R.id.nav_perfil);

        // 3. LÓGICA DE PESTAÑAS
        if (tabActivas != null && tabExpiradas != null) {
            tabActivas.setOnClickListener(v -> {
                tabActivas.setBackgroundResource(R.drawable.bd_rounded);
                tabActivas.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
                tabActivas.setTextColor(android.graphics.Color.parseColor("#FF6A00"));
                tabExpiradas.setBackground(null);
                tabExpiradas.setTextColor(android.graphics.Color.WHITE);
            });

            tabExpiradas.setOnClickListener(v -> {
                tabExpiradas.setBackgroundResource(R.drawable.bd_rounded);
                tabExpiradas.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE));
                tabExpiradas.setTextColor(android.graphics.Color.parseColor("#FF6A00"));
                tabActivas.setBackground(null);
                tabActivas.setTextColor(android.graphics.Color.WHITE);
            });
        }

        // 4. LÓGICA DE NAVEGACIÓN (Barra Inferior)
        if (btnVolver != null) btnVolver.setOnClickListener(v -> finish());

        if (btnCrearPublicacion != null) {
            btnCrearPublicacion.setOnClickListener(v -> startActivity(new Intent(this, Publicar_trabajo.class)));
        }

        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                Intent i = new Intent(this, Inicio.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
            });
        }

        if (navBuscar != null) {
            navBuscar.setOnClickListener(v -> startActivity(new Intent(this, Buscar_trabajo.class)));
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> startActivity(new Intent(this, Chat.class)));
        }

        if (navPerfil != null) {
            navPerfil.setOnClickListener(v -> startActivity(new Intent(this, Perfil.class)));
        }
    }
}