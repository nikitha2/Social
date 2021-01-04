package com.example.social;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;


public class ValidateFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ValidateFragment.class.getSimpleName();
    View rootview;
    FirebaseUser user;
    Button verify;
    FirebaseAuth mAuth;
    TextView status_verify;
    Common common;

    public ValidateFragment(FirebaseUser user, FirebaseAuth mAuth) {
        this.user=user;
        this.mAuth=mAuth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
     rootview=  inflater.inflate(R.layout.fragment_validate, container, false);
      setHasOptionsMenu(true);
     verify=rootview.findViewById(R.id.verify);
     verify.setOnClickListener(this);
     status_verify=rootview.findViewById(R.id.status_verify);
     common=new Common();
     return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu1, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh){
            reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUI(mAuth.getCurrentUser());
                    Toast.makeText(getActivity(),"Reload successful!",Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "reload", task.getException());
                    Toast.makeText(getActivity(),"Failed to reload user.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        common.updateUI(user,getContext(),mAuth);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.verify: sendEmailVerification();
                verify.setEnabled(false);
                verify.setBackgroundColor(R.color.dark_grey);
                verify.setTextColor(R.color.dark_grey);
                status_verify.setVisibility(View.GONE);

                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verify.setEnabled(true);
                                status_verify.setVisibility(View.VISIBLE);
                                verify.setText(R.string.resendEval);
                            }
                        });
                    }
                },10000);
        }
    }

    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Verification email sent to " + user.getEmail(),Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(getActivity(),"Failed to send verification email.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}