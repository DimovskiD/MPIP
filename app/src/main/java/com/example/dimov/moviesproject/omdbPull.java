package com.example.dimov.moviesproject;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimov on 12/3/2017.
 */

public class omdbPull extends IntentService {
    String ENDPOINT = "";
    private RequestQueue requestQueue;
    private Gson gson;
    AppDatabase db = App.get().getDB();

    public omdbPull() {
        super("omdbPull");
    }

    @Override
    public void onCreate () {
        super.onCreate();

        //send jSON request for movie
        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent workIntent) {
            ENDPOINT = workIntent.getStringExtra("endpoint");
            fetchPosts();

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
                    writeInDB(m);
                }

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MoviesActivity.ResponseReceiver.LOCAL_ACTION);
                broadcastIntent.putExtra("json",response);
                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(omdbPull.this);
                localBroadcastManager.sendBroadcast(broadcastIntent);

            }
            catch (JSONException e) {
            }

        }
    };

    private void writeInDB(MovieData m) {

        db.movieDao().insert(m);

    }

    private void fetchPosts() {

        StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);
        requestQueue.add(request);
    }
}
