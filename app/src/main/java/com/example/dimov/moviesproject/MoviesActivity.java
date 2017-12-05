package com.example.dimov.moviesproject;

import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MoviesActivity extends AppCompatActivity {
    private String ENDPOINT = "";
    String u="";
    ResponseReceiver responseReceiver;

    MovieAdapter myAdapter;
    RecyclerView myRV;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setUI();
       // getMoviesFromDB();

        handleSearch();
    }

    protected void onResume () {
        super.onResume();
        IntentFilter broadcastFilter = new IntentFilter(ResponseReceiver.LOCAL_ACTION);
        responseReceiver = new ResponseReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(responseReceiver,broadcastFilter);
    }

    protected void onPause () {
        super.onPause();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(responseReceiver);
    }

    private void handleSearch() {

        //get search icon reference
        ImageView iv = (ImageView) findViewById(R.id.searchButton);

        iv.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                u = ((EditText) findViewById(R.id.toolbar_search)).getText().toString();
                u=u.replace(" ","+");
                ENDPOINT = "http://www.omdbapi.com/?s="+u+"&apikey=67eb4522";
                loadInService();
            }
        });
    }


    private void setUI() {
        setContentView(R.layout.activity_movies_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

//    private void getMoviesFromDB() {
//        try {
//            List<MovieData> data = db.movieDao().getAll();
//            myRV = (RecyclerView)findViewById(R.id.movies);
//            myAdapter = new MovieAdapter(MoviesActivity.this, data);
//            myRV.setAdapter(myAdapter);
//            myRV.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));
//
//        }
//        catch (Exception e) {}
//    }


    private void loadInService() {
        Intent getData = new Intent (this, omdbPull.class);
        getData.putExtra("endpoint",ENDPOINT);
        startService (getData);
    }


    public class ResponseReceiver extends BroadcastReceiver {

        public static final String LOCAL_ACTION = "com.example.dimov.moviesproject.intent_service.DONE";
        private String jSonData="";


        @Override
        public void onReceive (Context context, Intent intent) {
            jSonData = intent.getStringExtra("json");

            List<MovieData> data = new ArrayList<>();
            JSONArray array = new JSONArray();
            try {
                JSONObject object = new JSONObject(jSonData);
                array = object.getJSONArray("Search");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject json_data = array.getJSONObject(i);
                    MovieData m = new MovieData();
                    m.Title = json_data.getString("Title");
                    m.Poster = json_data.getString("Poster");
                    m.Year = json_data.getString("Year");
                    m.imdbID = json_data.getString("imdbID");
                    data.add(m);
                }

                myRV = (RecyclerView)findViewById(R.id.movies);
                myAdapter = new MovieAdapter(MoviesActivity.this, data);
                myRV.setAdapter(myAdapter);
                myRV.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }



}


