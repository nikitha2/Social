package com.example.social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.social.CommonDb.DISPLAY_NAME;

public class SearchedActivity extends AppCompatActivity {
    Map<String, Object> hashMap;
    private HomeFragmentAdapter homeFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);

        Intent intent = getIntent();
        hashMap = (HashMap<String, Object>)intent.getSerializableExtra(getString(R.string.DOCUMENT_KEY));
        setTitle(hashMap.get(DISPLAY_NAME).toString().split("@")[0]);
        setData();
    }

    private void setData() {
        ImageView roundedimage_pofile_image=findViewById(R.id.roundedimage_pofile_image);
        TextView pofile_postsNums=findViewById(R.id.pofile_postsNums);
        TextView pofile_followersNums=findViewById(R.id.pofile_followersNums);
        TextView pofile_followingNums=findViewById(R.id.pofile_followingNums);
        RecyclerView recyclerView_posts=findViewById(R.id.recyclerView_posts);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(this).load(hashMap.get(CommonDb.DISPLAY_IMAGE_URL)).apply(options).into(roundedimage_pofile_image);

        ArrayList<String> urlData = (ArrayList<String>) hashMap.get(CommonDb.URL);
        pofile_postsNums.setText(String. valueOf(urlData.size()));
        homeFragmentAdapter=new HomeFragmentAdapter(this,urlData, null);
        recyclerView_posts.setAdapter(homeFragmentAdapter);
        recyclerView_posts.setLayoutManager(new GridLayoutManager(this,3));



    }
}