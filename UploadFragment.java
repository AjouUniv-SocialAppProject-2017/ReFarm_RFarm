package com.example.byoungeun_kim.exrefarm;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;


public class UploadFragment extends Fragment {

    String TAG = getClass().getSimpleName();
    Bitmap bitmap;
    ImageView ivUser;
    String stUid;
    String stEmail;
    private StorageReference mStorageRef;
    ProgressBar pbLogin;
    EditText etContent;
    EditText etTitle;
    String stContent;
    String stTitle;
    String photoUrl;
    Button btnUpload;
    Button btnCancel;

    Fragment fragment;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recommend, container, false);
        pbLogin = (ProgressBar)v.findViewById(R.id.pbLogin);

        etContent = (EditText)v.findViewById(R.id.etContent);
        etTitle = (EditText)v.findViewById(R.id.etTitle);


        btnUpload = (Button)v.findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"게시물 업로드 완료",Toast.LENGTH_SHORT).show();
                stTitle = etTitle.getText().toString();
                stContent = etContent.getText().toString();
                fragment = new RecommendFragment();
                switchFragment(fragment);


            }
        });






        btnCancel = (Button)v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new RecommendFragment();
                switchFragment(fragment);
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        stUid = sharedPreferences.getString("uid","");
        stEmail = sharedPreferences.getString("email","");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("like","0");






        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("userpost").child(stUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                String stPhoto = dataSnapshot.child("photo").getValue().toString();



                if(TextUtils.isEmpty(stPhoto)){
                    pbLogin.setVisibility(View.GONE);
                }else{
                    pbLogin.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(stPhoto).fit().centerInside().into(ivUser, new Callback.EmptyCallback() {
                        @Override public void onSuccess() {
                            // Index 0 is the image view.
                            Log.d(TAG,"SUCCESS");
                            pbLogin.setVisibility(View.GONE);

                            stTitle = etTitle.getText().toString();
                            stContent = etContent.getText().toString();
                        }
                    });
                }



                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        ivUser = (ImageView) v.findViewById(R.id.ivUser);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);

            }
        });



        return v;
    }



    public void uploadImage(){


        StorageReference mountainsRef = mStorageRef.child("upload").child(stUid+".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        final byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                photoUrl = String.valueOf(downloadUrl);
                Log.d("url",String.valueOf(downloadUrl));

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("userpost");

                Hashtable<String, String> upload = new Hashtable<String, String>();
                upload.put("email", stEmail);
                upload.put("key", stUid);
                upload.put("title", etTitle.getText().toString());
                upload.put("content",etContent.getText().toString());
                upload.put("photo", photoUrl);
                upload.put("like", "0");



                myRef.child(stUid).setValue(upload);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = dataSnapshot.getValue().toString();
                        Log.d("upload",s);
                        if(dataSnapshot != null){
                            Toast.makeText(getActivity(),"사진 업로드 완료",Toast.LENGTH_SHORT).show();
/*
                            //RecommendFragment로 값전달
                            fragment = new RecommendFragment();
                            Bundle bundle = new Bundle(5);
                            bundle.putString("email",stEmail );
                            bundle.putString("key", stUid);
                            bundle.putString("title", stTitle);
                            bundle.putString("content", stContent);
                            bundle.putString("photo", photoUrl);
                            fragment.setArguments(bundle);

*/
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
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
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

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
