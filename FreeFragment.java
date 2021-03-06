package com.example.byoungeun_kim.exrefarm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FreeFragment extends Fragment {

    String TAG = getClass().getSimpleName();
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;


    List<Free> mFree;


    FirebaseDatabase database;
    FreeAdapter mAdapter;
    Button btnUpload;
    Fragment fragment;
    String stEmail;
    String stUid;
    String stTitle;
    String stContent;
    String photoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_free, container, false);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        stUid = sharedPreferences.getString("uid", "");
        stEmail = sharedPreferences.getString("email", "");
        stTitle = sharedPreferences.getString("title", "");



        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvUpload);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        mFree = new ArrayList<>();
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new FreeAdapter(mFree, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        database=  FirebaseDatabase.getInstance();


        btnUpload = (Button)v.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new FreeUploadFragment();
                switchFragment(fragment);
            }
        });

/*
        //UploadFragment에서 받아온 값
        stEmail = getArguments().getString("email");
        stUid = getArguments().getString("key");
        stTitle = getArguments().getString("title");
        stContent = getArguments().getString("content");
        photoUrl = getArguments().getString("photo");


*/



        DatabaseReference myRef = database.getReference();
        myRef.child("freepost").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                  //  String value = dataSnapshot.child(stUid).child("upload").getValue().toString();
                  //  Log.d(TAG, "upload Value is: " + value);
                //    mRecommend.clear();
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){


                        String value2 = dataSnapshot2.getValue().toString();
                        Log.d(TAG, "free Value is: " + value2);
                        Free free = dataSnapshot2.getValue(Free.class);

                        // [START_EXCLUDE]
                        // Update RecyclerView

                        mFree.add(free);
                        mAdapter.notifyItemInserted(mFree.size() - 1);
                        mAdapter.notifyDataSetChanged();
                    }






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });


        return v;

    }
    public void switchFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.content, fragment);


// Commit the transaction
        transaction.commit();
    }


}