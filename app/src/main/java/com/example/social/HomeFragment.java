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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class HomeFragment extends Fragment implements View.OnClickListener, CommonDb.AddUserToCollectionSuccessCallback, CommonDb.GetAllUsersCallBack, HomeFragmentAdapter.ListItemClickListener, CommonDb.DisplayImageUrlSuccessCallback {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private static final String REQUEST_CODE = "1";
    private static final int PICK_IMAGE_ID = 1; // the number doesn't matter
    private static final int PICK_IMAGE_ID_FOR_PROFILE_PIC = 3; // the number doesn't matter
    private static final int SHARE_IMG_RESULT_CODE=2;
    CommonDb commonDb;
    CallbackFromHomeFragment callbackFromHomeFragment;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Common common;
    private Context context;
    View rootView;
    RecyclerView posts;
    TextView pofile_postsNums;
    ImageView profilePic;
    private HomeFragmentAdapter homeFragmentAdapter;

    public HomeFragment() {
        common=new Common();
       // this.callbackFromHomeFragment=callbackFromHomeFragment;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        rootView= inflater.inflate(R.layout.fragment_home, container, false);
        posts=rootView.findViewById(R.id.recyclerView_posts);
        pofile_postsNums=rootView.findViewById(R.id.pofile_postsNums);
        profilePic=rootView.findViewById(R.id.roundedimage_pofile_image);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionsAndOpenGallery(PICK_IMAGE_ID_FOR_PROFILE_PIC);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        commonDb = new CommonDb(context, this, this,this);
        commonDb.getAllPostsForUser();
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
        inflater.inflate(R.menu.menu2, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.signout){
            common.signOut(common.mAuth,context);
            return true;
        }
        if (id == R.id.add){
            requestPermissionsAndOpenGallery(PICK_IMAGE_ID);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static final int PERMISSION_REQUEST_ACTIVITY_ACCESS_FINE_LOCATION_ALREADYGRANTED = 2;
    public static final int PERMISSION_REQUEST_ACTIVITY_ACCESS_FINE_LOCATION_GRANTED_NOW = 1;
    private int requestPermissionsAndOpenGallery(int id) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            openGallery(id);
            return PERMISSION_REQUEST_ACTIVITY_ACCESS_FINE_LOCATION_ALREADYGRANTED;
        } if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted and the user would benefit from additional context for the use of the permission.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, id);
        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, id);
        }
        return id;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    openGallery(PICK_IMAGE_ID);
                }  else {
                    // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to system settings in an effort to convince the user to change their decision.
                }
                return;
            case PICK_IMAGE_ID_FOR_PROFILE_PIC:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    openGallery(PICK_IMAGE_ID_FOR_PROFILE_PIC);
                }  else {
                    // Explain to the user that the feature is unavailable because the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to system settings in an effort to convince the user to change their decision.
                }
                return;
        }
    }

    private void openGallery(int id) {
        Intent chooseImageIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooseImageIntent, id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(context, resultCode, data);
                Intent intent = new Intent(context,SelectImageFromGalleryActivity.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                intent.putExtra("picture", byteArray);
                startActivity(intent);
                break;

             case SHARE_IMG_RESULT_CODE:
                 Log.d(TAG,"BACK TO HOME FRAGMENT FROM SelectImageFromGalleryActivity");break;
             case PICK_IMAGE_ID_FOR_PROFILE_PIC:
                 bitmap = ImagePicker.getImageFromResult(context, resultCode, data);
                 stream = new ByteArrayOutputStream();
                 bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                 byteArray = stream.toByteArray();

                 FirebaseUser currentUser = mAuth.getCurrentUser();
                 String path=currentUser.getUid()+"/ProfilePic/"+ UUID.randomUUID()+".png";
                 StorageMetadata metadata= new StorageMetadata.Builder().setCustomMetadata("uid",currentUser.getUid()).build();
                 commonDb.uploadImageToStorage(TASK_PROFILE_PIC_URL,byteArray,null, path, metadata,null,null);
                 //commonDb.updateDocument(null,);
//                 processTheImage(byteArray);
                 break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void processTheImage(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        profilePic.setImageBitmap(bmp);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void getAllUsersCallBack(Map<String, Object> data) {
        ArrayList<String> urlData = (ArrayList<String>) data.get(CommonDb.URL);
        pofile_postsNums.setText(String. valueOf(urlData.size()));
        homeFragmentAdapter=new HomeFragmentAdapter(context,urlData, this);
        posts.setAdapter(homeFragmentAdapter);
        posts.setLayoutManager(new GridLayoutManager(context,3));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(data.get(CommonDb.DISPLAY_IMAGE_URL)).apply(options).into(profilePic);
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(context,"CLICKED ON POSITION: "+position,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayImageUrlSuccessCallbackOnSuccess() {
        commonDb.getAllPostsForUser();
    }


    interface CallbackFromHomeFragment{
        public void callback(byte[] byteArray);
    }
    @Override
    public void onClick(View v) {

    }
}