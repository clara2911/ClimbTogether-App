package com.example.clara.myown;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
 * File: AddRouteActivvity.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * In this activity the user can input route-info (name, grade, description) and add the route to the
 * list of routes. This list of routes is public for all users of the app. */

public class AddRouteActivity extends AppCompatActivity {
    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private DatabaseReference mDatabase;
    private Route aRoute;

    // On create: sets Firebase references and set the authState listener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        authTest = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setListener();
    }

    /* Sets the authstate listener. This checks whether there is still
     * a user logged in at a given moment, preventing non-logged in users
     * from seeing pages they shouldn't */
    private void setListener() {
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d(getString(R.string.signed_in), user.getUid());
                } else {
                    Log.d(getString(R.string.signed_out), getString(R.string.user_signed_out));
                    goToRegisterLoginActivity();
                }
            }
        };
    }

    /* creates a Route object from the info in the EditTexts and add it to Firebase under 'routes',
     * then go to the showRouteActivity to show the info of this route */
    public void addToDB(View view) {
        getRouteFromEts();
        mDatabase.child("routes").child(aRoute.getName()).setValue(aRoute);
        goToShowRoute();
    }

    // gets the route info from the EditTexts, turn it into the Route object 'aRoute'
    public void getRouteFromEts() {
        EditText et1 = (EditText) findViewById(R.id.editText1);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        EditText et3 = (EditText) findViewById(R.id.editText3);

        String name = et1.getText().toString();
        String grade = et2.getText().toString();
        String description = et3.getText().toString();
        aRoute = new Route(name, grade, description);

        et1.getText().clear();
        et2.getText().clear();
        et3.getText().clear();
    }

    /* goes to ShowRouteActivity and passes the route info (name, grade, description)
     * through the Intent */
    public void goToShowRoute() {
        Intent routeIntent = new Intent(this, ShowRouteActivity.class);
        routeIntent.putExtra(getString(R.string.name), aRoute.getName());
        routeIntent.putExtra(getString(R.string.grade), aRoute.getGrade());
        routeIntent.putExtra(getString(R.string.description), aRoute.getDescription());
        this.startActivity(routeIntent);
    }

    public void goToRegisterLoginActivity() {
        Intent firstIntent = new Intent(this, MainActivity.class);
        this.startActivity(firstIntent);
    }
}