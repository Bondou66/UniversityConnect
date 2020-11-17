package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.zybooks.universityconnect.viewmodel.MainActivityViewModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final double CIRCLE_RADIUS = 45.72;
    private final int STROKE_WIDTH = 2;

    private float zoomLevel;
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = MainActivityViewModel.getInstance();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        zoomLevel = 18;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        updateMap(location);
                    }
                }
            }
        };

        client = LocationServices.getFusedLocationProviderClient(this);
    }

    private void updateMap(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        // Place a marker at the current location
        MarkerOptions myMarker = new MarkerOptions()
                .title("Here you are!")
                .position(myLatLng);

        // Remove previous marker
        mMap.clear();
        drawCircle(new LatLng(33.9268, -117.4223));
        drawCircle(new LatLng(33.9293, -117.4271));
        drawCircle(new LatLng(33.9294, -117.4231));
        drawCircle(new LatLng(33.9317, -117.4249));
        drawCircle(new LatLng(33.9302, -117.4237));

        // Add new marker
        mMap.addMarker(myMarker);

        // Move and zoom to current location at the street level
        CameraUpdate update = CameraUpdateFactory.
                newLatLngZoom(myLatLng, zoomLevel);
        mMap.animateCamera(update);
    }

    @Override
    public void onPause() {
        super.onPause();
        client.removeLocationUpdates(locationCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        if (hasLocationPermission()) {
            client.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private boolean hasLocationPermission() {

        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int REQUEST_LOCATION_PERMISSIONS = 0;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);

            return false;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel.isSigningOut()) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                viewModel.setSigningOut(true);
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MapsActivity.this,
                                        "You have been signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();
                                setContentView(R.layout.sign_in);
                            }
                        });
                break;
            case R.id.menu_switch_activity:
                Intent chat = new Intent(this, ChatActivity.class);
                startActivity(chat);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * s     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                zoomLevel = cameraPosition.zoom;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker){
                Toast.makeText(MapsActivity.this, "Lat: " +
                        marker.getPosition().latitude + "\nLong: " + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void drawCircle(LatLng point){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(CIRCLE_RADIUS);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(STROKE_WIDTH);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }
}