package com.example.lenovo.firebasefacebooklogin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramu on 02/03/17.
 */

public class FacebookPostsAdapter extends RecyclerView.Adapter<FacebookPostsAdapter.FacebookPageViewHolder>  implements Constants{

    final String TAG = "FacebookPagesAdapter";
    private JSONArray list;
    private Context contextM;
    private int flag=0;
    public FacebookPostsAdapter(JSONArray list, Context context) {
        this.list = list;
        this.contextM=context;
        Log.i(TAG,"list in adapter"+list);
    }

    @Override
    public int getItemCount() {
        if(list !=null)
            return list.length();
        return 0;
    }

    @Override
    public FacebookPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.facebook_posts_adapter, parent, false);
        return new FacebookPageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FacebookPageViewHolder holder, int position) {

        try{
            final JSONObject hashMap = (JSONObject) list.get(position);
            Log.i(TAG,"list map"+hashMap);

            holder.postMessage.setText(hashMap.get(MESSAGE).toString());
            holder.postTime.setText(hashMap.get(CREATEDTIME).toString());
            final AccessToken token = AccessToken.getCurrentAccessToken();
            final String id = hashMap.get(ID).toString();

            holder.viewCommentsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag == 0) {
                        flag = 1; // 1 => Button ON

                        GraphRequest request = GraphRequest.newGraphPathRequest(
                                token,
                                "/"+id+"/comments/",
                                new GraphRequest.Callback() {
                                    @Override
                                    public void onCompleted(GraphResponse response) {
                                        // Insert your code here
                                        Log.i(TAG,"comments are"+response);

                                        try {

                                            JSONObject jsonObject = response.getJSONObject();
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            Log.i(TAG, "json array length" + jsonArray.length());

                                            FacebookCommentsAdapter facebookCommentsAdapter = new FacebookCommentsAdapter(jsonArray,contextM);
                                            holder.viewCommentsRecyclerView.setAdapter(facebookCommentsAdapter);


                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                });
                        request.executeAsync();

                    holder.viewCommentsTextview.setTextColor(contextM.getResources().getColor(R.color.colorPrimary));
                    holder.viewCommentsRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        flag = 0; // 0 => Button OFF
                     //   Toast.makeText(contextM,"no comments",Toast.LENGTH_LONG).show();
                        holder.viewCommentsRecyclerView.setVisibility(View.GONE);
                        holder.viewCommentsTextview.setTextColor(contextM.getResources().getColor(R.color.gray));

                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(contextM);
                    holder.viewCommentsRecyclerView.setLayoutManager(layoutManager);
                }
            });

                }catch(Exception e){
            Log.i(TAG,e.toString());
        }
    }

    public static class FacebookPageViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView postMessage;
        public TextView postTime;

        public TextView viewCommentsTextview;
        public RecyclerView viewCommentsRecyclerView;

        public LinearLayout linearLayout,viewCommentsLayout;

        public FacebookPageViewHolder(final View itemView) {
            super(itemView);
            id   = (TextView)itemView.findViewById(R.id.postId);
            postMessage = (TextView)itemView.findViewById(R.id.postMessage);
            postTime       = (TextView) itemView.findViewById(R.id.postCreatedTime);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.posts_layout);

            viewCommentsTextview = (TextView) itemView.findViewById(R.id.viewCommentsTextview);
            viewCommentsRecyclerView = (RecyclerView) itemView.findViewById(R.id.viewCommentsRecyclerView);
            viewCommentsLayout = (LinearLayout) itemView.findViewById(R.id.viewCommentsLayout);

        }
    }
}