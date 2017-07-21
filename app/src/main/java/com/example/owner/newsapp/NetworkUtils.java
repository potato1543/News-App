package com.example.owner.newsapp;

import android.net.Uri;

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

/**
 * Created by Owner on 6/19/2017.
 */

public class NetworkUtils {

    //?source=the-next-web&sortBy=latest&apiKey=bf84b88bcd544f8094c1083036bffaae
    public static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles";
    public static final String source_parameter = "source";
    public static final String sortBy_parameter = "sortBy";
    public static final String apiKey_parameter = "apiKey";


    public static URL buildUrl(String locationQuery, String sortBy, String apiKey) {

        Uri uri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(source_parameter, locationQuery)
                .appendQueryParameter(sortBy_parameter, sortBy)
                .appendQueryParameter(apiKey_parameter, apiKey).build();

        URL url = null;
        try {
            url = new URL(uri.toString());
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
            scanner.useDelimiter("//A");
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
        JSONArray articles = main.getJSONArray("articles");

        for (int i = 0; i < articles.length(); i++) {
            JSONObject article = articles.getJSONObject(i);
            String author = article.getString("author");
            String title = article.getString("title");
            String description = article.getString("description");
            String url = article.getString("url");
            String urlToImage = article.getString("urlToImage");
            String publishedAt = article.getString("publishedAt");
            NewsItem item = new NewsItem(author, title, description, url, urlToImage, publishedAt);
            result.add(item);
        }
        return result;
    }
}