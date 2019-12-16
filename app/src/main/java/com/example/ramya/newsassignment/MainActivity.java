package com.example.ramya.newsassignment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.content.res.Configuration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.*;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    private DrawerLayout dlayout;
    private ListView drawlist;
    private List<Fragment> frag;
    private ViewPager vpager;
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    private static int flag2 = 1;
    private ArrayList<String> newsresource1 = new ArrayList<>();
    private ArrayList<SourceGetterSetter> newsrclist = new ArrayList<>();
    private ActionBarDrawerToggle toggledraw;
    private ArrayAdapter listadap;
    private NewsReciever nreceive;
    private Article newsarticle;

    private MyPageAdapter pageadap;
    HashMap hmap = new HashMap();
    private int flag3 = 1;
    private ArrayList<String> listitems = new ArrayList<>();
    private ArrayList<String> newsres = new ArrayList<>();

    String et2;
    String[] catstring = new String[newsres.size()];
    Fragment mContent;
    private static FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //describe drawer layout
        dlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawlist = (ListView) findViewById(R.id.left_drawer);
        String allcat = "all";
        new SourceDownloader(MainActivity.this).execute(String.valueOf(allcat));
        nreceive = new NewsReciever();

        Intent i = new Intent(MainActivity.this, NewsService.class);
        startService(i);

        IntentFilter filter1 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(nreceive, filter1);

        toggledraw = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                dlayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        //mDarawerList
        listadap = new ArrayAdapter<>(this,
                R.layout.drawer_list_item, listitems);
        drawlist.setAdapter(listadap);
        drawlist.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        selectItem(position);
                        Log.d(TAG, listitems.get(position));
                        vpager.setBackground(null);
                        //define intent
                        for(int i = 0; i< newsrclist.size(); i++)
                        {
                            if(listitems.get(position).equals(newsrclist.get(i).getName()))
                            {
                                Intent newintent = new Intent();
                                Log.d("Position", listitems.get(position));
                                newintent.putExtra("myinfo", newsrclist.get(i));
                                //Broadcast the intent
                                newintent.setAction(ACTION_MSG_TO_SERVICE);
                                sendBroadcast(newintent);
                                dlayout.closeDrawer(drawlist);
//                                startService(intent);
                            }
                        }
                    }
                }
        );

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
        }
        //sampleReceiver = new SampleReceiver();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Fragment Declaration
        frag = getFragments();

        pageadap = new MyPageAdapter(getSupportFragmentManager());
        vpager = (ViewPager) findViewById(R.id.viewpager);
        vpager.setBackgroundResource(R.drawable.news);
        vpager.setAdapter(pageadap);

    }

    private void selectItem(int pos)
    {
        Toast.makeText(this, listitems.get(pos), Toast.LENGTH_SHORT).show();
        setTitle(listitems.get(pos));
        dlayout.closeDrawer(drawlist);
    }

    private class NewsReciever extends BroadcastReceiver
    {
        @Override
        //ONRECEIVE method
        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction())
            {
                case ACTION_NEWS_STORY:
                    if (intent.hasExtra("ramya"))
                    {
                        reDoFragments((ArrayList<Article>) intent.getSerializableExtra("ramya"));
                    }
            }

        }
    }

    public void setSources(ArrayList<SourceGetterSetter> newsresourcelist, ArrayList<String> nreslistcat)
    {
        hmap.clear();
        listitems.removeAll(listitems);
        this.newsrclist.removeAll(this.newsrclist);
        this.newsrclist.addAll(newsresourcelist);
        Log.d(TAG, "StockList" + this.newsrclist.toString());
        Log.d(TAG, String.valueOf(listitems.size()));
        Log.d(TAG, String.valueOf(this.newsrclist.size()));
        //clear the list of source name
        nreslistcat.add(0, "all");

        Log.d("flag1", String.valueOf(flag3));
        if(flag3 == 1)
        {
            newsres.removeAll(newsres);
            newsres.addAll(nreslistcat);
            catstring = newsres.toArray(new String[newsres.size()]);
            flag3++;
            Log.d("flag2", String.valueOf(flag3));
        }

        //Fill the list of sources
        for(int k = 0; k< this.newsrclist.size(); k++)
        {
            listitems.add(this.newsrclist.get(k).getName());
            hmap.put(this.newsrclist.get(k).getName(), this.newsrclist.get(k));
        }
        invalidateOptionsMenu();
        Log.d(TAG, String.valueOf(listitems.size()));
//            mDrawerList.setAdapter(new ArrayAdapter<>(this,
//                R.layout.drawer_list_item, items));
        listadap.notifyDataSetChanged();
    }

    private void reDoFragments(ArrayList<Article> art)
    {
        for (int i = 0; i < pageadap.getCount(); i++)
        {
            pageadap.notifyChangeInPosition(i);
        }
        frag.clear();

        for (int f = 0; f < art.size(); f++)
        {
            Log.d("DEEP", art.get(f).getTitle());
            //newsadapter.notifyChangeInPosition(i);
            frag.add(ArticleFragment.newInstance(art.get(f).getTitle(), art.get(f).getUrlToImage(), art.get(f).getAuthor(), art.get(f).getDescription(), art.get(f).getPublishedAt(), art.get(f).getUrl(), " Page " + (f+1) + " of" + art.size()));
//            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment_f, "fragment1").commit();
        }

        pageadap.notifyDataSetChanged();
        vpager.setCurrentItem(0);

    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggledraw.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        toggledraw.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG,"onPrepareOptionsMenu "+ catstring.length);
        menu.clear();
        if(catstring.length != 0)
        {
            for (int i = 0; i < catstring.length; i++)
            {
                menu.add(R.menu.action_menu, Menu.NONE, 0, catstring[i]);
            }
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (toggledraw.onOptionsItemSelected(item))
        {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }


        Log.d("item", String.valueOf(item));
        new SourceDownloader(MainActivity.this).execute(String.valueOf(item));
        return true;
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return frag.get(position);
        }

        @Override
        public int getCount() {
            return frag.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        //fragment
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }

    //Unregister service
    @Override
    protected void onDestroy()
    {
        unregisterReceiver(nreceive);
        Intent i = new Intent(MainActivity.this, NewsService.class);
        stopService(i);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        listitems.addAll(savedInstanceState.getStringArrayList("HISTORY"));
        newsres.addAll(savedInstanceState.getStringArrayList("HISTORY1"));
        // FragmentTransaction transaction = fragmentManager.beginTransaction();
//        fragments = (List<Fragment>) fragmentManager.getFragment(savedInstanceState, "myFragment");
//        mContent.getFragmentManager().getFragment(savedInstanceState, "myFragmentName");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("HISTORY",listitems);
        outState.putStringArrayList("HISTORY1",newsres);
//        if(fragments != null) {
//            fragmentManager.putFragment(outState, "myFragment", fragments);
//        }
//        mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
//        getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
//        outState.putStringArray("HISTORY2",categoryStringarray);
    }
}


