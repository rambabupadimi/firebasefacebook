package com.example.lenovo.firebasefacebooklogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private String TAG = "ProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        Button logout = (Button) findViewById(R.id.logout);

        final TextView username = (TextView)findViewById(R.id.name);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);


            Log.i(TAG,"show data"+firebaseAuth.getCurrentUser().getEmail());
        username.setText(firebaseAuth.getCurrentUser().getEmail());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ProfileActivity.this,FacebookPages.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();

                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.putExtra("logout","logout");
                startActivity(intent);


            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){
                    Log.i(TAG,"show data"+firebaseUser.getEmail());
                 //   username.setText(firebaseUser.getDisplayName());
                 }
                else{
                    Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };

    }
}
