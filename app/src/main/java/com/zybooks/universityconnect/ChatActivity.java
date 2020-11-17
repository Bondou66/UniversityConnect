package com.zybooks.universityconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class ChatActivity extends AppCompatActivity {


    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firestore = FirebaseFirestore.getInstance();
        List<DocumentSnapshot> chats = firestore.collection("chat").orderBy("Ascending").get().getResult().getDocuments();
        //TODO: This line of code returns a list of all chats, display this, and associate chats with distance

    }
}