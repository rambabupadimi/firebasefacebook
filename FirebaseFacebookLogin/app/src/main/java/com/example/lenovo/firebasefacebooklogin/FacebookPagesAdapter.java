package com.example.lenovo.firebasefacebooklogin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lenovo on 1.3.17.
 */
public class FacebookPagesAdapter extends RecyclerView.Adapter<FacebookPagesAdapter.FacebookPageViewHolder>  implements Constants{

    final String TAG = "FacebookPagesAdapter";
    private List<Map> list;
    private Context contextM;
    public FacebookPagesAdapter(List list, Context context) {
        this.list = list;
        this.contextM=context;
        Log.i(TAG,"list in adapter"+list);
    }

    @Override
    public int getItemCount() {
        if(list !=null)
            return list.size();
        return 0;
    }

    @Override
    public FacebookPageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faceboo_pages_adapter, parent, false);
        return new FacebookPageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FacebookPageViewHolder holder, int position) {

        try{
            final HashMap hashMap = (HashMap) list.get(position);
            Log.i(TAG,"list map"+hashMap);

            holder.name.setText(hashMap.get(NAME).toString());
            holder.category.setText(hashMap.get(CATEGORY).toString());

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(contextM,FacebookPosts.class);
                    intent.putExtra(ID,hashMap.get(ID).toString());
                    intent.putExtra(NAME,hashMap.get(NAME).toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    contextM.startActivity(intent);
                }
            });
        }catch(Exception e){
            Log.i(TAG,e.toString());
        }
    }

    public static class FacebookPageViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView name;
        public TextView category;

        public LinearLayout linearLayout;

        public FacebookPageViewHolder(final View itemView) {
            super(itemView);
            id   = (TextView)itemView.findViewById(R.id.id);
            name = (TextView)itemView.findViewById(R.id.name);
            category       = (TextView) itemView.findViewById(R.id.category);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.pages_layout);
        }
    }
}

