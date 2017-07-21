package com.example.owner.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.newsapp.model.NewsItem;

import java.util.ArrayList;


public class GithubAdapter extends RecyclerView.Adapter<GithubAdapter.ItemHolder> {

    private ArrayList<NewsItem> data;
    private final ItemClickListener listener;

    public GithubAdapter(ItemClickListener listener) {
        // this.data = data;
        this.listener = listener;
    }

    public void setData(ArrayList<NewsItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public interface ItemClickListener {
        void onClick(NewsItem newsItem);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.activity_list_item, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        TextView date;

        ItemHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.time);
            view.setOnClickListener(this);
        }

        public void bind(int pos) {
            NewsItem item = data.get(pos);
            title.setText(item.getTitle());
            description.setText(item.getDescription());
            date.setText(item.getPublishedAt());
        }

        @Override
        public void onClick(View v) {

            NewsItem newsItem;
            newsItem = getClickNewsItem();
            listener.onClick(newsItem);
        }


        private NewsItem getClickNewsItem() {
            int index = getAdapterPosition();
            return data.get(index);
        }
//            String url = item.getUrl();
//            Uri webpage = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);

}

}

