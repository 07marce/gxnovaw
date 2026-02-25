package com.gxnova.appgxnova;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Publicar_trabajo extends AppCompatActivity {

    // Declaramos el Spinner
    private Spinner spinnerCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_trabajo);

        // 1. VINCULAR SPINNER Y CONFIGURARLO
        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        configurarSpinner();

        // BOTÓN VOLVER
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // BOTÓN PUBLICAR FINAL
        Button btnPublicar = findViewById(R.id.btnPublicarServicioFinal);
        if (btnPublicar != null) {
            btnPublicar.setOnClickListener(v -> {
                // Validación de categoría
                if (spinnerCategorias.getSelectedItemPosition() == 0) {
                    Toast.makeText(this, "Selecciona una categoría válida", Toast.LENGTH_SHORT).show();
                } else {
                    String seleccion = spinnerCategorias.getSelectedItem().toString();
                    Toast.makeText(this, "Servicio Publicado en " + seleccion, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        // NAVEGACIÓN INFERIOR
        setupNavegacion();
    }

    private void configurarSpinner() {
        // Lista de categorías para GXNOVA
        String[] opciones = {"Selecciona una categoría", "Tecnología", "Construcción", "Hogar", "Transporte", "Otros"};

        // Adaptador con lógica para que la primera opción se vea gris y no sea seleccionable
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0; // Deshabilita la posición 0
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY); // Color para la instrucción
                } else {
                    tv.setTextColor(Color.BLACK); // Color para opciones reales
                }
                return view;
            }
        };

        if (spinnerCategorias != null) {
            spinnerCategorias.setAdapter(adapter);
        }
    }

    private void setupNavegacion() {
        LinearLayout navInicio = findViewById(R.id.nav_inicio);
        LinearLayout navBuscar = findViewById(R.id.nav_buscar);
        LinearLayout navPerfil = findViewById(R.id.nav_perfil);

        if (navInicio != null) {
            navInicio.setOnClickListener(v -> {
                startActivity(new Intent(this, Inicio.class));
                finish();
            });
        }
        if (navBuscar != null) {
            navBuscar.setOnClickListener(v -> startActivity(new Intent(this, Buscar_trabajo.class)));
        }
        if (navPerfil != null) {
            navPerfil.setOnClickListener(v -> startActivity(new Intent(this, Perfil.class)));
        }
    }
}