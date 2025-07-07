package com.example.notes_tab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent splash_to_login = new Intent(splash_screen.this,login_screen_2.class);
//        Intent splash_to_login = new Intent(splash_screen.this,login_page.class);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(splash_to_login);
                finish();
            }
        },2000);
    }
}