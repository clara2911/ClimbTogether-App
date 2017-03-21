package com.example.clara.myown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyTops extends AppCompatActivity {
    private static final String TAG = "Firebase_test";
    private ArrayList<String> routeNmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tops);

        Intent intent = getIntent();
        ArrayList<String> routeNmsList = intent.getStringArrayListExtra("routeNmsList");
        if(routeNmsList == null) {
            TextView noRoutes = (TextView) findViewById(R.id.noRoutes);
            noRoutes.setText("No routes added yet");
        }
    }

    public void goToRoutesList(View view) {
        Intent routesListIntent = new Intent(this,RoutesListActivity.class);
        this.startActivity(routesListIntent);
    }
}
