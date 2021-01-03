package com.example.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninOrSignupFragment extends Fragment implements  View.OnClickListener{
    View rootview;
    private FirebaseAnalytics mFirebaseAnalytics;
    public static FirebaseAuth mAuth;
    String id,name;
    Button button;
    Button emailSignInButton;
    Button emailSignUpButton;
    EditText Password_editText;
    EditText Username_editText;
    ProgressBar progressBar;
    TextView status;
    Context context;
    private static final String TAG = SigninOrSignupFragment.class.getSimpleName();
    SignupSuccessCallBack signupSuccessCallBack;
    Common common;
  //  CommonDb commonDb;
    public SigninOrSignupFragment(SignupSuccessCallBack signupSuccessCallBack) {
        this.signupSuccessCallBack=signupSuccessCallBack;
    }

    public SigninOrSignupFragment() {
        //this.signupSuccessCallBack=signupSuccessCallBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_signin_or_signup, container, false);
        emailSignInButton=rootview.findViewById(R.id.SignIn);
        emailSignUpButton=rootview.findViewById(R.id.SignUp);
        Password_editText=rootview.findViewById(R.id.Password_editText);
        Username_editText=rootview.findViewById(R.id.Username_editText);
        progressBar=rootview.findViewById(R.id.progressBar);
        status=rootview.findViewById(R.id.status);
        emailSignInButton.setOnClickListener(this);
        emailSignUpButton.setOnClickListener(this);
        common=new Common();
       // commonDb=new CommonDb();
        return rootview;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            updateUI(currentUser);
        }
    }

    interface SignupSuccessCallBack {
        public void signupSuccess_nowValidate(FirebaseUser user,  FirebaseAuth mAuth);
    }
    private void createUserWithEmailAndPassword(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        common.showProgressBar(progressBar);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    status.setVisibility(View.GONE);
                    signupSuccessCallBack.signupSuccess_nowValidate(user,mAuth);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    common.checkForMultiFactorFailure(task.getException(),status,getActivity());
                    Username_editText.setText("");
                    Password_editText.setText("");
                }
                common.hideProgressBar(progressBar);
            }
        });
    }

    private void signInWithEmailAndPassword(String email, String password){
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        common.showProgressBar(progressBar);mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    status.setVisibility(View.GONE);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(getActivity(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                    common.checkForMultiFactorFailure(task.getException(),status,getActivity());
                    updateUI(null);
                }
                common.hideProgressBar(progressBar);
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        common.updateUI(user,getContext(),mAuth);
        common.hideProgressBar(progressBar);
    }



    private boolean validateForm() {
        boolean valid = true;
        String email = Username_editText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Username_editText.setError("Required.");
            valid = false;
        } else {
            Username_editText.setError(null);
        }

        String password = Password_editText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Password_editText.setError("Required.");
            valid = false;
        } else {
            Password_editText.setError(null);
        }

        return valid;
    }



    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.SignIn) {
            signInWithEmailAndPassword(Username_editText.getText().toString(),Password_editText.getText().toString());
        } else if (i == R.id.SignUp) {
            createUserWithEmailAndPassword(Username_editText.getText().toString(),Password_editText.getText().toString());
        }
    }



}