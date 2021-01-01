package com.example.social;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageMetadata;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static com.example.social.CommonDb.TASK_PROFILE_PIC_URL;


public class SearchFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final String REQUEST_CODE = "1";
    private static final int PICK_IMAGE_ID = 1; // the number doesn't matter
    private static final int PICK_IMAGE_ID_FOR_PROFILE_PIC = 3; // the number doesn't matter
    private static final int SHARE_IMG_RESULT_CODE=2;
    CommonDb commonDb;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Common common;
    private Context context;
    View rootView;
    RecyclerView posts;
    TextView pofile_postsNums;
    ImageView profilePic;
    private HomeFragmentAdapter homeFragmentAdapter;

    public SearchFragment() {
        common=new Common();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootView= inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        commonDb = new CommonDb(context, null, null,null);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu4, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.signout){
            common.signOut(common.mAuth,context);
            return true;
        }
        if(id==R.string.search){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}