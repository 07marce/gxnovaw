package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class Mis_trabajos extends AppCompatActivity {

    private MaterialButton btnPublicarTrabajo, btnPublicarCentro, btnPublicarUrgente;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_trabajos);

        // 1. Botones de acción
        btnBack = findViewById(R.id.btnBack);
        btnPublicarTrabajo = findViewById(R.id.btnPublicarTrabajo);
        btnPublicarCentro = findViewById(R.id.btnPublicarCentro);
        btnPublicarUrgente = findViewById(R.id.btnPublicarUrgente);

        btnBack.setOnClickListener(v -> finish());

        // Ir a publicar (Formulario)
        btnPublicarTrabajo.setOnClickListener(v -> startActivity(new Intent(this, Publicar_trabajo.class)));
        btnPublicarCentro.setOnClickListener(v -> startActivity(new Intent(this, Publicar_trabajo.class)));
        btnPublicarUrgente.setOnClickListener(v -> startActivity(new Intent(this, Publicar_trabajo.class)));

        // 2. LA BARRA INFERIOR (Para que no se cierre la app)
        configurarBarra();
    }

    private void configurarBarra() {
        findViewById(R.id.nav_inicio).setOnClickListener(v -> startActivity(new Intent(this, Inicio.class)));
        findViewById(R.id.nav_buscar).setOnClickListener(v -> startActivity(new Intent(this, Buscar_trabajo.class)));
        findViewById(R.id.nav_chat).setOnClickListener(v -> startActivity(new Intent(this, Chat.class)));
        findViewById(R.id.nav_perfil).setOnClickListener(v -> startActivity(new Intent(this, Perfil.class)));
    }
}