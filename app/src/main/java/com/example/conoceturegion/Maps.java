package com.example.conoceturegion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;

public class Maps extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;

    private String selectedMarkerTitle;
    private String username;
    private TextView textView5;

    // Regiones del Maule
    private LatLng piedraLocation = new LatLng(-36.03666733563102, -71.50668255092104);
    private LatLng penascoLocation = new LatLng(-35.981138782963214, -71.47782030533841);
    private LatLng altosLircayLocation = new LatLng(-35.60356543308457, -70.9769858766285);
    private LatLng balnearioPelluhue = new LatLng(-35.811389, -72.635278);
    private LatLng vilchesAlto = new LatLng(-35.768889, -70.864444);
    private LatLng termasDeQuinamavida = new LatLng(-35.934167, -71.323889);
    private LatLng lagoColbun = new LatLng(-35.732222, -71.409167);
    private LatLng embalseColbun = new LatLng(-35.709722, -71.412778);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // ID usuario test
        textView5 = findViewById(R.id.textView5);
        Intent intent = getIntent();
        username = intent.getStringExtra("user");
        textView5.setText("Usuario: " + username);

        ImageView btncomentario = findViewById(R.id.btncomentario);
        btncomentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedMarkerTitle != null && !selectedMarkerTitle.isEmpty()) {
                    startComentarioActivity();
                } else {
                    Toast.makeText(Maps.this, "Selecciona una ubicacion antes de agregar un comentario", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView btnComents = findViewById(R.id.btnComents);
        btnComents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerCome();
            }
        });

        ImageView btnlog = findViewById(R.id.btnlog);
        btnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logs();
            }
        });

        ImageView btnLocation = findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationUpdates();
                if (currentLocationMarker != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationMarker.getPosition(), 11));
                }
            }
        });
    }

    private void VerCome() {
        Intent intent = new Intent(this, Vercomentarios.class);
        startActivity(intent);
    }

    private void logs() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mauleCenter = new LatLng(-35.4551, -71.6485);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mauleCenter, 9.0f));
        mMap.setOnMarkerClickListener(this);

        // Agregar marcadores de las solicitudes habilitadas
        addSolicitudesMarkers();
        addMarkers();
        // requestLocationUpdates(); // No es necesario llamarlo aquí
    }

    private void addSolicitudesMarkers() {
        DatabaseHelperUsuarios dbHelper = new DatabaseHelperUsuarios(this);
        ArrayList<ObjetoSolicitud> solicitudesHabilitadas = dbHelper.obtenerSolicitudesHabilitadasConNombre();

        for (ObjetoSolicitud solicitud : solicitudesHabilitadas) {
            LatLng latLng = solicitud.getLatlngSolicitud();
            String nombreSolicitud = solicitud.getNombresolicitud();

            mMap.addMarker(new MarkerOptions().position(latLng).title(nombreSolicitud));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        selectedMarkerTitle = marker.getTitle();
        return false;
    }

    private void startComentarioActivity() {
        Intent intent = new Intent(this, Comentario.class);
        intent.putExtra("nombreMarcador", selectedMarkerTitle);
        intent.putExtra("user", username);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 5 seconds
        locationRequest.setFastestInterval(2000); // 2 seconds

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicitar permisos si no están concedidos
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (currentLocationMarker == null) {
                        // Crea un nuevo marcador con el icono del drawable "ubicacion"
                        currentLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.timereal)));
                    } else {
                        currentLocationMarker.setPosition(latLng);
                    }
                    // No movemos automáticamente la cámara cuando se actualiza la ubicación
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions().position(piedraLocation).title("Piedra del Batro").icon(BitmapDescriptorFactory.fromResource(R.drawable.piedra)));
        mMap.addMarker(new MarkerOptions().position(penascoLocation).title("El Peñasco").icon(BitmapDescriptorFactory.fromResource(R.drawable.penasco)));
        mMap.addMarker(new MarkerOptions().position(altosLircayLocation).title("Reserva Nacional Altos De Lircay").icon(BitmapDescriptorFactory.fromResource(R.drawable.altos)));
        mMap.addMarker(new MarkerOptions().position(balnearioPelluhue).title("Balneario Pelluhue").icon(BitmapDescriptorFactory.fromResource(R.drawable.pelluhue)));
        mMap.addMarker(new MarkerOptions().position(vilchesAlto).title("Vilches Alto").icon(BitmapDescriptorFactory.fromResource(R.drawable.vilches)));
        mMap.addMarker(new MarkerOptions().position(termasDeQuinamavida).title("Termas de Quinamavida").icon(BitmapDescriptorFactory.fromResource(R.drawable.termas)));
        mMap.addMarker(new MarkerOptions().position(lagoColbun).title("Lago Colbun").icon(BitmapDescriptorFactory.fromResource(R.drawable.lago)));
        mMap.addMarker(new MarkerOptions().position(embalseColbun).title("Embalse Colbun").icon(BitmapDescriptorFactory.fromResource(R.drawable.embalse)));
    }
}


