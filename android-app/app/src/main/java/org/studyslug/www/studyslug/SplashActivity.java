package org.studyslug.www.studyslug;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {

        super.onStart();
        /* TODO: This is where the firebase auth stuff goes
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            // go to main page
            Toast.makeText(SplashActivity.this, "Logged in, going to main page", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            // go to login page
            Toast.makeText(SplashActivity.this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
        */
        Button startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });
    }

    public void openLogin() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);

    }

}
