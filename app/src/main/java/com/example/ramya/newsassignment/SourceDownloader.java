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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class SourceDownloader extends AsyncTask<String, Void, String>
{
    String urlToUse = null;
    String yahoo, fb;
    private final String APIKEY = "https://newsapi.org/v1/sources?language=en&country=us&category=";
    String pass;
    Uri datauri = null;
    private final String apikeysrc ="&apiKey=ae498bec395341ce9303d7cb7f7c9acb";
    private MainActivity mact;
    private static final String TAG = "SourceDownloader";
    ArrayList<String> newsrescat1 = new ArrayList<>();
    HashMap<Integer, SourceGetterSetter> map = new HashMap<>();
    ArrayList<SourceGetterSetter> newsreslist = new ArrayList<>();
    ArrayList<String> newsrescat = new ArrayList<>();
    ArrayList uniqueList = new ArrayList();

    public SourceDownloader(MainActivity ma)
    {
        mact = ma;
    }


    @Override
    protected void onPreExecute()
    {
        Toast.makeText(mact, "Loading NewsSource Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String l, s11;
        StringBuilder sb = new StringBuilder();
        yahoo = null;
        fb = null;
        urlToUse = null;
        datauri = null;

        if(params[0].equals("all"))
        {
            datauri = Uri.parse(APIKEY + apikeysrc);
            urlToUse = datauri.toString();
            Log.d(TAG, urlToUse);
        }
        else
        {
            datauri = Uri.parse(APIKEY + params[0] + apikeysrc);
            urlToUse = datauri.toString();
            Log.d(TAG, urlToUse);
        }

        try
        {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader read = new BufferedReader((new InputStreamReader(is)));
            while ((l = read.readLine()) != null)
            {
                sb.append(l).append('\n');
            }
        }
        catch (FileNotFoundException e)
        {
            ArrayList<String> newsrc = new ArrayList<>();
            return String.valueOf(newsrc);
        }
        catch (Exception e)
        {
            ArrayList<String> newsrc = new ArrayList<>();
            return String.valueOf(newsrc);
        }

        ArrayList<SourceGetterSetter> newsrc = parseJSON(sb.toString());
        return String.valueOf(newsrc);
    }

    private ArrayList<SourceGetterSetter> parseJSON(String s)
    {
        String cid = null, cname = null, curl = null, ccat = null;
        try
        {
            JSONObject jr1 = new JSONObject(s);
            JSONArray resp = jr1.getJSONArray("sources");
            Log.d(TAG, "response1 Length: " +resp.length());

            //NewsResource data
            for(int i =0; i<resp.length(); i++)
            {
                cid = null;
                cname = null;
                curl = null;
                ccat = null;
                {
                    JSONObject jb1 = resp.getJSONObject(i);
                    cid = jb1.getString("id");
                    Log.d("[" + i + "]" + "Channelid:", cid);
                    cname = jb1.getString("name");
                    Log.d("[" + i + "]" + "Channelname:", cname);
                    curl = jb1.getString("url");
                    Log.d("[" + i + "]" + "ChannelURL:", curl);
                    ccat = jb1.getString("category");
                    Log.d("[" + i + "]" + "ChannelCategory:", ccat);
                }
                newsreslist.add(new SourceGetterSetter(cid, cname, curl, ccat));
                newsrescat.add(ccat);
            }
            for(int k = 0; k<newsrescat.size(); k++)
            {
                Log.d(TAG, "ResourceList: [" + k + "]" + newsrescat);
            }
            Set<String> hmap = new HashSet<>();
            hmap.addAll(newsrescat);
            newsrescat.clear();
            newsrescat1.addAll(hmap);
//            for(int k = 0; k<newsresourcecategory.size(); k++)
//            {
            Log.d(TAG, "NewResourceList: ["+
                    "]" + newsrescat1);
//            }
            //newsresourcelist.add(new SourceGetterSetter(cid, cname, curl, ccategory));
            for(int i = 0; i<newsreslist.size(); i++)
            {
                Log.d(TAG, "ResourceList: [" + i + "]" + newsreslist.get(i).getId());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsreslist.get(i).getName());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsreslist.get(i).getUrl());
                Log.d(TAG, "ResourceList: [" + i + "]" + newsreslist.get(i).getCategory());
            }

            return newsreslist;

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
        Toast.makeText(mact, "Loading Article Data..", Toast.LENGTH_SHORT).show();
        if(newsreslist.size() > 0)
        {
            mact.setSources(newsreslist, newsrescat1);
        }
    }
}
