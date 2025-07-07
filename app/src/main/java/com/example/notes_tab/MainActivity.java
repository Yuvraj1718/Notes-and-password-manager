package com.example.notes_tab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static class PassData implements Serializable {
        public String getData() {
            return data;
        }

        public final String data;

        public PassData() {
            data = null;
        }

        public PassData(String data) {
            this.data = data;
        }
    }

    String Title, Content;
    RecyclerView notesRecyclerView;
    LinearLayout ll;
    Intent toNewNote;
    static RecyclerNotesAdapter notesAdapter;
    static ArrayList<ContactNoteRow> notesArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toNewNote = new Intent(MainActivity.this, NewNoteActivity.class);

//        View v =
//        ll=(LinearLayout) findViewById(R.id.notesRowLayout2);
//        ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,showNote.class);
////                intent.putExtra("title",)
//
//            }
//        });
        notesAdapter = new RecyclerNotesAdapter(this, notesArray);

        notesRecyclerView.setAdapter(notesAdapter);

        retrieveAndUpdateUI();


        AppCompatButton dummy = findViewById(R.id.dummy);
        dummy.setOnClickListener(view -> startActivity(toNewNote));


    }

    private void retrieveAndUpdateUI() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("notes_").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (notesArray.isEmpty()) {
                        PassData data = documentSnapshot.toObject(PassData.class);
                        if (data != null) {
                            try {
                                notesArray.addAll((ArrayList<ContactNoteRow>) new ObjectInputStream
                                        (new ByteArrayInputStream
                                                (Base64.decode(data.getData(), Base64.DEFAULT))).
                                        readObject());
                            } catch (ClassNotFoundException | IOException e) {
                                throw new RuntimeException(e);
                            }
                            notesAdapter.notifyItemRangeInserted(0, notesArray.size());
                        }
                    }
                })
        ;
        notesAdapter.notifyDataSetChanged();
    }

    public static void updateData() {
        notesAdapter.notifyDataSetChanged();


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String data = null;

        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(b);
            os.writeObject(notesArray);
            data = android.util.Base64.encodeToString(b.toByteArray(), Base64.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        db.collection("notes_").document(FirebaseAuth.getInstance().getUid())
                .set(new PassData(data))
                .addOnSuccessListener(unused -> Log.d("DATA_ADDED", "Data added"))
        ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1000000){
            notesArray.remove(data.getIntExtra("pos",-1));
            updateData();
        }
    }
}

