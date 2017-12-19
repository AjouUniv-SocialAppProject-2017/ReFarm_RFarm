package com.example.byoungeun_kim.exrefarm;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.TextUtilsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.jar.*;

import static android.R.attr.bitmap;
import static android.content.ContentValues.TAG;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.byoungeun_kim.exrefarm.R.id.pbLogin;
import static com.example.byoungeun_kim.exrefarm.R.id.up;


public class ProfileFragment extends Fragment {

    String TAG = getClass().getSimpleName();
    String stUid;
    String stEmail;
    String  stLike;
    String stTitle;
    String stName;
    int totalLike;


    CheckBox likeCB;
    ProgressBar pbLogin;
    TextView likeTV1;
    ImageView ivUser;
    private StorageReference mStorageRef;
    Bitmap bitmap;
    EditText etName;
    TextView tvName;
    Button btnName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_profile, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        stUid = sharedPreferences.getString("uid", "");
        stEmail = sharedPreferences.getString("email", "");


        pbLogin = (ProgressBar)v.findViewById(R.id.pbLogin);
        etName = (EditText)v.findViewById(R.id.etName);
        tvName = (TextView) v.findViewById(R.id.tvName);
        btnName = (Button) v.findViewById(R.id.btnName);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stName = etName.getText().toString();
                etName.setText("");
                tvName.setText(stName);
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        myRef.child("users").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue().toString();
                String stPhoto = dataSnapshot.child("photo").getValue().toString();


                if (TextUtils.isEmpty(stPhoto)) {
                    pbLogin.setVisibility(View.GONE);

                } else {
            pbLogin.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity())
                            .load(stPhoto)
                            .fit()
                            .centerInside()
                            .into(ivUser, new Callback.EmptyCallback() {

                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            Log.d(TAG,"SUCCESS");
                            pbLogin.setVisibility(View.GONE);
                        }
                    });

                }
                    Log.d(TAG, "Value is: " + value);

                }

            @Override
            public void onCancelled(DatabaseError error) {

            Log.v(TAG, "Failed to read value.",error.toException());

            }
        });



        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            }
            else{
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
            }
        }





        ivUser = (ImageView) v.findViewById(R.id.ivUser);


        ivUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        Button btnLogout = (Button) v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });


    return v;
    }








public  void uploadImage() {

    StorageReference mountainRef = mStorageRef.child("users").child(stUid + ".jpg");
//'user'라는 폴더를 만들어서 관리

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();
    UploadTask uploadTask = mountainRef.putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @SuppressWarnings("VisibleForTests")
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri downloadUri = taskSnapshot.getDownloadUrl();
            String photoUri = String.valueOf(downloadUri);
            Log.d("url", photoUri);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");




            Hashtable<String, String> profile = new Hashtable<String, String>();

            profile.put("email", stEmail);
            profile.put("key", stUid);
            profile.put("photo", photoUri);




            myRef.child(stUid).setValue(profile);

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s = dataSnapshot.getValue().toString();
                    Log.d("Profile", s);
                    if (dataSnapshot != null) {
                        Toast.makeText(getActivity(), "사진 업로드가 잘 됐습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    });
}






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        try {
            bitmap =MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
            ivUser.setImageBitmap(bitmap);
            uploadImage();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


        }}
        }
