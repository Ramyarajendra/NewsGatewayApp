package com.example.ramya.newsassignment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import static com.example.ramya.newsassignment.MainActivity.ACTION_MSG_TO_SERVICE;
import static com.example.ramya.newsassignment.MainActivity.ACTION_NEWS_STORY;


public class NewsService extends Service {

    private ArrayList<Article> strylis = new ArrayList<>();
    private static final String TAG = "NewsService";
    private boolean run = true;
    private ServiceReciever servrec;
    public SourceGetterSetter srcname;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ramya", "new Intent service started:");

        servrec = new ServiceReciever();

        IntentFilter fil = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(servrec, fil);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        while (strylis.size() == 0) {

                            Thread.sleep(250);
                        }

                        Intent i = new Intent();
                        i.setAction(ACTION_NEWS_STORY);
                        i.putExtra("ramya", strylis);
                        sendBroadcast(i);
                        strylis.removeAll(strylis);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        return Service.START_STICKY;
    }

    private class ServiceReciever extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent i)
        {
            switch (i.getAction()) {
                case ACTION_MSG_TO_SERVICE:
                    if (i.hasExtra("myinfo"))
                    {
                        srcname = (SourceGetterSetter) i.getSerializableExtra("myinfo");
                        new NewsDownloader(NewsService.this, srcname.getId()).execute();
                    }
            }

        }
    }

    public void setArticles(ArrayList<Article> artlist)
    {
        strylis.clear();
        strylis.addAll(artlist);
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(servrec);
        Intent i = new Intent(NewsService.this, MainActivity.class);
        stopService(i);
        super.onDestroy();
    }
}



