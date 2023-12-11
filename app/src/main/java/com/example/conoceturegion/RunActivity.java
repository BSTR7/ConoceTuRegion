package com.example.conoceturegion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RunActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);


        ImageView btnvers = findViewById(R.id.btnvers);
        btnvers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerCome();
            }
        });

        ImageView btnsesion = findViewById(R.id.btnsesion);
        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sesion();
            }
        });

        ImageView btnsoli = findViewById(R.id.btnsoli);
        btnsoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerSoli();
            }
        });
    }

    private void VerCome() {
        Intent intent = new Intent(this, Vercomentarios.class);
        startActivity(intent);
    }
    private void VerSoli() {
        Intent intent = new Intent(this, Solicitud.class);
        startActivity(intent);
    }
    private void Sesion() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}


