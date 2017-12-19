package com.example.byoungeun_kim.exrefarm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.ViewHolder> {

    List<Recommend> mRecommend;
    Context context;
    String TAG = getClass().getSimpleName();



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvEmail;
        public ImageView ivUser;
        public TextView tvTitle;
        public Button btnChat;




        public ViewHolder(View itemView) {
            super(itemView);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
            btnChat = (Button)itemView.findViewById(R.id.btnChat);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecommendAdapter(List<Recommend> mRecommend, Context context) {
        this.mRecommend = mRecommend;
        this.context = context;
    }

    @Override
    public RecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_recommend, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvEmail.setText(mRecommend.get(position).getEmail());
        holder.tvTitle.setText(mRecommend.get(position).getTitle());



        final String stPhoto = mRecommend.get(position).getPhoto();
        Log.d(TAG, "mRecommend stPhoto Value is: " + stPhoto);

        if(TextUtils.isEmpty(stPhoto)){

            Picasso.with(context)
                    .load(R.drawable.ic_nophoto)
                    .fit()
                    .centerInside()
                    .into(holder.ivUser);

        }else{


            Picasso.with(context)
                    .load(stPhoto)
                    .fit()
                    .centerInside()
                    .into(holder.ivUser);
        }

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //String stPosition = String.valueOf(position);
                String stFriendId = mRecommend.get(position).getKey();
                String stTitle = mRecommend.get(position).getTitle();
                String stContent = mRecommend.get(position).getContent();


                Intent in = new Intent(context, PostActivity.class);
                in.putExtra("friendUid", stFriendId);
                in.putExtra("photo",stPhoto);
                in.putExtra("title",stTitle);
                in.putExtra("content",stContent);

                context.startActivity(in);

               // fragment = new PostFragment();
               // switchFragment(fragment);


            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRecommend.size();
    }
/*
    public void switchFragment(Fragment fragment){


       // FragmentTransaction transaction = getFragmentManager().beginTransaction();

        FragmentTransaction transaction = fragment.getFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.content, fragment);


// Commit the transaction
        transaction.commit();
    }

*/

}