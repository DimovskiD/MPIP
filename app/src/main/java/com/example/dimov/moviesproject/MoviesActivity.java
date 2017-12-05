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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.List;



public class MoviesActivity extends AppCompatActivity {
    private String ENDPOINT = "";
    String u="";
    MovieAdapter myAdapter;
    RecyclerView myRV;
    AppDatabase db = App.get().getDB();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setUI();
        getMoviesFromDB();
        //show movies from database (if any)

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

    private void getMoviesFromDB() {
        try {
            List<MovieData> data = db.movieDao().getAll();
            myRV = (RecyclerView)findViewById(R.id.movies);
            myAdapter = new MovieAdapter(MoviesActivity.this, data);
            myRV.setAdapter(myAdapter);
            myRV.setLayoutManager(new LinearLayoutManager(MoviesActivity.this));

        }
        catch (Exception e) {}
    }


    private void loadInService() {
        Intent getData = new Intent (this, omdbPull.class);
        getData.setData(Uri.parse(ENDPOINT));
        getData.putExtra("endpoint",ENDPOINT);
        this.startService (getData);
    }




}


