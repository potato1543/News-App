package com.example.owner.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.owner.newsapp.model.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GithubAdapter.ItemClickListener {
    static final String TAG = "MainActivity";
    private EditText search;
    private ProgressBar progress;
    private RecyclerView rv;
    private GithubAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.searchQuery);
        progress = (ProgressBar) findViewById(R.id.progressBar);

        GithubAdapter.ItemClickListener adapters = this;
        adapter = new GithubAdapter(adapters);

        rv = (RecyclerView) findViewById(R.id.recyclerview_news);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
            String s = search.getText().toString();
            NetworkTask task = new NetworkTask(s);
            task.execute();
        }
        return true;

    }

    @Override
    public void onClick(NewsItem newsItem) {
        Uri webpage = Uri.parse(newsItem.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        String query;

        NetworkTask(String s) {
            query = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }


        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            ArrayList<NewsItem> result = null;
            //  URL url = NetworkUtils.buildUrl(query, "stars");
            URL url = NetworkUtils.buildUrl("the-next-web", "latest", "bf84b88bcd544f8094c1083036bffaae");
            Log.d(TAG, "Starturl: " + url.toString());
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> data) {
            super.onPostExecute(data);
            progress.setVisibility(View.GONE);
            if (data != null) {
               rv.setAdapter(adapter);
                adapter.setData(data);
            } else {

                Log.d(TAG, "no data");
            }

        }
    }
}