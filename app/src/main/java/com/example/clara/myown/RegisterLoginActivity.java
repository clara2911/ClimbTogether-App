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

public class RegisterLoginActivity extends AppCompatActivity {
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
    private boolean signedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        Log.d("sd","in registerloginactivity");

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
                    Log.d("signed in", "USER IS NOT NULL" + user.getUid());
                    Log.d("sd","going to routeslist");
                    goToRoutesListActivity();

                } else {
                    // User is signed out
                    Log.d("signed out", "USER IS NULL");
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

    public void register(View view) {
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        username = etUsername.getText().toString();
        if(checkPassword(password)) {
            Log.d("sd","password is okay");
            createUser();
        }

    }

    public boolean checkPassword(String password) {
        if(password.length() < 6) {
            Toast.makeText(RegisterLoginActivity.this, "password has to be at least 6 characters",
                    Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public void createUser() {
        Log.d("sd", "creating user");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d("created user", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterLoginActivity.this, "created user:"+email,
                                    Toast.LENGTH_SHORT).show();
                            addUserToDB();
                        }

                        // ...
                    }
                });
    }

    public void addUserToDB() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        // add an object to the database
        User user = new User(email, username);
        // possible to overwrite routes now!
        // make unique usernames
        mDatabase.child("users").child(userId).setValue(user);
    }



    public void login(View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("sign in", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("email", "signInWithEmail", task.getException());
                            Toast.makeText(RegisterLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterLoginActivity.this, "logged in user: " + email,
                                    Toast.LENGTH_SHORT).show();
                            goToRoutesListActivity();
                        }


                    }
                });
    }

    public void goToRoutesListActivity() {
        Intent secondIntent = new Intent(this, RoutesListActivity.class);
        secondIntent.putExtra("username", username);
        this.startActivity(secondIntent);

    }
}
