package com.example.owner.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList>, GithubAdapter.ItemClickListener {
    static final String TAG = "MainActivity";
    private EditText search;
    private ProgressBar progress;
    private RecyclerView rv;
    private GithubAdapter adapter;

    //uniquely identify the loader
    private static final int NEWS_LOADER = 1;


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

        // this called the loading manager
        loadThem();
        // intializes all the values in the asyncloader
        getSupportLoaderManager().initLoader(NEWS_LOADER, null, this);

    }

    // this methods loads the data if not all ready loaded
    private void loadThem() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
        adapter.notifyDataSetChanged();
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
            loadThem();

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

    // AsyncTaskLoader
    // Loader is the same as Task it just runs the program better

    @Override
    public Loader<ArrayList> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList>(this) {

            // same as Preexecute will check the args for info
            @Override
            protected void onStartLoading() {
                if (args != null) {
                    Log.d(TAG, "no new info ");
                }
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            // run the json in the background and make sure every matches before it is displays in PostExecute
            @Override
            public ArrayList loadInBackground() {
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
        };
    }


    // OnLoadFinished works the same way as postExecute did. Displays the json
    @Override
    public void onLoadFinished(Loader<ArrayList> loader,ArrayList data) {
        progress.setVisibility(View.GONE);
        if (data != null) {
            rv.setAdapter(adapter);
            adapter.setData(data);
        } else {

            Log.d(TAG, "no data");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader) {

    }

}