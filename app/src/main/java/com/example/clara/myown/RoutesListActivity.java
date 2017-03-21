package com.example.clara.myown;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesListActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private static final String TAG = "Firebase_test";
    private FirebaseUser user;
    private ArrayList<String> routeNames = new ArrayList<String>();
    private ArrayList<Route> routes = new ArrayList<Route>();
    private ArrayAdapter arrayAdapter;
    private ListView lvRouteList;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setListener();

        makeRoutesListAdapter();

        getFromDB();

        listenToLongClicks();


    }

    public void updateUserInfo() {
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            fbUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });

    }

    /* Sets the authstate listener. This checks whether there is still
    a user logged in at a given moment, preventing non-logged in users
    from seeing pages they shouldn't
     */
    private void setListener() {
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

    public void makeRoutesListAdapter() {
        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, routeNames);
        lvRouteList = (ListView) findViewById(R.id.lvRouteList);
        assert lvRouteList != null;
        lvRouteList.setAdapter(arrayAdapter);
    }

    public void getFromDB() {
        Log.d("sd","in main part of getFromDB");
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("sd","getting all routes from FB");
                // get our object out of the database
                Map<String,Object> routesMap = (HashMap<String,Object>) dataSnapshot.child("routes").getValue();
                makeRouteNamesList(routesMap);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting the data failed, log a message
                Log.w(TAG, "Something went wrong:", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
        //return addedEvent;
    }

    public void makeRouteNamesList(Map<String,Object> routesMap) {
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : routesMap.entrySet()){
            //Get route map
            Map singleRoute = (Map) entry.getValue();
            //Get phone field and append to list
            String routeName = (String) singleRoute.get("name");
            String routeGrade = (String) singleRoute.get("grade");
            String routeDescr = (String) singleRoute.get("description");
            Route newRoute = new Route(routeName, routeGrade, routeDescr);
            routes.add((Route) newRoute);
            routeNames.add(routeName + " (" + routeGrade + ")");
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void goToAddRoute(View view) {
        Intent addRouteIntent = new Intent(this, AddRouteActivity.class);
        this.startActivity(addRouteIntent);
    }

    public void listenToLongClicks() {
        lvRouteList.setClickable(true);
        lvRouteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                viewItem(position);
                return true;
            }
        });
    }

    public void viewItem(int pos) {
        Route routeToView = routes.get(pos);
        goToShowRouteActivity(routeToView);
    }

    public void goToShowRouteActivity(Route route) {
        Intent routeIntent = new Intent(this, ShowRouteActivity.class);
        routeIntent.putExtra("name", route.name);
        routeIntent.putExtra("grade", route.grade);
        routeIntent.putExtra("descr", route.description);
        this.startActivity(routeIntent);
    }

    public void signOut (View view) {
        user = null;
        mAuth.signOut();
        setListener();
    }
}
