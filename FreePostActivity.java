package com.example.byoungeun_kim.exrefarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class FreePostActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    String TAG = getClass().getSimpleName();
    EditText etComment;
    TextView tvTitle;
    TextView tvContent;
    ImageView ivPhoto;
    Button btnFreeComment;


    String email;
    String stComment;
    String stContent;
    String stTitle;
    String stChatId;
    String stPhoto;


    List<FreePost> mFreePost;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freepost);

        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url

            email = user.getEmail();

        }

        etComment = (EditText) findViewById(R.id.etComment);
        btnFreeComment = (Button) findViewById(R.id.btnFreeComment);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        mRecyclerView = (RecyclerView) findViewById(R.id.freepost_recycler_view);
        ivPhoto = (ImageView) findViewById(R.id.ivUser);

        //RecommendAdapter에서 받은 값
        Intent in = getIntent();
        stChatId = in.getStringExtra("friendUid");
        stTitle = in.getStringExtra("title");
        stPhoto = in.getStringExtra("photo");
        stContent = in.getStringExtra("content");

        mFreePost = new ArrayList<>();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new FreePostAdapter(mFreePost, email, FreePostActivity.this);
        mRecyclerView.setAdapter(mAdapter);


        tvTitle.setText(stTitle);
        tvContent.setText(stContent);


        btnFreeComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                stComment = etComment.getText().toString();
                if (stComment.equals("") || stComment.isEmpty()) {
                    Toast.makeText(FreePostActivity.this, "댓글내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(FreePostActivity.this, email + "," + stComment, Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    // Write a message to the database

                    DatabaseReference myRef = database.getReference("freepost").child(stChatId).child("comment").child(formattedDate);

                    Hashtable<String, String> comment = new Hashtable<String, String>();
                    comment.put("email", email);
                    comment.put("title", stTitle);
                    comment.put("content", stContent);
                    comment.put("comment", stComment);
                    comment.put("photo", stPhoto);
                    myRef.setValue(comment);
                    etComment.setText("");

                }


            }
        });


        DatabaseReference myRef = database.getReference("freepost");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
              //  String value = dataSnapshot.getValue(String.class);
              //  Log.d(TAG, "Value is: " + value);

                if (TextUtils.isEmpty(stPhoto)) {

                    Picasso.with(FreePostActivity.this)
                            .load(R.drawable.ic_nophoto)
                            .fit()
                            .centerInside()
                            .into(ivPhoto, new Callback.EmptyCallback() {
                                @Override public void onSuccess() {
                                    // Index 0 is the image view.
                                    Log.d(TAG,"Photo is empty");

                                }
                            });

                } else {


                    Picasso.with(FreePostActivity.this)
                            .load(stPhoto)
                            .fit()
                            .centerInside()
                            .into(ivPhoto, new Callback.EmptyCallback() {
                                @Override public void onSuccess() {
                                    // Index 0 is the image view.
                                    Log.d(TAG,"SUCCESS");

                                }
                            });
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        myRef.child(stChatId).child("comment").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                FreePost freepost = dataSnapshot.getValue(FreePost.class);

                // [START_EXCLUDE]
                // Update RecyclerView

                mFreePost.add(freepost);
                mRecyclerView.scrollToPosition(mFreePost.size() - 1);
                mAdapter.notifyItemInserted(mFreePost.size() - 1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
