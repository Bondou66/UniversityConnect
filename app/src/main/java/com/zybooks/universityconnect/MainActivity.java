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
import com.zybooks.universityconnect.viewmodel.MainActivityViewModel;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private MainActivityViewModel viewModel;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        viewModel = new MainActivityViewModel();
        firestore = FirebaseFirestore.getInstance();
        if (shouldStartSignIn()) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    RC_SIGN_IN
            );
            new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                }
            }.onAuthStateChanged(FirebaseAuth.getInstance());
            viewModel.setSigningIn(true);
        } else {
            Toast.makeText(this,
                    "Welcome " + currentUser.getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent maps = new Intent(this, MapsActivity.class);
            startActivity(maps);
        } else {
            setContentView(R.layout.not_verified);
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
                signOut();
                break;
            case R.id.menu_switch_activity:

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean shouldStartSignIn() {
        return currentUser == null && !viewModel.getIsSigningIn();
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

                    Toast.makeText(MainActivity.this,
                            "Verification email sent to " + currentUser.getEmail() +
                                    ".  Please reload the application upon verification",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please use a .edu account",
                    Toast.LENGTH_LONG).show();
            TimeUnit.SECONDS.sleep(5);
            signOut();
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,
                                "You have been signed out.",
                                Toast.LENGTH_LONG)
                                .show();
                        finish();
                    }
                });
    }
}