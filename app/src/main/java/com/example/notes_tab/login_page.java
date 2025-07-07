package com.example.notes_tab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class login_page extends AppCompatActivity {
//
    VideoView videoViewLoginPage;
    AppCompatButton googleLogInButton;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
         googleLogInButton = findViewById(R.id.googleLogInButton);

        Intent login_to_notes = new Intent(login_page.this,switch_note_or_password.class);
        getSupportActionBar().hide();
        googleLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(login_to_notes);
                finish();
            }
        });
        videoViewLoginPage = findViewById(R.id.videoViewLoginPage);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login_video_bg);
        videoViewLoginPage.setVideoURI(uri);
        videoViewLoginPage.start();
        videoViewLoginPage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("935903966530-2gph48sl3nn3p01og1k8s5h886uh4f8q.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);

        googleLogInButton.setOnClickListener(this::googleSigning);


    }

    private void googleSigning(View view) {
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(a->{
                    if (a.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id",user.getUid());
                        map.put("name",user.getDisplayName());
                        map.put("profile",user.getPhotoUrl().toString());

                        firebaseDatabase.getReference().child("user").child(user.getUid()).setValue(map);
                        System.out.println("Successful");
                        Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onPostResume() {
        videoViewLoginPage.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        videoViewLoginPage.start();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        videoViewLoginPage.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoViewLoginPage.stopPlayback();
        super.onDestroy();
    }
}