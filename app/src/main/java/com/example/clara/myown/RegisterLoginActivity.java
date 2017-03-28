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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
 * File: RegisterLoginActivity.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * This is the activity where users can register or log in for the app. When a user clicks the
 * 'register' button, their info is put into Firebase asynchronously. Once this is done, the user
 * is redirected to the RoutesListAcitivity. When a user clicks the Login-button, their info is
 * retrieved from Firebase (again asynchronously), and if it is correct the user is also redirected
 * to the RoutesListActivity. */

public class RegisterLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private EditText etEmail;
    private EditText etPassword;
    private EditText etUsername;
    private String email;
    private String password;
    private String username;

    // On create: initializes the EditText and Firebase references
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setListener();
    }

    /* is triggered when a user is logged in / registered, or signed out. If the user
     * is signed in, go to RoutesListActivity, if the user is signed out, log this (sign out action to
     * be added in future) */
    public void setListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(getString(R.string.signed_in), user.getUid());
                    goToRoutesListActivity();
                } else {
                    Log.d(getString(R.string.signed_out), getString(R.string.user_signed_out));
                }
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

    /* is triggered when the user clicks the 'Register' button. Get the info from the EditTexts,
     * and if the password requirements are met, create a new user */
    public void register(View view) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        username = etUsername.getText().toString();
        if(checkPassword(password)) {
            createUser();
        }
    }

    // checks whether the password is more than 6 characters
    public boolean checkPassword(String password) {
        if(password.length() < 6) {
            Toast.makeText(RegisterLoginActivity.this, "password has to be at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /* Asynchronously adds user to authenticated users in Firebase, and adds user's info
     * (username, email, password) to Firebase (addUserInfoToFB).
     * When completed, the authStateListener is triggered. */
    public void createUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            makeToast(getString(R.string.auth_failed));
                        } else {
                            makeToast(getString(R.string.created_user) + email);
                            addUserInfoToFB();
                        }
                    }
                });
    }

    /* Creates a User-object with the info of the created user and stores it
     * in the Firebase database below 'users' */
    public void addUserInfoToFB() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = null;
        try {
            userId = mAuth.getCurrentUser().getUid();
        } catch(NullPointerException e) {
            Toast.makeText(RegisterLoginActivity.this, getString(R.string.auth_failed),
                    Toast.LENGTH_SHORT).show();
            goToRegisterLoginActivty();
        }
        if(userId != null) {
            User user = new User(email, username);
            mDatabase.child("users").child(userId).setValue(user);
        }
    }

    /* Log in user with email and password. If email and password form a authenticated user
     * in Firebase, the auth state listener is triggered that the user is logged in */
    public void login(View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            makeToast(getString(R.string.auth_failed));
                        } else {
                            makeToast(getString(R.string.logged_in_user) + email);
                            goToRoutesListActivity();
                        }
                    }
                });
    }

    // create a Toast which displays a given message
    public void makeToast(String msg) {
        Toast.makeText(RegisterLoginActivity.this, msg,
                Toast.LENGTH_SHORT).show();
    }

    public void goToRegisterLoginActivty() {
        Intent intent = new Intent(this,RegisterLoginActivity.class);
        this.startActivity(intent);
    }

    public void goToRoutesListActivity() {
        Intent secondIntent = new Intent(this, RoutesListActivity.class);
        this.startActivity(secondIntent);
    }
}