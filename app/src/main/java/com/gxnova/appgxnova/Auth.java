package com.gxnova.appgxnova;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Auth extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private TextView tabLogin, tabRegister, txtNombreCedula, txtNombreSelfie, txtNoCuenta;
    private EditText regPass, regPassConfirm;
    private LinearLayout layoutLogin, layoutRegister;
    private View indicatorLogin, indicatorRegister;
    private RelativeLayout areaCedula, areaSelfie;
    private CheckBox checkTerms;
    private Spinner spinnerBusqueda;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private String tipoCaptura = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        vincularVistas();
        configurarSpinner();
        configurarCamara();
        configurarTabs();

        txtNoCuenta.setOnClickListener(v -> tabRegister.performClick());

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, Inicio.class));
            finish();
        });

        btnRegister.setOnClickListener(v -> {
            if (!checkTerms.isChecked()) {
                Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            } else if (regPass.getText().toString().isEmpty() || !regPass.getText().toString().equals(regPassConfirm.getText().toString())) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, Inicio.class));
                finish();
            }
        });
    }

    private void vincularVistas() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tabLogin = findViewById(R.id.tabLogin);
        tabRegister = findViewById(R.id.tabRegister);
        layoutLogin = findViewById(R.id.layoutLogin);
        layoutRegister = findViewById(R.id.layoutRegister);
        indicatorLogin = findViewById(R.id.indicatorLogin);
        indicatorRegister = findViewById(R.id.indicatorRegister);
        txtNombreCedula = findViewById(R.id.txt_nombre_cedula);
        txtNombreSelfie = findViewById(R.id.txt_nombre_selfie);
        areaCedula = findViewById(R.id.area_cedula);
        areaSelfie = findViewById(R.id.area_selfie);
        checkTerms = findViewById(R.id.checkTerms);
        spinnerBusqueda = findViewById(R.id.spinnerBusqueda);
        regPass = findViewById(R.id.regPass);
        regPassConfirm = findViewById(R.id.regPassConfirm);
        txtNoCuenta = findViewById(R.id.txtNoCuenta);
    }

    private void configurarSpinner() {
        String[] ops = {"¿Qué buscas?", "Quiero trabajar", "Quiero contratar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ops) {
            @Override
            public boolean isEnabled(int pos) { return pos != 0; }
            @Override
            public View getDropDownView(int pos, @Nullable View conv, @NonNull ViewGroup par) {
                View v = super.getDropDownView(pos, conv, par);
                ((TextView) v).setTextColor(pos == 0 ? Color.GRAY : Color.BLACK);
                return v;
            }
        };
        spinnerBusqueda.setAdapter(adapter);
    }

    private void configurarCamara() {
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                if (tipoCaptura.equals("CEDULA")) {
                    txtNombreCedula.setText("Cédula Capturada ✅");
                    txtNombreCedula.setTextColor(Color.BLACK);
                } else {
                    txtNombreSelfie.setText("Selfie Capturada ✅");
                    txtNombreSelfie.setTextColor(Color.BLACK);
                }
            }
        });
        areaCedula.setOnClickListener(v -> { tipoCaptura = "CEDULA"; cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); });
        areaSelfie.setOnClickListener(v -> { tipoCaptura = "SELFIE"; cameraLauncher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)); });
    }

    private void configurarTabs() {
        tabLogin.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutRegister.setVisibility(View.GONE);
            indicatorLogin.setVisibility(View.VISIBLE);
            indicatorRegister.setVisibility(View.INVISIBLE);
            tabLogin.setTextColor(Color.parseColor("#FF5722"));
            tabRegister.setTextColor(Color.GRAY);
        });
        tabRegister.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.GONE);
            layoutRegister.setVisibility(View.VISIBLE);
            indicatorLogin.setVisibility(View.INVISIBLE);
            indicatorRegister.setVisibility(View.VISIBLE);
            tabRegister.setTextColor(Color.parseColor("#FF5722"));
            tabLogin.setTextColor(Color.GRAY);
        });
    }
}