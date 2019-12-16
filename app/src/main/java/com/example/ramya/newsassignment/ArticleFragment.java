package com.example.ramya.newsassignment;


import android.content.Intent;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArticleFragment extends Fragment
{
    final String TAG = "ArticleFragment";
    public static final String a = "a";
    public static final String b = "b";
    private TextView authtxt, title, desc, pageno;
    private ImageView img;
    public static final String c = "c";
    public static final String d = "d";
    String datefinal;
    public static final String e = "e";
    public static final String f = "f";
    public static final String g = "g";
    String d2 = "";
    NetworkInfo activeNetworkInfo;
    Fragment frag;

    public static final ArticleFragment newInstance(String message, String message1, String message2, String message3, String message4, String message5, String message6)
    {
        ArticleFragment f = new ArticleFragment();
        Bundle bun = new Bundle(6);
        if(message != null) {
            bun.putString(a, message);
        }
        if(message1 != null) {
            bun.putString(b, message1);
        }
        if(message2 != null) {
            bun.putString(c, message2);
        }
        if(message3 != null) {
            bun.putString(d, message3);
        }
        if(message4 != null) {
            bun.putString(e, message4);
        }
        if(message5 != null) {
            bun.putString(ArticleFragment.f, message5);
        }
        if(message6 != null) {
            bun.putString(g, message6);}
        f.setArguments(bun);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        onRestoreInstanceStae(savedInstanceState);
        final String msg = getArguments().getString(a);;
        String msg1 = getArguments().getString(b);
        String msg2 = getArguments().getString(c);
        String msg3 = getArguments().getString(d);
        String msg4 = getArguments().getString(e);
        final String msg5 = getArguments().getString(f);
        String msg6 = getArguments().getString(g);
        msg.trim();
        msg1.trim();
        msg2.trim();
        msg3.trim();
        msg6.trim();

        setRetainInstance(true);
        View v = inflater.inflate(R.layout.myfragment_layout, container, false);
        authtxt = (TextView)v.findViewById(R.id.Authorname);
        img = (ImageView) v.findViewById(R.id.positionphoto);
        title = (TextView)v.findViewById(R.id.Title);
        desc = (TextView)v.findViewById(R.id.Description);
        pageno = (TextView)v.findViewById(R.id.pageno);

        try {
            SimpleDateFormat dateform = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
            Date createdate = dateform.parse(msg4.replaceAll("Z$", "+0000"));

            dateform = new SimpleDateFormat("EEE MMM dd HH:mm", Locale.getDefault());
            String newDate = dateform.format(createdate);

            String [] d1 = newDate.split(" ");
            datefinal = d1[1]+" "+ d1[2]+", 2017 "+d1[3];
            d2 = datefinal;
            Log.d("Final Date:", "Final Date ==> "+datefinal);
//            dateSet(finalDate);
        }catch(Exception pe) {
            Log.d(TAG, "onCreateView: Inside exception");
        }


        Log.d("message2", msg2.trim());
        if(msg2.trim()!= null && datefinal!= null)
        {
            authtxt.setText(datefinal + "\n\n\n" +msg2);
        }
        else if(msg2.trim() == null && datefinal!= null)
        {
            authtxt.setText(datefinal);
        }
        else if(msg2.trim()!= null && datefinal== null)
        {
            authtxt.setText("");
        }
        else if (msg2.trim()== null && datefinal== null)
        {
            authtxt.setText("No Information Available!");
        }
        pageno.setText(msg6);


        Log.d("imageview", msg1);
        if (msg1 != null) {

            final String imgurl = msg1;
            Log.d("ImageUrl:", msg1);
            Log.d("1", "1");

            Picasso pica = new Picasso.Builder(this.getContext()).listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                { // Here we try https if the http image attempt failed
                    final String urlchng = imgurl.replace("http:", "https:");
                    picasso.load(urlchng).error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder).into(img);
                }
            }).build();
            pica.load(imgurl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Uri uri = Uri.parse(msg5);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });
        }
        if(!msg.equals(null))
        {
            title.setText(msg);
            title.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(msg5);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });
//
        }

        else {
            title.setText("No Information Available!");
        }
        if(!msg3.equals(null)) {
            desc.setText(msg3);
            desc.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Uri uri = Uri.parse(msg5);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
            });
        }
        else {
            desc.setText("No Information Available!");
        }
        return v;
    }

    private void onRestoreInstanceStae(Bundle savedInstanceState)
    {
        if(savedInstanceState!=null)
        {
//            String authorname1 = savedInstanceState.getString("author");
            title.setText(savedInstanceState.getString("title"));
            desc.setText(savedInstanceState.getString("description"));
            pageno.setText(savedInstanceState.getString("pageno"));
            authtxt.setText(savedInstanceState.getString("author"));
            FragmentManager mgr = getFragmentManager();
            FragmentTransaction transaction = mgr.beginTransaction();
            frag = (ArticleFragment) mgr.getFragment(savedInstanceState, "myFragment");
//            image.getImageAlpha()
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        FragmentManager mgr = getFragmentManager();
        if(frag != null) {
            mgr.putFragment(outState, "myFragment", frag);
        }
        outState.putString("author", authtxt.getText().toString());
        outState.putString("title", title.getText().toString());
//        outState.putString("image", image.getImageAlpha());
        outState.putString("description", desc.getText().toString());
        outState.putString("pageno", pageno.getText().toString());
    }
}