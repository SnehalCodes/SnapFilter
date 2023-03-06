package com.example.newproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we are configuring our window to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        // we are calling handler to run a task for specific time interval
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // creating a new intent
                Intent i = new Intent(splash_activity.this, LoginActivity.class);
                // starting a new activity.
                startActivity(i);
                // finishing our current activity.
                finish();
            }
        }, 4000);
    }
}