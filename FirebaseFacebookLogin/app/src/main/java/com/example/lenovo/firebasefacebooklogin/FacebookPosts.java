package com.example.lenovo.firebasefacebooklogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class FacebookPosts extends AppCompatActivity implements Constants {

    private String TAG ="FacebookPosts";
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_posts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        TextView textViewTitle = (TextView) findViewById(R.id.postTitle);
        textViewTitle.setText(getIntent().getStringExtra(NAME));

        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null) {
          Log.i(TAG,"token is"+token);
                }
        String id = getIntent().getStringExtra(ID);

                                            GraphRequest request = GraphRequest.newGraphPathRequest(
                                                    token,
                                                    "/"+id+"/posts/",
                                                    new GraphRequest.Callback() {
                                                        @Override
                                                        public void onCompleted(GraphResponse response) {
                                                            // Insert your code here
                                                            Log.i(TAG,"posts are"+response);

                                                            try {
                                                                JSONObject jsonObject = response.getJSONObject();
                                                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                Log.i(TAG, "json array length" + jsonArray.length());

                                                               FacebookPostsAdapter facebookPostsAdapter = new FacebookPostsAdapter(jsonArray,getBaseContext());
                                                                recyclerView.setAdapter(facebookPostsAdapter);
                                                                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                                                                recyclerView.setLayoutManager(layoutManager);



                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    });
                                            request.executeAsync();





    }
}
