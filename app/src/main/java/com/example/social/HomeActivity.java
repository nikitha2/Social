package com.example.social;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    private TextView mTextView;
    private FirebaseAuth mAuth;
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
    }
}