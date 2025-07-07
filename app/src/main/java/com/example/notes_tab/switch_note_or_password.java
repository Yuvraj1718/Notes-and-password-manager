package com.example.notes_tab;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notes_tab.model.PasswordData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class switch_note_or_password extends AppCompatActivity {
    MaterialButton open_notes_tab, signOutButton, openPassword;

    ArrayList<ContactNoteRow> l1 = new ArrayList<>();
    ArrayList<PasswordData> l2 = new ArrayList<>();

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_note_or_password);
        Intent choose_tab_to_notes_tab = new Intent(this, MainActivity.class);

        Intent passwordActivity = new Intent(this, PasswordActivity.class);
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        );

//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://notes-and-password-manager-99-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference reference = database.getReference(String.valueOf(mGoogleSignInClient.getApiOptions().getAccount()));
//        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                ArrayList list = task.getResult().getValue(ArrayList.class);
//
//                if (list != null) {
//                    l1 = (ArrayList<ContactNoteRow>) list.get(0);
//                    l2 = (ArrayList<PasswordData>) list.get(1);
//                }
//                try {
//                    ByteArrayOutputStream os = new ByteArrayOutputStream();
//                    ObjectOutputStream o = new ObjectOutputStream(os);
//                    o.writeObject(l1);
//                    choose_tab_to_notes_tab.putExtra("notes", os.toByteArray());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    ByteArrayOutputStream os = new ByteArrayOutputStream();
//                    ObjectOutputStream o = new ObjectOutputStream(os);
//                    o.writeObject(l2);
//                    passwordActivity.putExtra("pass", os.toByteArray());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(view -> ((ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE))
                .clearApplicationUserData());

        open_notes_tab = findViewById(R.id.open_notes_tab_button);
        openPassword = findViewById(R.id.open_password_tab_button);


        open_notes_tab.setOnClickListener(view -> startActivity(choose_tab_to_notes_tab));

        openPassword.setOnClickListener(view -> startActivity(passwordActivity));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}