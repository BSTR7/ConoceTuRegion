package com.example.conoceturegion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.view.View;

import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class Comentario extends AppCompatActivity {
    private ImageView mImageView;
    private RadioButton radioA;
    private RadioButton radioB;
    private RadioButton radioC;

    private EditText mEditText;

    private String Valoracion;
    private String Comentario;


    private String usuario;
    private String nombreMarcador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentario);

        Intent intent = getIntent();
        usuario = intent.getStringExtra("user");
        nombreMarcador = intent.getStringExtra("nombreMarcador");

        mEditText = findViewById(R.id.mEditText); /// Comentario

        radioA = findViewById(R.id.radioA);
        radioB = findViewById(R.id.radioB);
        radioC = findViewById(R.id.radioC);

        mImageView = findViewById(R.id.mImageView);

        TextView textView = findViewById(R.id.txtlugar);
        textView.setText("Comentario para: " + nombreMarcador);

        if (nombreMarcador != null) {
            switch (nombreMarcador) {
                case "Piedra del Batro":
                    mImageView.setImageResource(R.drawable.imagenpiedra);
                    break;
                case "El Peñasco":
                    mImageView.setImageResource(R.drawable.imagenpenasco);
                    break;
                case "Reserva Nacional Altos De Lircay":
                    mImageView.setImageResource(R.drawable.imagenaltos);
                    break;
                case "Balneario Pelluhue":
                    mImageView.setImageResource(R.drawable.imagenpelluhue);
                    break;
                case "Vilches Alto":
                    mImageView.setImageResource(R.drawable.imagenvilches);
                    break;
                case "Termas de Quinamavida":
                    mImageView.setImageResource(R.drawable.imagentermas);
                    break;
                case "Lago Colbun":
                    mImageView.setImageResource(R.drawable.imagenlago);
                    break;
                case "Embalse Colbun":
                    mImageView.setImageResource(R.drawable.imagenembalse);
                    break;
                default:
                    mImageView.setImageResource(R.drawable.default_image);
                    break;
            }
        }

        ImageView btnComentar = findViewById(R.id.btnComentar);
        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarComentarioEnBaseDeDatos();
            }
        });


    }



    private void guardarComentarioEnBaseDeDatos() {
        // Obtener el comentario del EditText
        Comentario = mEditText.getText().toString().trim();
        if (TextUtils.isEmpty(Comentario)) {
            Toast.makeText(this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la valoración seleccionada
        if (radioA.isChecked()) {
            Valoracion = "Aceptable";
        } else if (radioB.isChecked()) {
            Valoracion = "Buena opción";
        } else if (radioC.isChecked()) {
            Valoracion = "Excepcionalmente recomendado";
        }

        // Guardar el comentario en la base de datos
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        long resultado = dbHelper.insertComentario(usuario, Comentario, nombreMarcador, Valoracion);
        Toast.makeText(this, "Comentario guardado exitosamente", Toast.LENGTH_SHORT).show();

    }
}

