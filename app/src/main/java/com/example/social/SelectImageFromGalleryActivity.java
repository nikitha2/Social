package com.example.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;
import java.util.zip.Inflater;

import static com.example.social.CommonDb.TASK_ADD_URL;

public class SelectImageFromGalleryActivity extends AppCompatActivity {
    ImageView previewImg;
    byte[] byteArrayOfImage;
    FirebaseStorage storage ;
    Context context;
    FirebaseUser currentUser;
    ProgressBar progressBar;
    public static FirebaseAuth mAuth;
    private static final int PICK_IMAGE_ID = 1;
    CommonDb commonDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_from_gallery);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        previewImg=findViewById(R.id.preview_img);
        progressBar=findViewById(R.id.progressBar_preview_img);
        storage = FirebaseStorage.getInstance();
        commonDb=new CommonDb(context,null,null,null);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            byte[] byteArray = extras.getByteArray("picture");
            processTheImage(byteArray);
        }
    }


    private void processTheImage(byte[] byteArray) {
        byteArrayOfImage=byteArray;
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        previewImg.setImageBitmap(bmp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share:
                //TODO
                // upload to storage db and go back to homeFragment
                final String[] url = new String[1];
                item.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                String path=currentUser.getUid()+"/"+ UUID.randomUUID()+".png";
                StorageMetadata metadata= new StorageMetadata.Builder().setCustomMetadata("uid",currentUser.getUid()).build();
                commonDb.uploadImageToStorage(TASK_ADD_URL,byteArrayOfImage,item, path, metadata,progressBar,new Intent(context, HomeActivity.class));
                return true;
            case android.R.id.home:
               Intent chooseImageIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
               startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

            default:  return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                processTheImage(byteArray);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

}