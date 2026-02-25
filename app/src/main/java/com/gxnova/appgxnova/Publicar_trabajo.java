package com.gxnova.appgxnova;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gxnova.appgxnova.models.Categoria;
import com.gxnova.appgxnova.network.ApiService;
import com.gxnova.appgxnova.network.NetworkClient;
import com.gxnova.appgxnova.network.SessionManager;
import com.gxnova.appgxnova.models.Trabajo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Publicar_trabajo extends AppCompatActivity {

    private Spinner spinnerCategorias;
    private EditText editTitulo, editDescripcion, editPresupuesto, editModalidad;
    private Button btnPublicar;

    private ApiService apiService;
    private SessionManager sessionManager;
    private List<Categoria> listaCategorias = new ArrayList<>();
    private ArrayAdapter<Categoria> adapterCategorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar_trabajo);

        apiService = NetworkClient.getApiService();
        sessionManager = new SessionManager(this);

        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        editTitulo = findViewById(R.id.editTitulo);
        editDescripcion = findViewById(R.id.editDescripcion);
        editPresupuesto = findViewById(R.id.editPresupuesto);
        editModalidad = findViewById(R.id.editModalidad);
        btnPublicar = findViewById(R.id.btnPublicarServicioFinal);

        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null)
            btnBack.setOnClickListener(v -> finish());

        configurarSpinnerVacio();
        cargarCategorias();

        if (btnPublicar != null)
            btnPublicar.setOnClickListener(v -> publicarTrabajo());
        setupNavegacion();
    }

    private void configurarSpinnerVacio() {
        adapterCategorias = new ArrayAdapter<Categoria>(this, android.R.layout.simple_spinner_dropdown_item,
                listaCategorias) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };
        if (spinnerCategorias != null)
            spinnerCategorias.setAdapter(adapterCategorias);
    }

    private void cargarCategorias() {
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCategorias.clear();
                    // Agregar placeholder
                    Categoria placeholder = new Categoria();
                    placeholder.nombre = "Selecciona una categoría";
                    listaCategorias.add(placeholder);
                    listaCategorias.addAll(response.body());
                    adapterCategorias.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable t) {
                Toast.makeText(Publicar_trabajo.this, "No se cargaron categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publicarTrabajo() {
        if (spinnerCategorias.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Selecciona una categoría válida", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = editTitulo != null ? editTitulo.getText().toString().trim() : "";
        String descripcion = editDescripcion != null ? editDescripcion.getText().toString().trim() : "";
        String presupuesto = editPresupuesto != null ? editPresupuesto.getText().toString().trim() : "0";
        String modalidad = editModalidad != null ? editModalidad.getText().toString().trim() : "Remoto";
        Categoria cat = (Categoria) spinnerCategorias.getSelectedItem();

        if (titulo.isEmpty()) {
            Toast.makeText(this, "El título es requerido", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.getBearerToken();
        if (token == null) {
            Toast.makeText(this, "Inicia sesión para publicar", Toast.LENGTH_SHORT).show();
            return;
        }

        btnPublicar.setEnabled(false);
        btnPublicar.setText("Publicando...");

        RequestBody rbTitulo = toBody(titulo);
        RequestBody rbDesc = toBody(descripcion);
        RequestBody rbPresup = toBody(presupuesto);
        RequestBody rbMod = toBody(modalidad.isEmpty() ? "Remoto" : modalidad);
        RequestBody rbCat = toBody(String.valueOf(cat.id));

        // Foto vacía como placeholder
        RequestBody emptyPhoto = RequestBody.create(MediaType.parse("image/jpeg"), new byte[0]);
        MultipartBody.Part partFoto = MultipartBody.Part.createFormData("foto", "foto.jpg", emptyPhoto);

        apiService.crearTrabajo(token, rbTitulo, rbDesc, rbPresup, rbMod, rbCat, partFoto)
                .enqueue(new Callback<Trabajo>() {
                    @Override
                    public void onResponse(Call<Trabajo> call, Response<Trabajo> response) {
                        btnPublicar.setEnabled(true);
                        btnPublicar.setText("Publicar");
                        if (response.isSuccessful()) {
                            Toast.makeText(Publicar_trabajo.this, "¡Trabajo publicado correctamente!",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(Publicar_trabajo.this, "Error al publicar. Verifica tu rol.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Trabajo> call, Throwable t) {
                        btnPublicar.setEnabled(true);
                        btnPublicar.setText("Publicar");
                        Toast.makeText(Publicar_trabajo.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    private RequestBody toBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private void setupNavegacion() {
        LinearLayout navInicio = findViewById(R.id.nav_inicio);
        LinearLayout navBuscar = findViewById(R.id.nav_buscar);
        LinearLayout navPerfil = findViewById(R.id.nav_perfil);

        if (navInicio != null)
            navInicio.setOnClickListener(
                    v -> startActivity(new Intent(this, Inicio.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        if (navBuscar != null)
            navBuscar.setOnClickListener(v -> startActivity(new Intent(this, Buscar_trabajo.class)));
        if (navPerfil != null)
            navPerfil.setOnClickListener(v -> startActivity(new Intent(this, Perfil.class)));
    }
}