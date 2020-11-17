package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zybooks.universityconnect.viewmodel.MainActivityViewModel;


import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    FirebaseFirestore firestore;
    ArrayList<DocumentSnapshot> chats;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        viewModel = MainActivityViewModel.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("chat").document("plaza")
                .collection("message").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                list.get(0);
            }
        });
        firestore.collection("chat").get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chats = (ArrayList<DocumentSnapshot>) queryDocumentSnapshots.getDocuments();
                ChatAdapter adapter = new ChatAdapter(ChatActivity.this, chats);

                ListView listView = (ListView) findViewById(R.id.listview_chat);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String chatName = chats.get(i).getId();
                        Intent message = new Intent(ChatActivity.this, MessageActivity.class);
                        startActivity(message);
                    }
                });
            }
        });
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
                viewModel.setSigningOut(true);
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ChatActivity.this,
                                        "You have been signed out.",
                                        Toast.LENGTH_LONG)
                                        .show();
                                setContentView(R.layout.sign_in);
                            }
                        });
                break;
            case R.id.menu_switch_activity:
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
        }
        return super.onOptionsItemSelected(item);
    }
}