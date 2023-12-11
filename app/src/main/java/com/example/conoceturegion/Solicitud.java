package com.example.conoceturegion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Solicitud extends FragmentActivity implements OnMapReadyCallback {

    private EditText editTextNombre;
    private EditText editTextEmail;
    private TextView textViewLatitud;
    private TextView textViewLongitud;
    private Button botonCrearSolicitud;

    private GoogleMap mMap;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);

        // Inicializar los elementos del diseño
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        textViewLatitud = findViewById(R.id.textViewLatitud);
        textViewLongitud = findViewById(R.id.textViewLongitud);
        botonCrearSolicitud = findViewById(R.id.botonCrearSolicitud);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        // Configurar un clic en el botón para manejar la creación de la solicitud
        botonCrearSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos ingresados por el usuario
                String nombre = editTextNombre.getText().toString();
                String email = editTextEmail.getText().toString();
                String latitud = textViewLatitud.getText().toString();
                String longitud = textViewLongitud.getText().toString();

                // Verificar que se hayan ingresado todos los datos
                if (nombre.isEmpty() || email.isEmpty() || latitud.isEmpty() || longitud.isEmpty()) {
                    Toast.makeText(Solicitud.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear un objeto de solicitud con los datos
                ObjetoSolicitud objetoSolicitud = new ObjetoSolicitud();
                objetoSolicitud.setNombresolicitud(nombre);
                objetoSolicitud.setEmail(email);
                objetoSolicitud.setEstado(true);

                // Convertir latitud y longitud a LatLng y establecer en el objeto de solicitud
                LatLng latLngSolicitud = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                objetoSolicitud.setLatlngSolicitud(latLngSolicitud);

                // Agregar el objeto de solicitud a la base de datos
                DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(Solicitud.this);
                long resultado = dbHelper.insertSolicitud(objetoSolicitud);

                if (resultado != -1) {
                    Toast.makeText(Solicitud.this, "Solicitud creada exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarFormularios();
                } else {
                    Toast.makeText(Solicitud.this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Mover la cámara a la región del Maule
        LatLng maule = new LatLng(-35.6532, -71.6922);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maule, 8.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                textViewLatitud.setText(String.valueOf(latLng.latitude));
                textViewLongitud.setText(String.valueOf(latLng.longitude));
                if (marker != null) {
                    marker.remove();
                }
                marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    private void limpiarFormularios() {
        // Limpiar los campos de texto
        editTextNombre.getText().clear();
        editTextEmail.getText().clear();
        textViewLatitud.setText("Latitud: ");
        textViewLongitud.setText("Longitud: ");
        // Quitar el marcador del mapa
        if (marker != null) {
            marker.remove();
            marker = null;
        }
        // Mover la cámara a la región del Maule
        LatLng maule = new LatLng(-35.6532, -71.6922);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(maule, 8.0f));
    }

}
