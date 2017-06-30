package com.example.owner.newsapp;

import android.net.Uri;
import android.util.Log;

import com.example.owner.newsapp.model.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 6/19/2017.
 */

public class NetworkUtils {

    public static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=bf84b88bcd544f8094c1083036bffaae";
    public static final String PARAM_QUERY = "q";
    public static final String PARAM_SORT = "sort";

    public static URL makeURL(String searchQuery, String sortBy) {
        Uri builtUri = Uri.parse(NEWS_BASE_URL).buildUpon().appendQueryParameter(PARAM_QUERY, searchQuery).appendQueryParameter(PARAM_SORT, sortBy).build();

        URL url = null;
        try {
            String urlString = builtUri.toString();
            Log.d(TAG, "Url: " + urlString);
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray items = main.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String name = item.getString("author");
            JSONObject owner = item.getJSONObject("owner");
            String ownerName = owner.getString("title");
            String url = item.getString("url");
            String descript = item.getString("description");
            String url1 = item.getString("urlToImage");
            String publish = item.getString("publishedAt");
            NewsItem news = new NewsItem(name, ownerName, url, descript, url1, publish);
            result.add(news);
        }
        return result;

    }
}