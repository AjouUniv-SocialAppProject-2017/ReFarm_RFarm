package com.example.byoungeun_kim.exrefarm;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by byoungeun-Kim on 2017. 11. 20..
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private String[] mDataset;
    List<Post> mPost;
    String stEmail;
    Context context;

    //Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView ivUser;


        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.mTextView);
            ivUser = (ImageView)itemView.findViewById(R.id.ivUser);

        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public PostAdapter(List<Post> mPost, String email, Context context) {
        this.mPost = mPost;
        this.stEmail = email;
        this.context = context;
     //   this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v;


           v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_text_view, parent, false);

        // create a new view

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

            holder.mTextView.setText(mPost.get(position).getEmail() + " : " + mPost.get(position).getComment());



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPost.size();
    }
}