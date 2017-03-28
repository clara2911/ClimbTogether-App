package com.example.clara.myown;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * File: MainActivity.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * This is the welcome screen of the app. When the user clicks the 'Continue'-button, it is checked
 * whether the user is already logged in from a previous time, if this is the case, the user is
 * redirected to the RoutesListActivity, if not, he/she is redirected to the registerLoginActivity. */

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean signedIn = false;

    // On create: sets the authState listener reference and sets the authState listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        setListener();
    }

    /* Sets the authstate listener. This checks whether there is still
     * a user logged in at a given moment, preventing non-logged in users
     * from seeing pages they shouldn't */
    public void setListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // The user was already signed in
                    Log.d(getString(R.string.signed_in), user.getUid());
                    signedIn = true;
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

    /* Called when 'continue'-button is clicked. If user is signed in: redirect to
     * RoutesListActivity, if not: redirect to RegisterLoginActivity */
    public void nextActivity(View view) {
        if(signedIn) {
            Intent intent = new Intent(this, RoutesListActivity.class);
            this.startActivity(intent);
        } else {
            Intent intent = new Intent(this,RegisterLoginActivity.class);
            this.startActivity(intent);
        }
    }
}