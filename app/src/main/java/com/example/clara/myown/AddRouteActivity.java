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

public class AddRouteActivity extends AppCompatActivity {

    private FirebaseAuth authTest;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_test";
    private DatabaseReference mDatabase;
    private Route aRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);
        Intent intent = getIntent();

        authTest = FirebaseAuth.getInstance();
        setListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }


    /* Sets the authstate listener. This checks whether there is still
    a user logged in at a given moment, preventing non-logged in users
    from seeing pages they shouldn't
     */
    private void setListener() {
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
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

    public void addToDB(View view) {
        // Get values from editTexts
        EditText et1 = (EditText) findViewById(R.id.editText1);
        EditText et2 = (EditText) findViewById(R.id.editText2);
        EditText et3 = (EditText) findViewById(R.id.editText3);

        String name = et1.getText().toString();
        String grade = et2.getText().toString();
        String description = et3.getText().toString();
        if(name!= null && grade != null && description != null) {

            // add an object to the database
            aRoute = new Route(name, grade, description);
            // possible to overwrite routes now!
            mDatabase.child("routes").child(aRoute.name).setValue(aRoute);

            et1.getText().clear();
            et2.getText().clear();
            et3.getText().clear();

            goToShowRoute();
        } else {
            Toast.makeText(AddRouteActivity.this, "Input field cannot be empty",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void goToShowRoute() {
        Intent routeIntent = new Intent(this, ShowRouteActivity.class);
        Log.d("ngdSecondAct","here"+aRoute.name + aRoute.grade + aRoute.description);
        routeIntent.putExtra("name", aRoute.name);
        routeIntent.putExtra("grade", aRoute.grade);
        routeIntent.putExtra("descr", aRoute.description);
        this.startActivity(routeIntent);
    }

    public void getFromDB(View view) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sd","in getFromDB");
                // get our object out of the database
                aRoute = dataSnapshot.child("routes").child("route1").getValue(Route.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting the data failed, log a message
                Log.w(TAG, "Something went wrong:", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void goToRegisterLoginActivity() {
        Intent firstIntent = new Intent(this, MainActivity.class);
        this.startActivity(firstIntent);
    }

    public void login(View view) {
        Log.d("sd", "Trying to login from second Activity");
    }
}
