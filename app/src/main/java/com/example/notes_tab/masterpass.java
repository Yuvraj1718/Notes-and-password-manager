package com.example.notes_tab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class masterpass extends AppCompatActivity {
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterpass);

        SharedPreferences settings = getSharedPreferences("PREF",0);
        password = settings.getString("password","");
        Handler handler = new Handler();
//        handler.postDelayed()
        if(password.equals("")){
            Intent intent = new Intent(getApplicationContext(),createMasterPass.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(),enterMasterPass.class);
            startActivity(intent);
            finish();
        }
    }
}