package com.example.conoceturegion;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button btnlogin;
    private Button btnregister;
    private EditText txtUsuario;
    private EditText txtPassword;
    private Switch switchPassword;

    private String loginusernames;

    private DatabaseHelperUsuarios dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelperUsuarios(this);


        btnlogin = findViewById(R.id.btnlogin);
        btnregister = findViewById(R.id.btnregister);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword = findViewById(R.id.txtPassword);

        switchPassword = findViewById(R.id.switchPassword);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irARegisterActivity();
            }
        });

        switchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MostrarPassword(isChecked);
            }
        });

    }


    ///Metodo para hacer funcional el switch password
    private void MostrarPassword(boolean showPass) {
        if (showPass) {
            // Si el switch está activado, mostrar la contraseña
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            // Si el switch está desactivado, ocultar la contraseña
            txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
    private void loginUser() {
        String username = txtUsuario.getText().toString();
        String password = txtPassword.getText().toString();
        // Checker usuario y contraseña
        boolean userExists = dbHelper.checkUser(username, password);

        if (userExists) {
            // Login usuario
            loginusernames = username;
            irAMapsActivity(loginusernames);

        } else if(username.equals("admin") && password.equals("admin")) {
            irAdmin();
        } else {
            // Error Login
            Toast.makeText(this, "Error de inicio de sesión. Por favor, verifique su nombre de usuario y contraseña.", Toast.LENGTH_SHORT).show();
        }
    }

    /// metodo si login es correcto
    private void irAMapsActivity(String loginusername) {
        Intent intent = new Intent(this, Maps.class);
        intent.putExtra("user", loginusername);
        startActivity(intent);
    }

    private void irAdmin() {
        Intent intent = new Intent(this, Admin.class);
        startActivity(intent);
    }

    /// metodov ir Register
    private void irARegisterActivity() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }



}