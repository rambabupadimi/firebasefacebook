package com.example.lenovo.firebasefacebooklogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Constants{

    private String TAG = "MainActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;
    private DatabaseReference firebaseDatabase;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager =  CallbackManager.Factory.create();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");

        try
        {
            if(getIntent().getStringExtra("logout").toString().equalsIgnoreCase("logout")){
                firebaseAuth.signOut();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.setReadPermissions("email","user_likes");


                // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                progressDialog.dismiss();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                progressDialog.dismiss();
            }
        });


        authStateListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser!=null){

                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Log.i(TAG,"signed out");
                }
            }
        };

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.lenovo.firebasefacebooklogin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

    }



    private void handleFacebookAccessToken(final AccessToken accesstoken)
    {



        Log.i(TAG,"facebook accesstoken"+accesstoken);
        Log.i(TAG,"facebook accesstoken1"+AccessToken.getCurrentAccessToken());
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accesstoken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    GraphRequest data_request = GraphRequest.newMeRequest(
                            AccessToken.getCurrentAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(


                                        final JSONObject json_object,
                                        GraphResponse response) {

                                    try {
                                        final List<Map> likedPagesList = new ArrayList<Map>();

                                        Log.i(TAG,"json object "+json_object);
                                        Log.i(TAG,"json response"+response);
                                        JSONArray posts = json_object.getJSONObject("likes").optJSONArray("data");
                                        // Log.e(&quot;data1&quot;,posts.toString());

                                        for (int i = 0; i < posts.length(); i++) {

                                            JSONObject post = posts.optJSONObject(i);
                                            String id = post.optString("id");
                                            String category = post.optString("category");
                                            String name = post.optString("name");
                                            int count = post.optInt("likes");

                                            Log.i(TAG,"id"+id+"name"+name+"category"+category+"count"+count);

                                            HashMap hashMap = new HashMap();
                                            hashMap.put(ID,id);
                                            hashMap.put(NAME,name);
                                            hashMap.put(CATEGORY,category);
                                            likedPagesList.add(hashMap);


                                            String userid = firebaseAuth.getCurrentUser().getUid();
                                            DatabaseReference currentuserdb = firebaseDatabase.child(userid);
                                            currentuserdb.setValue(likedPagesList);
                                            currentuserdb.keepSynced(true);

                                        }

                                    } catch(Exception e){

                                    }



                                }
                            });
                    Bundle permission_param = new Bundle();
                    permission_param.putString("fields","likes{id,category,name,location,likes}");
                    data_request.setParameters(permission_param);
                    data_request.executeAsync();

                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"authantication success",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    intent.putExtra("token",accesstoken);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    Toast.makeText(MainActivity.this,"authantication failed",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(authStateListener!=null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
