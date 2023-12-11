package com.example.conoceturegion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);





        ImageView btnversol = findViewById(R.id.btnversol);
        btnversol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerSolis();
            }
        });

        ImageView btnuser = findViewById(R.id.btnuser);
        btnuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerUsur();
            }
        });


        ImageView btnlogout = findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void VerSolis() {
        Intent intent = new Intent(this, VerSolicitudes.class);
        startActivity(intent);
    }
    private void VerUsur() {
        Intent intent = new Intent(this, VerUsuario.class);
        startActivity(intent);
    }
}