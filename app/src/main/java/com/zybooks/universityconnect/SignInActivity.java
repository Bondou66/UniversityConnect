package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean isSigningIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        boolean signedOut = false;
        if (extras != null) {
            signedOut = extras.getBoolean("com.example.EXTRA_SIGNING_OUT");
            //The key argument here must match that used in the other activity
        }
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null && currentUser.isEmailVerified()) {
                    Intent chat = new Intent
                            (SignInActivity.this,
                                    ChatActivity.class);
                    startActivity(chat);
                } else {
                    setContentView(R.layout.not_verified);
                }
            }
        };
        if (signedOut) {
            signOut();
        }
        authStateListener.onAuthStateChanged(FirebaseAuth.getInstance());
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        if (shouldStartSignIn()) {
            signIn();
            isSigningIn = true;
        } else if (!signedOut) {
            Toast.makeText(this,
                    "Welcome " + currentUser.getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent chat = new Intent(this, ChatActivity.class);
            startActivity(chat);
        }
        setContentView(R.layout.not_verified);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent chat = new Intent
                    (SignInActivity.this,
                            ChatActivity.class);
            startActivity(chat);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_sign_out:
//                signOut();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private boolean shouldStartSignIn() {
        return currentUser == null && !isSigningIn;
    }

    public void verifyEmail(View view) throws InterruptedException {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();
        if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.+-]+\\.edu$")) {
            currentUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    // Re-enable button
                    findViewById(R.id.verify_email_button).setEnabled(true);

                    Toast.makeText(SignInActivity.this,
                            "Verification email sent to " + currentUser.getEmail() +
                                    ".  Please reload the application upon verification",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Map<String, Object> user = new HashMap<>();
            user.put("Uid", currentUser.getUid());
            user.put("Username", currentUser.getDisplayName());
            user.put("Email", currentUser.getEmail());
            firestore.collection("user").document(user.get("Uid").toString()).set(user);


        } else {
            Toast.makeText(SignInActivity.this, "Please use a .edu account",
                    Toast.LENGTH_LONG).show();
            TimeUnit.SECONDS.sleep(5);
            signOut();
        }
    }

    public void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignInActivity.this,
                                "You have been signed out.",
                                Toast.LENGTH_LONG)
                                .show();
                        setContentView(R.layout.sign_in);
                    }
                });
    }

    public void signIn(View view){
        signIn();
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .build(),
                RC_SIGN_IN
        );
        authStateListener.onAuthStateChanged(FirebaseAuth.getInstance());
    }
}