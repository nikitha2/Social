package com.example.social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements SigninOrSignupFragment.SignupSuccessCallBack {
    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    static Context  context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(null);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null){
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerForFragment,new SigninOrSignupFragment(this),null)
                    .setReorderingAllowed(true)
                    .addToBackStack("SigninOrSignupFragment") // name can be null
                    .commit();
        }
    }

    @Override
    public void signupSuccess_nowValidate(FirebaseUser user, FirebaseAuth mAuth) {
        fragmentManager.beginTransaction()
                .replace(R.id.containerForFragment, new ValidateFragment(user,mAuth), null)
                .setReorderingAllowed(true)
                .addToBackStack("SigninOrSignupFragment") // name can be null
                .commit();
    }


}