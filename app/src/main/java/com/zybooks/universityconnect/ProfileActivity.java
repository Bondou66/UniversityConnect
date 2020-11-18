package com.zybooks.universityconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.zybooks.universityconnect.MessageActivity.EXTRA_SIGNING_OUT;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                Intent signIn = new Intent(this, SignInActivity.class);
                signIn.putExtra(EXTRA_SIGNING_OUT, true);
                startActivity(signIn);
                return true;

            case R.id.menu_chats:
                Intent chat = new Intent(this, ChatActivity.class);
                startActivity(chat);
                return true;

            case R.id.menu_map:
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
                return true;

            case R.id.menu_profile:
                Intent profile = new Intent(this, ProfileActivity.class);
                startActivity(profile);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
