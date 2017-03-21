package com.example.clara.myown;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private String email;
    private String password;
    private String username;
    private DatabaseReference mDatabase;
    private static final String TAG = "Firebase_test";
    private boolean signedin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("signed in", "onAuthStateChanged:signed_in:" + user.getUid());
                    signedin = true;
                } else {
                    // User is signed out
                    Log.d("signed out", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    public void goToRoutesListActivity() {
        Intent secondIntent = new Intent(this, RoutesListActivity.class);
        secondIntent.putExtra("username", username);
        this.startActivity(secondIntent);

    }

    public void nextActivity(View view) {
        Log.d("sd","signedin: " + String.valueOf(signedin));
        if(signedin) {
            Intent intent = new Intent(this, RoutesListActivity.class);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this,RegisterLoginActivity.class);
            this.startActivity(intent);
        }

    }

    public void goToAddRoute(View view) {
        Intent intent = new Intent(this,AddRouteActivity.class);
        this.startActivity(intent);
    }
}
