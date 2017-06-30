package com.example.owner.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.newsapp.model.NewsItem;

import java.util.ArrayList;

/**
 * Created by Owner on 6/29/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private String mNewsData[];

    private static ArrayList<NewsItem> data;
    static ItemClickListener listener;



    public NewsAdapter(ArrayList<NewsItem> data, ItemClickListener listener){
        this.data = data;
        this.listener = listener;
    }

    public interface ItemClickListener{
        void onItemClick(int clickedItemIndex);
    }

    public NewsAdapter() {

    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListWeather = R.layout.activity_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListWeather, parent, shouldAttachToParentImmediately);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mNewsData) return 0;
        return mNewsData.length;
    }


    public static class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         TextView mNewsTextView;
         TextView mNewsTextView1;
        TextView date;


        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            mNewsTextView = (TextView) itemView.findViewById(R.id.description);
            mNewsTextView1 = (TextView) itemView.findViewById(R.id.title);
            date = (TextView)itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        public void bind(int pos){
            NewsItem repo = data.get(pos);
            mNewsTextView.setText(repo.getTitle());
            mNewsTextView1.setText(repo.getDescription());
            date.setText(repo.getPublishedAt());
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        }
    }

}
