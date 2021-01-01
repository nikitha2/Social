package com.example.social;

import android.app.DownloadManager;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.example.social.CommonDb.DISPLAY_NAME;

public class SearchableActivity extends AppCompatActivity implements SearchListAdaptor.ListItemClickListener {
    private static final String TAG = SearchableActivity.class.getSimpleName();
    ArrayList<Map<String, Object>> list_searchResults;
    private static FirebaseFirestore db ;
    private static CollectionReference mDocRef;
    SearchableActivity context;


    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        context = this;
        setTitle(R.string.app_name);
        recyclerView=findViewById(R.id.search_list);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }


    private void doMySearch(String query) {
        list_searchResults=new ArrayList<>();
        SearchListAdaptor firebaseRecyclerAdapter=new SearchListAdaptor(this,list_searchResults,this);
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        db = FirebaseFirestore.getInstance();
        mDocRef = db.collection("users");
        Query firebaseSearchQuery= mDocRef.whereGreaterThanOrEqualTo("DisplayName", query);
        firebaseSearchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot result = task.getResult();
                    int size = result.getDocuments().size();
                    if (!result.getDocuments().isEmpty()) {
                        list_searchResults.clear();
                        for( DocumentSnapshot document: result.getDocuments()){
                            if(document.get(DISPLAY_NAME).toString().contains(query))
                                 list_searchResults.add(document.getData());
                        }
                    }
                    
                }else{

                }
            }
        });


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onListItemClick(int position) {

    }
}