package com.example.ramya.newsassignment;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class NewsDownloader extends AsyncTask<String, Void, String>
{
    String str1, str2;
    String id1;
    Uri datagen = null;
    private NewsService newsserv;
    private final String apikey ="&apiKey=ae498bec395341ce9303d7cb7f7c9acb";
    private static final String TAG = "NewsDownloader";
    String urlToUse = null;
    ArrayList<Article> artdata = new ArrayList<>();
    private final String artURL = "https://newsapi.org/v1/articles?source=";



    public NewsDownloader(NewsService ma, String id)
    {
        newsserv = ma;
        id1 = id;
    }

    @Override
    protected void onPreExecute()
    {
        Toast.makeText(newsserv, "Loading NewsArticle Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        str1 = null;
        str2 = null;
        urlToUse = null;
        datagen = null;
        datagen = Uri.parse(artURL + id1 + apikey);
        urlToUse = datagen.toString();
        Log.d(TAG, urlToUse);
        StringBuilder sb = new StringBuilder();
        String line, s11;
        try
        {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader read = new BufferedReader((new InputStreamReader(is)));
            while ((line = read.readLine()) != null)
            {
                sb.append(line).append('\n');
            }
            Log.d(TAG, "SB string: " + sb.toString());
        }
        catch (FileNotFoundException e)
        {
            ArrayList<String> newssrc = new ArrayList<>();
            Log.e(TAG, "DoException: ", e);
            return String.valueOf(newssrc);
        }
        catch (Exception e)
        {
            ArrayList<String> newssrc = new ArrayList<>();
            Log.e(TAG, "DoException: ", e);
            return String.valueOf(newssrc);
        }
        ArrayList<Article> newsart = parseJSON(sb.toString());
        return String.valueOf(newsart);
    }

    private ArrayList<Article> parseJSON(String s)
    {
        Log.d("00000", s);
        String auth = null, title = null, desc = null, urlimg = null, publish = null, url = null;
        try
        {
            JSONObject jr1 = new JSONObject(s);
            JSONArray resp = jr1.getJSONArray("articles");
            Log.d(TAG, "Article Length: " +resp.length());

            for(int i =0; i<resp.length(); i++)
            {
//                author = null;
//                title = null;
//                description = null;
//                urlToImage = null;
//                publishedAt = null;
//                url = null;
                {
                    JSONObject job = resp.getJSONObject(i);
                    auth = job.getString("author");
                    Log.d("[" + i + "]" + "Author:", auth);
                    title = job.getString("title");
                    Log.d("[" + i + "]" + "Title:", title);
                    desc = job.getString("description");
                    Log.d("[" + i + "]" + "Description:", desc);
                    urlimg = job.getString("urlToImage");
                    Log.d("[" + i + "]" + "urlToImage:", urlimg);
                    publish = job.getString("publishedAt");
                    Log.d("[" + i + "]" + "PublishedAt:", publish);
                    url = job.getString("url");
                    Log.d("[" + i + "]" + "url:", url);
                }
                artdata.add(new Article(auth, title, desc, urlimg, publish, url));
            }
            for(int k = 0; k< artdata.size(); k++)
            {
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getAuthor());
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getTitle());
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getDescription());
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getUrlToImage());
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getPublishedAt());
                Log.d(TAG, "ResourceList: [" + k + "]" + artdata.get(k).getUrl());
            }
            return artdata;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        if(artdata.size() > 0)
        {
            newsserv.setArticles(artdata);
        }
    }
}
