package com.zybooks.universityconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zybooks.universityconnect.viewmodel.MainActivityViewModel;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private MainActivityViewModel viewModel;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        if (currentUser == null || !currentUser.isEmailVerified()) {
            setContentView(R.layout.not_verified);
        } else {
            setContentView(R.layout.activity_main);
        }
        viewModel = new MainActivityViewModel();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (shouldStartSignIn()) {
            startSignIn();
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
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean shouldStartSignIn() {
        return currentUser == null && !viewModel.getIsSigningIn();
    }

    private void startSignIn() {
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        viewModel.setSigningIn(true);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && !currentUser.isEmailVerified()) {
            currentUser.sendEmailVerification();
        }
    }
}