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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * File: RoutesListActivity.java
 * Last edited: 28-3-2017
 * By: Clara Tump
 *
 * In this activity the list of all routes (which are shared between all users of the app) is
 * displayed. If a route is clicked, its info can be viewed in ShowRouteActivity. */

public class RoutesListActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListenerTest;
    private FirebaseUser user;
    private ArrayList<String> routeNames = new ArrayList<>();
    private ArrayList<Route> routes = new ArrayList<>();
    private ArrayAdapter arrayAdapter;
    private ListView lvRouteList;

    /* sets all necessary references, listeners, and adapters and
     * gets all previously created routes */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_list);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setListener();

        makeRoutesListAdapter();
        getRoutesFromDB();
        listenToLongClicks();
    }

    /* Sets the authstate listener. This checks whether there is still
     * a user logged in at a given moment, preventing non-logged in users
     * from seeing pages they shouldn't */
    private void setListener() {
        authListenerTest = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
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

    // Sets an ArrayAdapter on the list of all Routes (routesList)
    public void makeRoutesListAdapter() {
        arrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_list_item_1, android.R.id.text1, routeNames);
        lvRouteList = (ListView) findViewById(R.id.lvRouteList);
        assert lvRouteList != null;
        lvRouteList.setAdapter(arrayAdapter);
    }

    // gets all the previously created routes from Firebase
    public void getRoutesFromDB() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> routesMap = (HashMap<String,Object>) dataSnapshot.child("routes").getValue();
                makeRouteNamesList(routesMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(getString(R.string.Tag), getString(R.string.db_error), databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    // creates an ArrayList of the routes' names to put into the ListView
    public void makeRouteNamesList(Map<String,Object> routesMap) {
        for (Map.Entry<String, Object> entry : routesMap.entrySet()){
            Map singleRoute = (Map) entry.getValue();
            String routeName = (String) singleRoute.get(R.string.name);
            String routeGrade = (String) singleRoute.get(R.string.grade);
            String routeDescr = (String) singleRoute.get(R.string.description);
            Route newRoute = new Route(routeName, routeGrade, routeDescr);
            routes.add(newRoute);
            routeNames.add(routeName + " (" + routeGrade + ")");
        }
        arrayAdapter.notifyDataSetChanged();
    }

    public void goToAddRoute(View view) {
        Intent addRouteIntent = new Intent(this, AddRouteActivity.class);
        this.startActivity(addRouteIntent);
    }

    /* listens to long clicks on items of the ListView.
     * This ListView contains the names of all routes */
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

    /* Goes to ShowRoutesListActivitiy which shows the info of the route at a given position of
     * the ArrayList routes */
    public void viewItem(int pos) {
        Route routeToView = routes.get(pos);
        goToShowRouteActivity(routeToView);
    }

    /* go to ShowRouteActivity and pass the route info (name, grade, description)
     * through the Intent */
    public void goToShowRouteActivity(Route route) {
        Intent routeIntent = new Intent(this, ShowRouteActivity.class);
        routeIntent.putExtra("name", route.getName());
        routeIntent.putExtra("grade", route.getGrade());
        routeIntent.putExtra("descr", route.getDescription());
        this.startActivity(routeIntent);
    }

    /* Activated when the user clicks 'Sign out'. Signs out the user, after which the outhState
     * listener is triggered */
    public void signOut (View view) {
        user = null;
        mAuth.signOut();
        setListener();
    }
}