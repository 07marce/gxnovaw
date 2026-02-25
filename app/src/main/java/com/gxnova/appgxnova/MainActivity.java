package com.gxnova.appgxnova;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progressBarSplash);

        // Tiempo total 3000ms (3 seg), se actualiza cada 30ms para suavidad
        new CountDownTimer(3000, 30) {

            public void onTick(long millisUntilFinished) {
                // Calculamos el progreso basado en el tiempo transcurrido
                int progress = (int) ((3000 - millisUntilFinished) / 30);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                progressBar.setProgress(100);
                // Cuando termina, pasa a la siguiente pantalla
                Intent intent = new Intent(MainActivity.this, Auth.class);
                startActivity(intent);

                // Transición suave (opcional)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();
            }
        }.start();
    }
}