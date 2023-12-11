package com.example.conoceturegion;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class VerSolicitudes extends AppCompatActivity implements OnMapReadyCallback {

    private Spinner spinnerSolicitudes;
    private Button botonHabilitar, botonDeshabilitar, botonEliminar;
    private GoogleMap mMap;
    private FrameLayout mapFrameLayout;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_soli);

        spinnerSolicitudes = findViewById(R.id.spinnerSolicitudes);
        botonHabilitar = findViewById(R.id.botonHabilitar);
        botonDeshabilitar = findViewById(R.id.botonDeshabilitar);
        botonEliminar = findViewById(R.id.botonEliminar);
        mapFrameLayout = findViewById(R.id.mapFrameLayout);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.mapFrameLayout, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        cargarSolicitudesEnSpinner(); // Llena el spinner con las solicitudes existentes

        spinnerSolicitudes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mostrarMarcador(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No se realiza ninguna acción cuando no hay nada seleccionado en el Spinner
            }
        });

        botonHabilitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                habilitarSolicitud();
            }
        });

        botonDeshabilitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deshabilitarSolicitud();
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarSolicitud();
            }
        });
    }

    private void cargarSolicitudesEnSpinner() {
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        List<ObjetoSolicitud> solicitudesList = dbHelper.obtenerSolicitudesDesdeBD();

        // Utiliza un ArrayAdapter con un diseño personalizado para mostrar solo el nombre
        ArrayAdapter<ObjetoSolicitud> adapter = new ArrayAdapter<ObjetoSolicitud>(this, android.R.layout.simple_spinner_item, solicitudesList) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                // Muestra el nombre de la solicitud en el spinner (cuando está cerrado)
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(android.R.layout.simple_spinner_item, parent, false);
                }

                TextView textView = convertView.findViewById(android.R.id.text1);
                ObjetoSolicitud solicitud = getItem(position);

                if (solicitud != null) {
                    textView.setText(solicitud.getNombresolicitud());
                }

                return convertView;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                // Muestra el nombre de la solicitud en la lista desplegable del spinner
                return getView(position, convertView, parent);
            }
        };

        // Especifica el layout que se usará cuando aparezca la lista de opciones
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplica el adapter al spinner
        spinnerSolicitudes.setAdapter(adapter);
    }

    private void mostrarMarcador(int position) {
        // Implementa la lógica para mostrar el marcador correspondiente a la solicitud seleccionada
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        ObjetoSolicitud solicitudSeleccionada = (ObjetoSolicitud) spinnerSolicitudes.getItemAtPosition(position);

        LatLng latLngSolicitud = solicitudSeleccionada.getLatlngSolicitud();

        // Muestra el marcador en el mapa
        if (selectedMarker != null) {
            selectedMarker.remove();
        }
        selectedMarker = mMap.addMarker(new MarkerOptions().position(latLngSolicitud).title("Ubicación de la solicitud seleccionada"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngSolicitud));
    }
    private void habilitarSolicitud() {
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        ObjetoSolicitud solicitud = (ObjetoSolicitud) spinnerSolicitudes.getSelectedItem();
        dbHelper.actualizarEstadoSolicitud(solicitud.getId(), false);

        // Actualiza el spinner después de habilitar la solicitud
        cargarSolicitudesEnSpinner();

        // Muestra un Toast indicando la acción y el nombre de la solicitud
        mostrarToast("Solicitud habilitada: " + solicitud.getNombresolicitud());
    }

    private void deshabilitarSolicitud() {
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        ObjetoSolicitud solicitud = (ObjetoSolicitud) spinnerSolicitudes.getSelectedItem();
        dbHelper.actualizarEstadoSolicitud(solicitud.getId(), true);

        // Actualiza el spinner después de deshabilitar la solicitud
        cargarSolicitudesEnSpinner();

        // Muestra un Toast indicando la acción y el nombre de la solicitud
        mostrarToast("Solicitud deshabilitada: " + solicitud.getNombresolicitud());
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void eliminarSolicitud() {
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        ObjetoSolicitud solicitud = (ObjetoSolicitud) spinnerSolicitudes.getSelectedItem();
        dbHelper.eliminarSolicitud(solicitud.getId());

        // Actualiza el spinner después de eliminar la solicitud
        cargarSolicitudesEnSpinner();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Agrega cualquier configuración adicional del mapa que necesites
    }
}
