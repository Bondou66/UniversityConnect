package com.zybooks.universityconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zybooks.universityconnect.R;

public class MessageActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        displayChatMessages();

        firestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                // TODO : Needs to use firestore rather than Firebase database
//                FirebaseDatabase.getInstance()
//                        .getReference()
//                        .push()
//                        .setValue(new ChatMessage(input.getText().toString(),
//                                FirebaseAuth.getInstance()
//                                        .getCurrentUser()
//                                        .getDisplayName())
//                        );

                // Clear the input
                input.setText("");
            }
        });
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView) findViewById(R.id.list_of_messages);

//        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
//                R.layout.message, firestore) {
//            @Override
//            protected void populateView(View v, ChatMessage model, int position) {
//                TextView messageText = (TextView)v.findViewById(R.id.message_text);
//                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
//                TextView messageTime = (TextView)v.findViewById(R.id.message_time);
//
//                // Set their text
//                messageText.setText(model.getMessageText());
//                messageUser.setText(model.getMessageUser());
//
//                // Format the date before showing it
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
//                        model.getMessageTime()));
//            }
//        };

        listOfMessages.setAdapter(adapter);
    }
}