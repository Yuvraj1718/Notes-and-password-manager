/*
 * Copyright (c) 2023 Navjot Singh Rakhra. All rights reserved.
 */
package com.example.notes_tab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes_tab.model.PasswordData;
import com.example.notes_tab.model.PasswordDataAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import EncryptionDecryption.Decryption.Decryption;
import EncryptionDecryption.Encryption.Encryption;
import EncryptionDecryption.PasswordGenerator.PasswordGenerator;

public class PasswordActivity extends AppCompatActivity {
    public ExecutorService executorService = Executors.newFixedThreadPool(2);
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

    private static final int DEFAULT_PASSWORD_LENGTH = 12;
    private ArrayList<PasswordData> passwordDataArrayList = new ArrayList<>();
    private PasswordDataAdapter passwordDataAdapter;

    private Animation scaleUp, scaleDown;

    public Animation getScaleUp() {
        return scaleUp;
    }

    public Animation getScaleDown() {
        return scaleDown;
    }

    private void checkEmptyMessage() {
        if (passwordDataArrayList.size() == 0) {
            findViewById(R.id.passwordRecyclerView).setVisibility(View.INVISIBLE);
            findViewById(R.id.emptyMessage).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.emptyMessage).setVisibility(View.INVISIBLE);
            findViewById(R.id.passwordRecyclerView).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwd_activity);

        init(savedInstanceState);

        executorService.execute(this::retrieveDataFromCloudAndUpdateView);
    }

    private void retrieveDataFromCloudAndUpdateView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("AUTH", FirebaseAuth.getInstance().getUid());

        db.collection("users_pass").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("DATA_RETRIEVED", "Document snapshot: " + task.getResult().toObject(PassData.class));
                        if (passwordDataArrayList.isEmpty()) {
                            PassData data = task.getResult().toObject(PassData.class);
                            if (data != null) {
                                try {
                                    passwordDataArrayList.addAll((ArrayList<PasswordData>) new ObjectInputStream(new ByteArrayInputStream(Decryption.decrypt(Base64.decode(data.getData(), Base64.DEFAULT),"NavjotGod".toCharArray()))).readObject());
                                } catch (IOException | ClassNotFoundException |
                                         IllegalBlockSizeException | BadPaddingException e) {
                                    throw new RuntimeException(e);
                                }
                                checkEmptyMessage();
                                passwordDataAdapter.notifyItemRangeInserted(0, passwordDataArrayList.size());
                            }
                        }
                    } else {
                        Log.d("DATA_RETRIEVED", "No such document" + task.getException());
                    }
                })
        ;
    }

    public void updateData() {
        checkEmptyMessage();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String data = null;

        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(b);
            os.writeObject(passwordDataArrayList);
            data = android.util.Base64.encodeToString(Encryption.encrypt(b.toByteArray(),"NavjotGod".toCharArray()), Base64.DEFAULT);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }


        db.collection("users_pass").document(FirebaseAuth.getInstance().getUid())
                .set(new PassData(data))
                .addOnSuccessListener(a -> Log.d("USER_ADDED", "Data successfully written"))
                .addOnFailureListener(e -> Log.w("USER_ADDED", e))
        ;
    }

    private void init(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) initializeRecyclerView();
        else restoreRecyclerView(savedInstanceState);

        scaleUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_down);

        checkEmptyMessage();
        findViewById(R.id.generatePasswordButton).setOnClickListener(this::generateNewPasswordClicked);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("data_list", passwordDataArrayList);
        super.onSaveInstanceState(outState);
    }

    private void restoreRecyclerView(@NotNull Bundle savedInstanceState) {
        //noinspection unchecked
        passwordDataArrayList = (ArrayList<PasswordData>) savedInstanceState.getSerializable("data_list");
        initializeWithCurrentState(findViewById(R.id.passwordRecyclerView));
        passwordDataAdapter.notifyItemRangeInserted(0,passwordDataArrayList.size());
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.passwordRecyclerView);
        initializeWithCurrentState(recyclerView);
        passwordDataAdapter.notifyItemRangeInserted(0, passwordDataArrayList.size());
    }

    private void initializeWithCurrentState(RecyclerView recyclerView) {

        passwordDataAdapter = new PasswordDataAdapter(passwordDataArrayList, this);
        recyclerView.setAdapter(passwordDataAdapter);
        int spanCount = 2;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 1;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), spanCount));
    }

    private void generateNewPasswordClicked(View view) {
        view.startAnimation(getScaleUp());
        view.startAnimation(getScaleDown());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText website = new EditText(this);
        website.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        website.setHint("Website / Username");
        final EditText password = new EditText(this);
        password.setInputType(InputType.TYPE_CLASS_NUMBER);
        password.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        password.setHint("Password length \n(Leave empty = default length)");
        final EditText passwordText = new EditText(this);
        passwordText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        passwordText.setHint("Enter Password \n(Leave empty = Autogenerate)");

        layout.addView(website);
        layout.addView(password);
        layout.addView(passwordText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Generate new password")
                .setView(layout)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if (website.getText().length() == 0) {
                        dialogInterface.cancel();
                    } else {
                        int c = passwordDataArrayList.size();
                        if (passwordText.getText().length()==0){
                        passwordDataArrayList.add(new PasswordData(website.getText().toString(),
                                PasswordGenerator.generatePassword(password.getText().length() == 0
                                        ? DEFAULT_PASSWORD_LENGTH
                                        : Integer.parseInt(password.getText().toString()))));}
                        else {
                            passwordDataArrayList.add(new PasswordData(website.getText().toString(),passwordText.getText().toString().toCharArray()));
                        }
                        findViewById(R.id.emptyMessage).setVisibility(View.INVISIBLE);
                        findViewById(R.id.passwordRecyclerView).setVisibility(View.VISIBLE);
                        passwordDataAdapter.notifyItemInserted(c);
                        updateData();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

}
