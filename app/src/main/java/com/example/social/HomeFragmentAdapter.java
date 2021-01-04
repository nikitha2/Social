package com.example.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class HomeFragmentAdapter extends RecyclerView.Adapter {
    ArrayList<String> urlData;
    Context context;
    ListItemClickListener mClickListener;

    public HomeFragmentAdapter(Context context, ArrayList<String> urlData, ListItemClickListener mClickListener) {
        this.urlData=urlData;
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
        View view = layoutInflater.inflate(R.layout.listitems_home, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View currentView = holder.itemView;
        String currentItemAtPos = urlData.get(position);

        ImageView imageView= currentView.findViewById(R.id.recyclerview_grid);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(currentItemAtPos).apply(options).into(imageView);

    }

    @Override
    public int getItemCount() {
        return urlData.size();
    }

    public void setData(ArrayList<String> data) {
        urlData.clear();
        urlData.addAll(data);
        notifyDataSetChanged();
    }
}
