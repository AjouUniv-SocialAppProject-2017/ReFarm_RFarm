package com.example.byoungeun_kim.exrefarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TabActivity extends AppCompatActivity {

    Fragment fragment;
    long lastPressed;
    Button btnRecommend;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    switchFragment(fragment);

                    return true;

                case R.id.navigation_dashboard:
                    fragment = new FriendsFragment();
                    switchFragment(fragment);

                    return true;

                case R.id.navigation_notifications:
                    fragment = new ProfileFragment();
                    switchFragment(fragment);

                    return true;

                case R.id.navigation_map:
                    Intent i = new Intent(TabActivity.this, MapsActivity.class);
                    startActivity(i);

                    return true;
            }
            return false;
        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    public void switchFragment(Fragment fragment){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
        transaction.replace(R.id.content, fragment);


// Commit the transaction
        transaction.commit();
    }

    @Override
    public void onBackPressed() {


        if(System.currentTimeMillis()-lastPressed < 1500){
            finish();
        }
        Toast.makeText(this,"한번 더 누르면 종료됩니다",Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }
}
