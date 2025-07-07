package com.example.notes_tab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {
    AppCompatButton saveNoteButton;
    CardView cardViewNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        EditText e1 = findViewById(R.id.EnterNoteTitle);
        EditText e2 = findViewById(R.id.EnterNoteContent);
        if (getIntent().getStringExtra("title") != null) {
            e1.setText(getIntent().getStringExtra("title"));
            e2.setText(getIntent().getStringExtra("content"));
        }
        cardViewNotes = findViewById(R.id.cardViewNotes);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        saveNoteButton.setOnClickListener(view -> {
            if (Objects.equals(e1.getText().toString(), "") || Objects.equals(e2.getText().toString(), "")) {
                Toast.makeText(NewNoteActivity.this, "Enter both", Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.notesArray.add(new ContactNoteRow(e1.getText().toString(), e2.getText().toString()));
                saveNoteButton.setEnabled(false);
                MainActivity.updateData();
                setResult(1000000, getIntent());
                finish();
            }

        });


    }
}
