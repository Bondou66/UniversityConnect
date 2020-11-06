package com.zybooks.universityconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


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
        if(shouldStartSignIn()) {
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
                    firestore.collection("users")
                            .document(currentUser.getUid())
                            .set(currentUser);
                }
            };
            viewModel.setSigningIn(true);
        } else {
            Toast.makeText(this,
                    "Welcome " + currentUser.getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }
        if (currentUser.isEmailVerified()) {
            setContentView(R.layout.activity_maps);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean shouldStartSignIn() {
        return currentUser == null && !viewModel.getIsSigningIn();
    }

    public void verifyEmail(View view) {
        currentUser.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                // Re-enable button
                findViewById(R.id.verify_email_button).setEnabled(true);

                Toast.makeText(MainActivity.this,
                        "Verification email sent to " + currentUser.getEmail(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}