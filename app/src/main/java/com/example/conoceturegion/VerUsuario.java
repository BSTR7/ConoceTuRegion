package com.example.conoceturegion;

 import android.os.Bundle;
import android.view.View;
import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VerUsuario extends AppCompatActivity {

    private RecyclerView recyclerUsuario;
    private UsuarioAdapter usuarioAdapter;
    private TextView textViewUsuarios, textViewUserID;
    private DatabaseHelperUsuarios dbHelper;
    private ImageView btnEliminar, imageView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuarios);

        recyclerUsuario = findViewById(R.id.recyclerUsuario);
        textViewUsuarios = findViewById(R.id.textViewUsuarios);
        textViewUserID = findViewById(R.id.textViewUserID);

        btnEliminar = findViewById(R.id.btnEliminar);

        imageView9 = findViewById(R.id.imageView9);




        if (recyclerUsuario.getVisibility() == View.INVISIBLE) {
            imageView9.setImageResource(R.drawable.mostrar);
        }



        dbHelper = new DatabaseHelperUsuarios(this);

        // Obtén la lista de usuarios y configura el adaptador
        ArrayList<String> usuariosList = dbHelper.verUsuarios();
        usuarioAdapter = new UsuarioAdapter(usuariosList, usuario -> {
            // Acciones a realizar cuando se hace clic en un usuario
            textViewUserID.setText(usuario);
            String mensajeuusuario = ("Usuario Seleccionado: " + usuario);
            mostrarToast(mensajeuusuario);
            actualizarTextViewUsuario(usuario);
        });

        // Configura el RecyclerView con el adaptador
        recyclerUsuario.setLayoutManager(new LinearLayoutManager(this));
        recyclerUsuario.setAdapter(usuarioAdapter);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioAEliminar = textViewUserID.getText().toString();
                if (!usuarioAEliminar.isEmpty()) {
                    int rowsAffected = dbHelper.eliminarUsuarioPorNombre(usuarioAEliminar);
                    if (rowsAffected > 0) {
                        mostrarToast("Usuario eliminado correctamente.");
                        // Obtén la nueva lista de usuarios después de la eliminación
                        ArrayList<String> nuevaListaUsuarios = dbHelper.verUsuarios();
                        // Actualiza el adaptador con la nueva lista
                        usuarioAdapter.actualizarLista(nuevaListaUsuarios);
                        // Limpia el TextView
                        actualizarTextViewUsuario("");
                    } else {
                        mostrarToast("Error al eliminar el usuario.");
                    }
                } else {
                    mostrarToast("Seleccione un usuario antes de eliminar.");
                }
            }
        });

        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrar();
            }
        });


    }


    public void mostrar(){
        if (recyclerUsuario.getVisibility() == View.INVISIBLE) {
            imageView9.setImageResource(R.drawable.ocultar);
            recyclerUsuario.setVisibility(View.VISIBLE);
            textViewUsuarios.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
        }else{
            imageView9.setImageResource(R.drawable.mostrar);
            recyclerUsuario.setVisibility(View.INVISIBLE);
            textViewUsuarios.setVisibility(View.INVISIBLE);
            btnEliminar.setVisibility(View.INVISIBLE);
        }

    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void actualizarTextViewUsuario(String usuario) {
        // Actualiza el TextView con el nombre de usuario seleccionado
        textViewUsuarios.setText("Usuario Seleccionado: "+usuario);
    }

}


/// desabhilitados son los true
//// habilitados son los false