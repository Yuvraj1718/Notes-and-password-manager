package com.example.notes_tab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class createMasterPass extends AppCompatActivity {
    EditText mp;
    AppCompatButton mpbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_master_pass);
        mp=findViewById(R.id.crpass);
        mpbtn=findViewById(R.id.createmasspassbtn);
        mpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mp.getText().toString();
                if(s.equals("")){
                    Toast.makeText(createMasterPass.this, "Enter password", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences settings  = getSharedPreferences("PREF",0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("password",s);
                    editor.apply();
                    startActivity(new Intent(createMasterPass.this,MainActivity.class));
                    finish();
                }
            }
        });


    }
}