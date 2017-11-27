package com.example.dimov.moviesproject;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {

    private ShareActionProvider mShareActionProvider;
    private RequestQueue requestQueue;
    private Gson gson;
    String ENDPOINT ="";
    private String t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        Bundle extras = getIntent().getExtras();
        if (getIntent().hasExtra("id")) {
            ENDPOINT += "http://www.omdbapi.com/?i="+ extras.get("id")+"&apikey=67eb4522&plot=full";
            Log.i("endpoint", ENDPOINT);
            t="http://imdb.com/title/"+extras.get("id").toString();
        }

        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        fetchPosts();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,t);
        setShareIntent(shareIntent);

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
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
        //        @Override
//        public void onResponse(String response) {
//            Log.i("PostActivity", response);
//        }
        @Override
        public void onResponse(String response) {
            JSONObject object=new JSONObject();
            Log.i("Activity",response);
            try {
                object = new JSONObject(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView title =(TextView)findViewById(R.id.titlebig);
                title.setText(object.getString("Title"));
                t = object.getString("Title");
                Glide.with(MovieDetailsActivity.this).load(object.getString("Poster"))
                        .placeholder(R.drawable.ic_img_error)
                        .error(R.drawable.ic_img_error)
                        .into((ImageView)findViewById(R.id.img));

                TextView year =(TextView)findViewById(R.id.year);
                year.setText("Year: "+ object.getString("Year"));

                TextView rated =(TextView)findViewById(R.id.rated);
                rated.setText("Rated: "+object.getString("Rated"));

                TextView genre =(TextView)findViewById(R.id.genre);
                genre.setText("Genre: "+object.getString("Genre"));

                TextView language =(TextView)findViewById(R.id.language);
                language.setText("Language: "+object.getString("Language"));

                TextView director =(TextView)findViewById(R.id.director);
                director.setText("Director: "+object.getString("Director"));

                TextView writer =(TextView)findViewById(R.id.writer);
                writer.setText("Writer(s): "+object.getString("Writer"));

                TextView actors =(TextView)findViewById(R.id.actors);
                actors.setText("Actors: "+object.getString("Actors"));

                TextView plot =(TextView)findViewById(R.id.plot);
                plot.setText("Plot: "+object.getString("Plot"));





            }
            catch (JSONException e) {
                e.printStackTrace();
            }
//            List<MovieData> movies = Arrays.asList(gson.fromJson(response, MovieData[].class));
//
//
//
//            Log.i("PostActivity", movies.size() + " posts loaded.");
//            for (MovieData movie : movies) {
//                Log.i("PostActivity", movie.imdbID + ": " + movie.Title);
//            }
        }
    };

}
