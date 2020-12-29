package com.example.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

public class MainActivity extends AppCompatActivity implements SigninOrSignupFragment.SignupSuccessCallBack {
    private static final String TAG = MainActivity.class.getSimpleName();
    FragmentManager fragmentManager;
    static Context context;
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