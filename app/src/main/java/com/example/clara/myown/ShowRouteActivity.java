package com.example.clara.myown;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ShowRouteActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvGrade;
    private TextView tvDescr;
    private DatabaseReference mDatabase;
    private Route aRoute;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_test";
    private FirebaseUser user;
    private User currentUser;
    private ArrayList<String> toppedRoutesNames;
    private ArrayList<Route> toppedRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);

        Bundle extras = getIntent().getExtras();
        String routeName = extras.getString("name");
        String routeGrade = extras.getString("grade");
        String routeDescr = extras.getString("descr");

        aRoute = new Route(routeName, routeGrade, routeDescr);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");


        tvName = (TextView) findViewById(R.id.tvName);
        tvGrade = (TextView) findViewById(R.id.tvGrade);
        tvDescr = (TextView) findViewById(R.id.tvDescr);

        tvName.setText(aRoute.name);
        tvGrade.setText(aRoute.grade);
        tvDescr.setText(aRoute.description);

        setListener();
    }

    /* Sets the authstate listener. This checks whether there is still
    a user logged in at a given moment, preventing non-logged in users
    from seeing pages they shouldn't
     */
    private void setListener() {
        Log.d("lskdfjlsdf", "Setting listener!");
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.d("insetlistener", "User is not null");
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    goToRegisterLoginActivity();
                }
            }
        };
    }

    public void goToRegisterLoginActivity() {
        Intent firstIntent = new Intent(this, MainActivity.class);
        this.startActivity(firstIntent);
    }

    public void addToMyRoutes(View view) {
        Log.d("sd","adding route to my routes");
        String userId = getUserId();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        getUserInfoFromDB(userId);
        if(toppedRoutes == null) {
            Log.d("sd", "toppedRoutes is null");
        } if(aRoute == null) {
            Log.d("sd", "aRoute is null");
        }
        toppedRoutes.add(aRoute);
        currentUser.setToppedRoutes(toppedRoutes);
        toppedRoutesNames.add(aRoute.name + "(" + aRoute.grade + ")");
    }



    public String getUserId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        Log.d("sd", "got user id: "+ userId);
        return userId;
    }

    public void getUserInfoFromDB(final String userId) {
            Log.d("sd", "in main part of getting info from DB");
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("sd","in data change part of getting info from DB");
                    // get our object out of the database
                    currentUser = (User) dataSnapshot.getValue(User.class);
                    Log.d("sd","current User.email" + currentUser.email);
                    Log.d("sd", "making names list");
                    makeTopRouteNmsList(currentUser);
                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {}
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting the data failed, log a message
                    Log.w(TAG, "Something went wrong:", databaseError.toException());
                }
            });
    }

    public void makeTopRouteNmsList(User currentUser) {
        toppedRoutesNames = new ArrayList<String>();
        toppedRoutes = new ArrayList<Route>();
        Route testRoute = new Route("testRoute", "6a", "This is a route to test");
        toppedRoutes.add(testRoute);
        if(currentUser.toppedRoutes != null) {
            ArrayList<Route> routesList = new ArrayList<Route>(currentUser.toppedRoutes);
            for (int i = 0; i < routesList.size(); i++) {
                String routeName = routesList.get(i).name;
                String routeGrade = routesList.get(i).grade;
                toppedRoutes.add(routesList.get(i));
                toppedRoutesNames.add(routeName + " (" + routeGrade + ")");
            }
        } else {
            currentUser.setToppedRoutes(toppedRoutes);
            Log.d("sd","added test route ");
        }
    }



    public void goToMyTops(View view) {
        Intent topIntent = new Intent(this, MyTops.class);
        topIntent.putExtra("routesNmsList",toppedRoutesNames);
        this.startActivity(topIntent);
    }

    public void goToRoutesList(View view) {
        Intent routesListIntent = new Intent(this,RoutesListActivity.class);
        this.startActivity(routesListIntent);
    }

}