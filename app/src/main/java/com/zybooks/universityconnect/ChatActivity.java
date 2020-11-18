package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.EXTRA_MESSAGE";
    public static final String EXTRA_SIGNING_OUT = "com.example.EXTRA_SIGNING_OUT";

    private FirebaseFirestore firestore;
    private ArrayList<DocumentSnapshot> chats;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient client;
    private ArrayList<DocumentSnapshot> permanentChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firestore = FirebaseFirestore.getInstance();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(8000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        firestore.collection("chat").get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                displayMessages(queryDocumentSnapshots);
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLocations().get(0);
                    if (chats != null) {
                        for (int i = permanentChats.size() - 1; i >= 0; i--) {
                            Location chatLocation = new Location("");
                            chatLocation.setLatitude((double) permanentChats.get(i).get("latitude"));
                            chatLocation.setLongitude((double) permanentChats.get(i).get("longitude"));
                            if (location.distanceTo(chatLocation) > 45.72) {
                                chats.remove(permanentChats.get(i));
                            }
                            if (location.distanceTo(chatLocation) < 45.72 &&
                                    !chats.contains(permanentChats.get(i))) {
                                chats.add(permanentChats.get(i));
                            }
                        }
                    }
                }
                displayMessages();
            }
        };

        client = LocationServices.getFusedLocationProviderClient(this);
    }

    private void displayMessages(QuerySnapshot queryDocumentSnapshots) {
        chats = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
        permanentChats = new ArrayList<>(chats);
        displayMessages();
    }

    private void displayMessages(){
        if (chats != null) {
            ChatAdapter adapter = new ChatAdapter(ChatActivity.this, chats);

            ListView listView = (ListView) findViewById(R.id.listview_chat);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String chatName = chats.get(i).getId();

                    Intent message = new Intent(ChatActivity.this,
                            MessageActivity.class);
                    message.putExtra(EXTRA_MESSAGE, chatName);
                    startActivity(message);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                Intent signIn = new Intent(this, SignInActivity.class);
                signIn.putExtra(EXTRA_SIGNING_OUT, true);
                startActivity(signIn);
                break;
            case R.id.menu_switch_activity:
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
        }
        return super.onOptionsItemSelected(item);
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
}