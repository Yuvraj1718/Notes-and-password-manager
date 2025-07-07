package com.example.notes_tab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class showNote extends AppCompatActivity {
        TextView title,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);
        title = findViewById(R.id.titleview);
        content = findViewById(R.id.contentview);
        title.setText(getIntent().getStringExtra("title"));
        content.setText(getIntent().getStringExtra("content"));

    }
}