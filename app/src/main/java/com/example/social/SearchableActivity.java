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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.social.CommonDb.DISPLAY_NAME;

public class SearchableActivity extends AppCompatActivity implements SearchListAdaptor.ListItemClickListener, CommonDb.GetQueryResultsCallBack {
    private static final String TAG = SearchableActivity.class.getSimpleName();
    ArrayList<Map<String, Object>> list_searchResults;
    private static FirebaseFirestore db ;
    private static CollectionReference mDocRef;
    SearchableActivity context;
    CommonDb commonDb;
    SearchListAdaptor firebaseRecyclerAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        setTitle("SearchableActivity");
        context = this;
        commonDb=new CommonDb(this,null,null,null,this);
        setTitle(getString(R.string.resultsForSearch));
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
        firebaseRecyclerAdapter=new SearchListAdaptor(this,list_searchResults,this);
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonDb.getResultsForQueryFromFirestoreDB(query, firebaseRecyclerAdapter);


    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onListItemClick(int position) {
        Map<String, Object> document = list_searchResults.get(position);
        Intent intent = new Intent(this, SearchedActivity.class);
        intent.putExtra(getString(R.string.DOCUMENT_KEY), (Serializable) document);
        startActivity(intent);
    }

    @Override
    public void getQueryResultsCallBack(ArrayList<Map<String, Object>> data) {
        firebaseRecyclerAdapter.setData( data);
    }
}