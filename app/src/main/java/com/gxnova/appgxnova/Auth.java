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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private TextView tabLogin, tabRegister, txtNombreCedula, txtNombreSelfie, txtNoCuenta;
    private EditText etEmailLogin, etPassLogin, etNombre, etApellido, etCelular, etEmailReg, regPass, regPassConfirm;
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

        // LÓGICA DE LOGIN
        btnLogin.setOnClickListener(v -> {
            String email = etEmailLogin.getText().toString().trim();
            String pass = etPassLogin.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor complete los datos", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest loginRequest = new LoginRequest(email, pass);
            RetrofitClient.getApiService().loginUsuario(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(Auth.this, "Bienvenido " + response.body().getNombre(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Auth.this, Inicio.class));
                        finish();
                    } else {
                        Toast.makeText(Auth.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Auth.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // LÓGICA DE REGISTRO
        btnRegister.setOnClickListener(v -> {
            String pass = regPass.getText().toString();
            String passConfirm = regPassConfirm.getText().toString();

            if (!checkTerms.isChecked()) {
                Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            } else if (pass.isEmpty() || !pass.equals(passConfirm)) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else if (etNombre.getText().toString().isEmpty() || etEmailReg.getText().toString().isEmpty()) {
                Toast.makeText(this, "Nombre y Email son obligatorios", Toast.LENGTH_SHORT).show();
            } else {
                ejecutarRegistro();
            }
        });
    }

    private void ejecutarRegistro() {
        RegisterRequest request = new RegisterRequest(
                etNombre.getText().toString().trim(),
                etApellido.getText().toString().trim(),
                etCelular.getText().toString().trim(),
                spinnerBusqueda.getSelectedItem().toString(),
                etEmailReg.getText().toString().trim(),
                regPass.getText().toString()
        );

        RetrofitClient.getApiService().registrarUsuario(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Auth.this, "¡Registro exitoso! Ya puedes iniciar sesión", Toast.LENGTH_LONG).show();
                    tabLogin.performClick();
                } else {
                    Toast.makeText(Auth.this, "Error: El usuario ya existe o datos inválidos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Auth.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

        // VINCULACIÓN DE NUEVOS IDS AGREGADOS AL XML
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPassLogin = findViewById(R.id.etPassLogin);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCelular = findViewById(R.id.etCelular);
        etEmailReg = findViewById(R.id.etEmailReg);
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