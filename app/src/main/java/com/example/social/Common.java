package com.example.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.MultiFactorResolver;

public class Common  {
    public static final int RESULT_NEEDS_MFA_SIGN_IN = 42;
    public static final String ID = "ID";
    private static final String TAG = Common.class.getSimpleName();
    FirebaseAuth mAuth;
    CommonDb commonDb;
    public void showProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void checkForMultiFactorFailure(Exception e, TextView status, Activity activity) {
        // Multi-factor authentication with SMS is currently only available for
        // Google Cloud Identity Platform projects. For more information:
        // https://cloud.google.com/identity-platform/docs/android/mfa
        status.setVisibility(View.VISIBLE);
        if (e instanceof FirebaseAuthMultiFactorException) {
            Log.w(TAG, "multiFactorFailure", e);
            Intent intent = new Intent();
            MultiFactorResolver resolver = ((FirebaseAuthMultiFactorException) e).getResolver();
            intent.putExtra("EXTRA_MFA_RESOLVER", resolver);
            activity.setResult(RESULT_NEEDS_MFA_SIGN_IN, intent);
            status.setText(resolver.getHints().indexOf(0));
            activity.finish();
        }
        else {
            String message = e.getMessage();
            status.setText(message);
            //finish();
        }
    }

    public void updateUI(FirebaseUser user, Context context,FirebaseAuth mAuth) {
        commonDb=new CommonDb(context,null,null,null,null);
        this.mAuth=mAuth;
        if (user != null) {
            if (user.isEmailVerified()) {
                commonDb.addorUpdateUserToCollectionAndStartNewActivity(new Intent(context, HomeActivity.class));
            }
        }else if(user==null && context!=MainActivity.context){
            Intent intent=new Intent(context,MainActivity.class);
            context.startActivity(intent);
        }
    }

    public void signOut(FirebaseAuth mAuth,Context context) {
        FirebaseAuth.getInstance().signOut();
        updateUI(null,context,mAuth);
        commonDb.logoffUser();
    }


}
