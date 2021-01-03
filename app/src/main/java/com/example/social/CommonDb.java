package com.example.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonDb {
    public static final String UID = "Uid";
    public static final String TASK_ADD_URL = "TASK_ADD_URL";
    public static final String TASK_PROFILE_PIC_URL = "TASK_PROFILE_PIC_URL";

    private static final String TAG = CommonDb.class.getSimpleName();
    public static final String EMAIL_ID = "EmailID";
    public static final String DISPLAY_NAME = "DisplayName";
    public static final String DISPLAY_IMAGE_URL = "DISPLAY_IMAGE_URL";
    public static final String TIMESTAMP = "timestamp";
    public static final String URL = "URL";
    public static final String LOGGEDIN = "loggedIn";
    FirebaseStorage storage ;
    private static FirebaseFirestore db ;
    private static DocumentReference mDocRef;
    AddUserToCollectionSuccessCallback addUserToCollectionSuccessCallback;
    GetAllUsersCallBack  getAllUsersCallBack;
    public static FirebaseAuth mAuth;
    FirebaseUser firebaseUseruser;
    GetQueryResultsCallBack getQueryResultsCallBack;
    DisplayImageUrlSuccessCallback displayImageUrlSuccessCallback;
    Context context;
    public CommonDb(Context context, AddUserToCollectionSuccessCallback addUserToCollectionSuccessCallback, GetAllUsersCallBack getAllUsersCallBack, DisplayImageUrlSuccessCallback displayImageUrlSuccessCallback,GetQueryResultsCallBack getQueryResultsCallBack) {
        this.addUserToCollectionSuccessCallback = addUserToCollectionSuccessCallback;
        this.getAllUsersCallBack=getAllUsersCallBack;
        this.context=context;
        this.displayImageUrlSuccessCallback=displayImageUrlSuccessCallback;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        this.firebaseUseruser = mAuth.getCurrentUser();
        this.getQueryResultsCallBack=getQueryResultsCallBack;
    }


    interface AddUserToCollectionSuccessCallback{
        public void onSuccess();
    }
    public  void addorUpdateUserToCollectionAndStartNewActivity(/*FirebaseUser firebaseUseruser, Context context,*/ Intent homeIntent) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put(EMAIL_ID, firebaseUseruser.getEmail());
        user.put(UID, firebaseUseruser.getUid());
        user.put(DISPLAY_NAME, firebaseUseruser.getEmail().split("@")[0]);
        user.put(LOGGEDIN, true);
        user.put(TIMESTAMP, FieldValue.serverTimestamp());

        // Add a new document with a generated ID
        mDocRef = db.document("users/" + firebaseUseruser.getUid());

        mDocRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<String> urlList=new ArrayList<>();
                    String displayImageUrl = "";
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        urlList = (ArrayList<String>) data.get(URL);
                        displayImageUrl = (String) data.get(DISPLAY_IMAGE_URL);
                    }
                    user.put(URL, urlList);
                    user.put(DISPLAY_IMAGE_URL, displayImageUrl);
                    addOrUpdateDocument(user, homeIntent);
                }else{
                    user.put(URL, new ArrayList<String>());
                    user.put(DISPLAY_IMAGE_URL, "");
                    addOrUpdateDocument(user, homeIntent);
                }
            }
        });

    }

    private void addOrUpdateDocument(Map<String, Object> user, Intent homeIntent) {
        mDocRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + firebaseUseruser.getUid());

                if (homeIntent != null) {
                    homeIntent.putExtra(context.getString(R.string.id), firebaseUseruser.getUid());
                    context.startActivity(homeIntent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public  void updateDocument( String urlValue, String profilePicUrlValue,Intent homeIntent) {
        // [START update_document]
        final Task<Void>[] mDocRefUpdate = new Task[]{null};
        mDocRef = db.document("users/" + firebaseUseruser.getUid());

        // Set the "isCapital" field of the city 'DC'
        if (urlValue != null) {
            mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Map<String, Object> data = document.getData();
                            ArrayList<String> urlList = (ArrayList<String>) data.get(URL);
                            urlList.add(urlValue);
                            mDocRef.update(URL, urlList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated! updateDocument urlValue");
                                    if (homeIntent != null) {
                                        homeIntent.putExtra(context.getString(R.string.id), firebaseUseruser.getUid());
                                        context.startActivity(homeIntent);
                                    }
                                }
                            });

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        if (profilePicUrlValue != null) {
            mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                             mDocRef.update(DISPLAY_IMAGE_URL, profilePicUrlValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated! updateDocument profilePicUrlValue");
                                    displayImageUrlSuccessCallback.displayImageUrlSuccessCallbackOnSuccess();
                                }
                            });

                        }
                    }
                }
            });
        }

    }

    public void logoffUser() {
        boolean loggedin;
        if(firebaseUseruser ==null){
            loggedin=false;
        }else{
            loggedin=true;
        }
            mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            boolean log;
                            mDocRef.update(LOGGEDIN, loggedin).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated! updateDocument profilePicUrlValue");
                                }
                            });

                        }
                    }
                }
            });
    }

    interface DisplayImageUrlSuccessCallback{
        public void displayImageUrlSuccessCallbackOnSuccess();
    }
    public void getAllPostsForUser() {
        // [START get_all_users]
        String authuid = mAuth.getUid();
        String userUid =firebaseUseruser.getUid();
        mDocRef = db.document("users/" + userUid);
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Map<String, Object> data = document.getData();
                                if(data.get(UID).equals(userUid)){
                                    getAllUsersCallBack.getAllUsersCallBack(data);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // [END get_all_users]
    }

    interface GetAllUsersCallBack {
        public void getAllUsersCallBack(Map<String, Object> data);
    }

    public void uploadImageToStorage(String task,byte[] byteArrayOfImage, MenuItem item,  String path, StorageMetadata metadata, ProgressBar progressBar,Intent intent) {
        final String[] url = new String[1];
        storage = FirebaseStorage.getInstance();
        StorageReference firebaseStorageRef = storage.getReference(path);
        UploadTask uploadTask=firebaseStorageRef.putBytes(byteArrayOfImage,metadata);
        uploadTask.addOnSuccessListener((Activity) context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(item!=null){
                    item.setEnabled(true);
                }
                if(progressBar!=null) {
                    progressBar.setVisibility(View.GONE);
                }

                firebaseStorageRef.getDownloadUrl().addOnSuccessListener((Activity) context, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        url[0] = String.valueOf(uri);
                        if(TASK_ADD_URL.equals(task)) {
                            updateDocument(url[0], null, intent );
                        }else if(TASK_PROFILE_PIC_URL.equals(task)){
                            updateDocument(null, url[0], null);
                        }
                    }
                });
            }
        }).addOnFailureListener((Activity) context, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(item!=null) {
                    item.setEnabled(true);
                }
                if(progressBar!=null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


    public void getResultsForQueryFromFirestoreDB(String query, SearchListAdaptor firebaseRecyclerAdapter) {
        ArrayList<Map<String, Object>> list_searchResults = new ArrayList<>();
        CollectionReference mDocRef1 = db.collection("users");
        Query firebaseSearchQuery= mDocRef1.whereGreaterThanOrEqualTo("DisplayName", query);
        firebaseSearchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    int size = result.getDocuments().size();
                    if (!result.getDocuments().isEmpty()) {
//                        list_searchResults.clear();
                        for( DocumentSnapshot document: result.getDocuments()){
                            if(document.get(DISPLAY_NAME).toString().contains(query))
                                list_searchResults.add(document.getData());
                        }
                    }
                    getQueryResultsCallBack.getQueryResultsCallBack(list_searchResults);
                    //firebaseRecyclerAdapter.setData(list_searchResults);
                }else{

                }
            }
        });
    }

    interface GetQueryResultsCallBack {
        public void getQueryResultsCallBack(ArrayList<Map<String, Object>> data);
    }
}
