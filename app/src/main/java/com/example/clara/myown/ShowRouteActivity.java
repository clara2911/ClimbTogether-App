package com.example.clara.myown;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/*
 * File: RoutesListActivity.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * In this activity the info of a single route is displayed (its name, grade and description). */

public class ShowRouteActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListenerTest;
    private FirebaseUser user;
    private Route aRoute;

    /* Gets the route's info from the Intent, puts this info in the TextViews,
     * and sets the authState listener. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        getRouteInfo();
        setTextViews();
        setListener();
    }

    // gets route info from intent and put it into the Route object
    public void getRouteInfo() {
        Bundle extras = getIntent().getExtras();
        String routeName = extras.getString(getString(R.string.name));
        String routeGrade = extras.getString(getString(R.string.grade));
        String routeDescr = extras.getString(getString(R.string.description));
        aRoute = new Route(routeName, routeGrade, routeDescr);
    }

    // puts the info from the Route object into the TextViews
    public void setTextViews() {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvGrade = (TextView) findViewById(R.id.tvGrade);
        TextView tvDescr = (TextView) findViewById(R.id.tvDescr);

        tvName.setText(aRoute.getName());
        tvGrade.setText(aRoute.getGrade());
        tvDescr.setText(aRoute.getDescription());
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

    public void goToRegisterLoginActivity() {
        Intent firstIntent = new Intent(this, MainActivity.class);
        this.startActivity(firstIntent);
    }

    public void goToRoutesList(View view) {
        Intent routesListIntent = new Intent(this,RoutesListActivity.class);
        this.startActivity(routesListIntent);
    }
}