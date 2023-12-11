package com.example.conoceturegion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class Register extends AppCompatActivity {

    ArrayList<String> regiones = new ArrayList<>();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private CheckBox termsCheckBox;
    private Button btnreg;
    private Spinner spinner;
    private TextView textModif;

    private DatabaseHelperUsuarios dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelperUsuarios(this);


        // Obtener referencias a los elementos de la interfaz de usuario
        usernameEditText = findViewById(R.id.txtreguser);
        nameEditText = findViewById(R.id.txtregnom);
        lastNameEditText = findViewById(R.id.txtregapp);
        emailEditText = findViewById(R.id.txtregemail);
        passwordEditText = findViewById(R.id.txtregpasswd);
        termsCheckBox = findViewById(R.id.checkterms);
        btnreg = findViewById(R.id.btnreg);
        spinner = findViewById(R.id.miSpinner);
        textModif = findViewById(R.id.textmodif);


        //Modificaciones en textview

        String texto = "Si ya tiene una cuenta, de click aqui";

        SpannableString content = new SpannableString(texto);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textModif.setText(content);


        //Regiones array
        regiones.add("Region del Maule");
        regiones.add("Region de O'Higgins");
        regiones.add("Region de Biobío");

        //Adapter de spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regiones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        textModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void registrarUsuario() {
        String username = usernameEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String selectedRegion = spinner.getSelectedItem().toString();
        boolean termsAccepted = termsCheckBox.isChecked();

        // Validar que se hayan ingresado todos los campos
        if (username.isEmpty() || name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || !termsAccepted) {
            Toast.makeText(this, "Todos los campos deben ser completados", Toast.LENGTH_SHORT).show();
        } else {
            // Insertar usuario en la base de datos
            long result = insertUsuario(username, name, lastName, email, password, selectedRegion);

            if (result != -1) {
                String successMessage = "Registro exitoso. ¡Bienvenido, " + name + "!";
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
                limpiarCampos();
                login();
            } else {
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private long insertUsuario(String usuario, String nombre, String apellido, String correo, String contrasena, String region) {
        return dbHelper.insertUsuario(usuario, nombre, apellido, correo, contrasena, region);
    }

    private void limpiarCampos() {
        usernameEditText.setText("");
        nameEditText.setText("");
        lastNameEditText.setText("");
        emailEditText.setText("");
        passwordEditText.setText("");
        spinner.setSelection(0);
        termsCheckBox.setChecked(false);
    }



}