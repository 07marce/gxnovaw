package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    protected LinearLayout navInicio, navBuscar, navPublicaciones, navChat, navPerfil;

    protected void setupBottomNavigation() {
        navInicio = findViewById(R.id.nav_inicio);
        navBuscar = findViewById(R.id.nav_buscar);
        navPublicaciones = findViewById(R.id.nav_publicaciones);
        navChat = findViewById(R.id.nav_chat);
        navPerfil = findViewById(R.id.nav_perfil);

        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                if (!this.getClass().getSimpleName().equals("Inicio")) {
                    startActivity(new Intent(this, Inicio.class));
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (navBuscar != null) {
            navBuscar.setOnClickListener(v -> {
                if (!this.getClass().getSimpleName().equals("Buscar_trabajo")) {
                    startActivity(new Intent(this, Buscar_trabajo.class));
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (navChat != null) {
            navChat.setOnClickListener(v -> {
                if (!this.getClass().getSimpleName().equals("Chat")) {
                    startActivity(new Intent(this, Chat.class));
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (navPublicaciones != null) {
            navPublicaciones.setOnClickListener(v -> {
                if (!this.getClass().getSimpleName().equals("MisPublicaciones")) {
                    startActivity(new Intent(this, MisPublicaciones.class));
                    overridePendingTransition(0, 0);
                }
            });
        }

        if (navPerfil != null) {
            navPerfil.setOnClickListener(v -> {
                if (!this.getClass().getSimpleName().equals("Perfil")) {
                    startActivity(new Intent(this, Perfil.class));
                    overridePendingTransition(0, 0);
                }
            });
        }
    }
}