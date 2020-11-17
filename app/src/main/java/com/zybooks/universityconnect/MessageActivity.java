package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zybooks.universityconnect.viewmodel.MainActivityViewModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        displayChatMessages();
        viewModel = MainActivityViewModel.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                Map<String, Object> message = new HashMap<>();
                message.put("Text", input.getText().toString());
                message.put("Username", currentUser.getDisplayName());
                message.put("Uid", currentUser.getUid());
                message.put("TimeSent", new Date().getTime());
                firestore.collection("chat").document("plaza")
                        .collection("message").add(message);
                input.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel.isSigningOut()) {
            finish();
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
                viewModel.setSigningOut(true);
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MessageActivity.this,
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

    private void displayChatMessages() {
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);
        try {
            CollectionReference messagesRef = firestore.collection("chat")
                    .document("plaza").collection("message");
            Query query = messagesRef.orderBy("TimeSent").limit(200);
            Task<QuerySnapshot> snapshotTask = query.get();
            List<Message> list = snapshotTask.getResult().toObjects(Message.class);
        } catch (NullPointerException ignored) {

        }
    }
}