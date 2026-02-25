package com.gxnova.appgxnova;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
// Auth.java — FIXED: backend uses 'password' not 'contrasena'
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

import com.gxnova.appgxnova.models.AuthModels;
import com.gxnova.appgxnova.network.ApiService;
import com.gxnova.appgxnova.network.NetworkClient;
import com.gxnova.appgxnova.network.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Auth extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private TextView tabLogin, tabRegister, txtNombreCedula, txtNombreSelfie, txtNoCuenta;
    private EditText regPass, regPassConfirm, editCorreo, editContrasena;
    private EditText regNombre, regCorreo, regTelefono, regCiudad, regPais;
    private LinearLayout layoutLogin, layoutRegister;
    private View indicatorLogin, indicatorRegister;
    private RelativeLayout areaCedula, areaSelfie;
    private CheckBox checkTerms;
    private Spinner spinnerBusqueda;

    // Captura de archivos
    private ActivityResultLauncher<Intent> cameraLauncher;
    private String tipoCaptura = "";
    private Uri uriCedula = null;
    private Uri uriSelfie = null;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        apiService = NetworkClient.getApiService();
        sessionManager = new SessionManager(this);

        // Si ya hay sesión, ir directo a Inicio
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, Inicio.class));
            finish();
            return;
        }

        vincularVistas();
        configurarSpinner();
        configurarCamara();
        configurarTabs();

        txtNoCuenta.setOnClickListener(v -> tabRegister.performClick());

        btnLogin.setOnClickListener(v -> doLogin());
        btnRegister.setOnClickListener(v -> doRegister());
    }

    private void doLogin() {
        String correo = editCorreo != null ? editCorreo.getText().toString().trim() : "";
        // FIXED: el backend espera el campo "password", no "contrasena"
        String password = editContrasena != null ? editContrasena.getText().toString() : "";

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Ingresando...");

        AuthModels.LoginRequest body = new AuthModels.LoginRequest(correo, password);
        apiService.login(body).enqueue(new Callback<AuthModels.LoginResponse>() {
            @Override
            public void onResponse(Call<AuthModels.LoginResponse> call, Response<AuthModels.LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Ingresar");
                if (response.isSuccessful() && response.body() != null) {
                    AuthModels.LoginResponse res = response.body();
                    // FIXED: usar UsuarioCompleto con getPrimerRol() para rolesAsignados[]
                    AuthModels.UsuarioCompleto u = res.usuario;
                    String rol = (u != null) ? u.getPrimerRol() : "";
                    int userId = (u != null) ? u.id : -1;
                    String name = (u != null && u.nombre != null) ? u.nombre : "";
                    sessionManager.saveSession(res.token, userId, name, rol);
                    startActivity(new Intent(Auth.this, Inicio.class));
                    finish();
                } else {
                    // Mostrar el mensaje real del servidor si existe
                    String errorMsg = "Correo o contraseña incorrectos";
                    try {
                        if (response.errorBody() != null) {
                            String rawError = response.errorBody().string();
                            if (rawError.contains("message")) {
                                errorMsg = rawError;
                            }
                        }
                    } catch (Exception ignored) {
                    }
                    Toast.makeText(Auth.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthModels.LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Ingresar");
                Toast.makeText(Auth.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doRegister() {
        if (!checkTerms.isChecked()) {
            Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show();
            return;
        }

        // FIXED: el campo en el backend se llama "password"
        String pass = regPass.getText().toString();
        String passConf = regPassConfirm.getText().toString();
        if (pass.isEmpty() || !pass.equals(passConf)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Campos obligatorios del backend: nombre, correo, password, telefono,
        // rolNombre
        String nombre = regNombre != null ? regNombre.getText().toString().trim() : "";
        String correo = regCorreo != null ? regCorreo.getText().toString().trim() : "";
        String telefono = regTelefono != null ? regTelefono.getText().toString().trim() : "";
        // rolNombre: "Trabajador" o "Empleador" según lo que eligió el usuario
        String rolNombre = spinnerBusqueda.getSelectedItemPosition() == 0 ? "Trabajador" : "Empleador";

        if (nombre.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Completa los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Registrando...");

        RequestBody rbNombre = toRequestBody(nombre);
        RequestBody rbCorreo = toRequestBody(correo);
        RequestBody rbPass = toRequestBody(pass);
        RequestBody rbTelefono = toRequestBody(telefono);
        RequestBody rbRol = toRequestBody(rolNombre); // campo "rolNombre" que espera el backend

        // Imágenes: si no se capturaron usamos archivos vacíos
        MultipartBody.Part partCedula = uriToMultipart(uriCedula, "foto_cedula");
        MultipartBody.Part partSelfie = uriToMultipart(uriSelfie, "selfie");
        MultipartBody.Part partFotoPerfil = uriToMultipart(uriSelfie, "foto_perfil");

        // FIXED: coincide con la firma corregida en ApiService (password + rolNombre,
        // sin ciudad/pais/busqueda)
        apiService.register(rbNombre, rbCorreo, rbPass, rbTelefono, rbRol,
                partCedula, partSelfie, partFotoPerfil)
                .enqueue(new Callback<AuthModels.MessageResponse>() {
                    @Override
                    public void onResponse(Call<AuthModels.MessageResponse> call,
                            Response<AuthModels.MessageResponse> response) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Registrarme");
                        if (response.isSuccessful()) {
                            Toast.makeText(Auth.this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_LONG).show();
                            tabLogin.performClick();
                        } else {
                            Toast.makeText(Auth.this, "Error al registrar. Verifica tus datos.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthModels.MessageResponse> call, Throwable t) {
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Registrarme");
                        Toast.makeText(Auth.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ─── Utilidades ───────────────────────────────────────────────────────────

    private RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private MultipartBody.Part uriToMultipart(Uri uri, String fieldName) {
        if (uri == null) {
            // Archivo vacío como fallback
            RequestBody empty = RequestBody.create(MediaType.parse("image/jpeg"), new byte[0]);
            return MultipartBody.Part.createFormData(fieldName, fieldName + ".jpg", empty);
        }
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            File tmpFile = File.createTempFile(fieldName, ".jpg", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tmpFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0)
                fos.write(buf, 0, len);
            fos.close();
            is.close();
            RequestBody rb = RequestBody.create(MediaType.parse("image/jpeg"), tmpFile);
            return MultipartBody.Part.createFormData(fieldName, tmpFile.getName(), rb);
        } catch (IOException e) {
            RequestBody empty = RequestBody.create(MediaType.parse("image/jpeg"), new byte[0]);
            return MultipartBody.Part.createFormData(fieldName, fieldName + ".jpg", empty);
        }
    }

    // ─── Binding ──────────────────────────────────────────────────────────────

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
        // Campos de login
        editCorreo = findViewById(R.id.editCorreo);
        editContrasena = findViewById(R.id.editContrasena);
        // Campos de registro
        regNombre = findViewById(R.id.regNombre);
        regCorreo = findViewById(R.id.regCorreo);
        regTelefono = findViewById(R.id.regTelefono);
        regCiudad = findViewById(R.id.regCiudad);
        regPais = findViewById(R.id.regPais);
    }

    private void configurarSpinner() {
        String[] ops = { "¿Qué buscas?", "Quiero trabajar", "Quiero contratar" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                ops) {
            @Override
            public boolean isEnabled(int pos) {
                return pos != 0;
            }

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
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri selectedUri = result.getData().getData();
                if (tipoCaptura.equals("CEDULA")) {
                    uriCedula = selectedUri;
                    txtNombreCedula.setText("Cédula seleccionada ✅");
                    txtNombreCedula.setTextColor(Color.BLACK);
                } else {
                    uriSelfie = selectedUri;
                    txtNombreSelfie.setText("Selfie seleccionada ✅");
                    txtNombreSelfie.setTextColor(Color.BLACK);
                }
            }
        });

        // Usamos galería en lugar de cámara para obtener Uri del archivo
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");

        if (areaCedula != null) {
            areaCedula.setOnClickListener(v -> {
                tipoCaptura = "CEDULA";
                cameraLauncher.launch(gallery);
            });
        }
        if (areaSelfie != null) {
            areaSelfie.setOnClickListener(v -> {
                tipoCaptura = "SELFIE";
                cameraLauncher.launch(gallery);
            });
        }
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