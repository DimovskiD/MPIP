package com.example.dimov.moviesproject;

import android.content.Intent;
import android.graphics.Movie;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoviesActivity extends AppCompatActivity {
    private String ENDPOINT = "";
    String u="";
    MovieAdapter myAdapter;
    RecyclerView myRV;
    private RequestQueue requestQueue;
    private Gson gson;
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ImageView iv = (ImageView) findViewById(R.id.searchButton);
        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = ((EditText) findViewById(R.id.toolbar_search)).getText().toString();
                u=u.replace(" ","+");
                ENDPOINT = "http://www.omdbapi.com/?s="+u+"&apikey=67eb4522";

                fetchPosts();
            }
        });
    }

        private void fetchPosts() {

            StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
            requestQueue.add(request);
        }



    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            List<MovieData> data = new ArrayList<>();
            JSONArray array = new JSONArray();
            try {
                JSONObject object = new JSONObject(response);
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

            }
            catch (JSONException e) {
                Toast.makeText(MoviesActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    };

    }
