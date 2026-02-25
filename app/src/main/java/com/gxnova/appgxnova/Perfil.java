package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Perfil extends AppCompatActivity {

    // Variables para los Tabs del diseño
    private LinearLayout layoutPersonal, layoutProfesional;
    private TextView tabPersonal, tabProfesional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // --- 1. VINCULAR LOS TABS (Para que el selector funcione) ---
        layoutPersonal = findViewById(R.id.layout_personal);
        layoutProfesional = findViewById(R.id.layout_profesional);
        tabPersonal = findViewById(R.id.tab_personal);
        tabProfesional = findViewById(R.id.tab_profesional);

        // Configurar clics de los Tabs
        if (tabPersonal != null) {
            tabPersonal.setOnClickListener(v -> mostrarPersonal());
        }
        if (tabProfesional != null) {
            tabProfesional.setOnClickListener(v -> mostrarProfesional());
        }

        // --- 2. CONFIGURAR BARRA DE NAVEGACIÓN ---
        configurarNavegacion();
    }

    // Lógica para mostrar Información Personal
    private void mostrarPersonal() {
        layoutPersonal.setVisibility(View.VISIBLE);
        layoutProfesional.setVisibility(View.GONE);

        // Cambiar colores para que se vea cuál está seleccionado
        tabPersonal.setBackgroundColor(android.graphics.Color.parseColor("#FF6A00"));
        tabPersonal.setTextColor(android.graphics.Color.WHITE);
        tabProfesional.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
        tabProfesional.setTextColor(android.graphics.Color.parseColor("#666666"));
    }

    // Lógica para mostrar Información Profesional
    private void mostrarProfesional() {
        layoutPersonal.setVisibility(View.GONE);
        layoutProfesional.setVisibility(View.VISIBLE);

        // Cambiar colores para que se vea cuál está seleccionado
        tabProfesional.setBackgroundColor(android.graphics.Color.parseColor("#FF6A00"));
        tabProfesional.setTextColor(android.graphics.Color.WHITE);
        tabPersonal.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
        tabPersonal.setTextColor(android.graphics.Color.parseColor("#666666"));
    }

    private void configurarNavegacion() {
        // Botón INICIO
        findViewById(R.id.nav_inicio).setOnClickListener(v -> {
            Intent i = new Intent(this, Inicio.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        });

        // Botón BUSCAR
        findViewById(R.id.nav_buscar).setOnClickListener(v -> {
            Intent i = new Intent(this, Buscar_trabajo.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        });

        // Botón PUBLICACIONES (Corregido a Mis_trabajos)
        findViewById(R.id.nav_publicaciones).setOnClickListener(v -> {
            Intent i = new Intent(this, Mis_trabajos.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        });

        // Botón CHAT
        findViewById(R.id.nav_chat).setOnClickListener(v -> {
            Intent i = new Intent(this, Chat.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
        });

        // Botón PERFIL (Ya estás aquí)
        findViewById(R.id.nav_perfil).setOnClickListener(v -> {
            // Ya estás en Perfil, no hace falta abrirlo de nuevo
        });
    }
}