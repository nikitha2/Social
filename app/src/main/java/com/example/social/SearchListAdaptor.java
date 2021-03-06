package com.example.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Map;

import static com.example.social.CommonDb.DISPLAY_IMAGE_URL;
import static com.example.social.CommonDb.DISPLAY_NAME;
import static com.example.social.CommonDb.LOGGEDIN;

public class SearchListAdaptor extends RecyclerView.Adapter {
    ArrayList<Map<String, Object>> list_searchResultsData;
    Context context;
    ListItemClickListener mClickListener;

    public SearchListAdaptor(Context context,  ArrayList<Map<String, Object>> list_searchResults, ListItemClickListener mClickListener) {
        this.list_searchResultsData =list_searchResults;
        this.context=context;
        this.mClickListener=mClickListener;
    }

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        View v;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.v=itemView;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            mClickListener.onListItemClick(pos);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_result_listitem, parent, false);
        return new SearchListAdaptor.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        Map<String, Object> currentItemAtPos = list_searchResultsData.get(position);

        ImageView imageView= currentView.findViewById(R.id.roundedimage_pofile_image_search);
        ImageView avail= currentView.findViewById(R.id.pofile_avail);
        TextView username= currentView.findViewById(R.id.pofile_name);
        TextView posts= currentView.findViewById(R.id.pofile_postsno);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(currentItemAtPos.get(DISPLAY_IMAGE_URL)).apply(options).into(imageView);

        username.setText(currentItemAtPos.get(DISPLAY_NAME).toString());

        if( currentItemAtPos.get(LOGGEDIN) !=null) {   // change condition
            posts.setText(R.string.avail);
            avail.setImageResource(R.drawable.available);
        }
        else {
            posts.setText(R.string.offline);
            avail.setImageResource(R.drawable.unavailable);
        }
    }

    @Override
    public int getItemCount() {
        return list_searchResultsData.size();
    }

    public void setData(ArrayList<Map<String, Object>> data) {
        list_searchResultsData.clear();
        list_searchResultsData.addAll(data);
        notifyDataSetChanged();
    }
}
