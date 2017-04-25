package com.example.lenovo.firebasefacebooklogin;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacebookPages extends AppCompatActivity {

    private String TAG = "FacebookPages";
    private RecyclerView recyclerView;
    Query databaseReference1;
    FirebaseAuth firebaseAuth;


    List<Map> mainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_pages);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference1   = FirebaseDatabase.getInstance().getReference().child(firebaseAuth.getCurrentUser().getUid());
        Log.i(TAG,"database base referecnece1"+databaseReference1);
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               Log.i(TAG,"data snapchat"+dataSnapshot);
                Log.i(TAG,"data snapchat value"+dataSnapshot.getValue());
                mainList = (List<Map>) dataSnapshot.getValue();
                FacebookPagesAdapter facebookPagesAdapter = new FacebookPagesAdapter(mainList,getBaseContext());
                recyclerView.setAdapter(facebookPagesAdapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                recyclerView.setLayoutManager(layoutManager);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    @Override
    public void onStart() {
        super.onStart();

    }



}
