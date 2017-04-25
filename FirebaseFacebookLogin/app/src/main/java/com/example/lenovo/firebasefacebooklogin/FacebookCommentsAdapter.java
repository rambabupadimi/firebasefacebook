package com.example.lenovo.firebasefacebooklogin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ramu on 02/03/17.
 */

public class FacebookCommentsAdapter extends RecyclerView.Adapter<FacebookCommentsAdapter.FacebookPageViewHolder>  implements Constants{

    final String TAG = "FacebookPagesAdapter";
    private JSONArray list;
    private Context contextM;
    private int flag=0;
    public FacebookCommentsAdapter(JSONArray list, Context context) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.facebook_comments_adapter, parent, false);
        return new FacebookPageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FacebookPageViewHolder holder, int position) {

        try{
            final JSONObject jsonObject = (JSONObject) list.get(position);
            Log.i(TAG,"comments object"+jsonObject);
            JSONObject jsonObject1 = (JSONObject) jsonObject.get("from");
            holder.commentMessage.setText(jsonObject.get(MESSAGE).toString());
            holder.commentBy.setText(jsonObject1.get(NAME).toString());

        }catch(Exception e){
            Log.i(TAG,e.toString());
        }
    }

    public static class FacebookPageViewHolder extends RecyclerView.ViewHolder {
        public TextView commentMessage;
        public TextView commentBy;

         public LinearLayout linearLayout;

        public FacebookPageViewHolder(final View itemView) {
            super(itemView);
            commentMessage = (TextView)itemView.findViewById(R.id.commentMessage);
            commentBy       = (TextView) itemView.findViewById(R.id.commentyBy);
        //    linearLayout = (LinearLayout) itemView.findViewById(R.id.posts_layout);


        }
    }
}