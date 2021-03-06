package com.example.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabItem;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    private TextView mTextView;
    private FirebaseAuth mAuth;
    TabItem home;
    TabItem search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        String profileName = mAuth.getCurrentUser().getEmail().split("@")[0];
        setTitle(profileName);

        mTextView = (TextView) findViewById(R.id.text);

        Intent intent = getIntent();
        intent.getStringExtra(getString(R.string.id));

        if(savedInstanceState==null){
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_home, new HomeFragment(), null)
                    .setReorderingAllowed(true)
                    .addToBackStack("HomeFragment") // name can be null
                    .commit();
        }


        home=findViewById(R.id.home);
        search=findViewById(R.id.search);
    }

    @Override
    protected void onResume() {
        super.onResume();

        home.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_home, new HomeFragment(), null)
                        .setReorderingAllowed(true)
                        .addToBackStack("HomeFragment") // name can be null
                        .commit();
            }
        });

        search.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_home, new SearchFragment(), null)
                        .setReorderingAllowed(true)
                        .addToBackStack("HomeFragment") // name can be null
                        .commit();
            }
        });
    }
}