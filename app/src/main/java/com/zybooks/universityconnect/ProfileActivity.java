package com.zybooks.universityconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTxtView, emailTxtView, phoneTxtView;
    private ImageView userImageView, emailImageView, phoneImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTxtView = findViewById(R.id.user_name);
        emailTxtView = findViewById(R.id.email);
        phoneTxtView = findViewById(R.id.phone_number);

    }


}
