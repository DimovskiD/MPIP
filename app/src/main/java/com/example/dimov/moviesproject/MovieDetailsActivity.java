package com.example.dimov.moviesproject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
    private String id;
    String ENDPOINT = "";
    private String t;
    ResponseReceiver responseReceiver;
    MovieData m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);

        checkExtras(getIntent().getExtras());
        gsonInit();

        if (isInternetConnected(this)) {
            loadInService();
        }
        else {
            getInfoFromDB();
        }
    }

    private void getInfoFromDB() {
        AppDatabase db = App.get().getDB();
        m= db.movieDao().loadById(id);
        TextView title = (TextView) findViewById(R.id.titlebig);
        title.setText(m.Title);

        TextView year = (TextView) findViewById(R.id.year);
        year.setText(m.Year);

        Glide.with(MovieDetailsActivity.this).load(m.getPoster)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into((ImageView) findViewById(R.id.img));


    }

    protected void onResume () {
        super.onResume();
        IntentFilter broadcastFilter = new IntentFilter(MovieDetailsActivity.ResponseReceiver.LOCAL_ACTION);
        responseReceiver = new MovieDetailsActivity.ResponseReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(responseReceiver,broadcastFilter);
    }

    protected void onPause () {
        super.onPause();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(responseReceiver);
    }

    private void loadInService() {
        Intent getData = new Intent(this, omdbPull.class);
        getData.putExtra("endpoint", ENDPOINT);
        startService(getData);
    }

    private void gsonInit() {

        requestQueue = Volley.newRequestQueue(this);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

    }

    private void checkExtras(Bundle extras) {
        if (getIntent().hasExtra("id")) {
            ENDPOINT += "http://www.omdbapi.com/?i=" + extras.get("id") + "&apikey=67eb4522&plot=full";
            Log.i("endpoint", ENDPOINT);
            id=extras.get("id").toString();
            t = "http://imdb.com/title/" + id;

        }
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
        shareIntent.putExtra(Intent.EXTRA_TEXT, t);
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


    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public class ResponseReceiver extends BroadcastReceiver {

        public static final String LOCAL_ACTION = "com.example.dimov.moviesproject.intent_service.DONE";
        private String jSonData = "";


        @Override
        public void onReceive(Context context, Intent intent) {
            jSonData = intent.getStringExtra("json");
            JSONObject object = new JSONObject();
            List<MovieData> data = new ArrayList<>();
            JSONArray array = new JSONArray();
            try {
                object = new JSONObject(jSonData);
                array = object.getJSONArray("Search");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                TextView title = (TextView) findViewById(R.id.titlebig);
                title.setText(object.getString("Title"));
                t = object.getString("Title");
                Glide.with(MovieDetailsActivity.this).load(object.getString("Poster"))
                        .placeholder(R.drawable.ic_img_error)
                        .error(R.drawable.ic_img_error)
                        .into((ImageView) findViewById(R.id.img));

                TextView year = (TextView) findViewById(R.id.year);
                year.setText("Year: " + object.getString("Year"));

                TextView rated = (TextView) findViewById(R.id.rated);
                rated.setText("Rated: " + object.getString("Rated"));

                TextView genre = (TextView) findViewById(R.id.genre);
                genre.setText("Genre: " + object.getString("Genre"));

                TextView language = (TextView) findViewById(R.id.language);
                language.setText("Language: " + object.getString("Language"));

                TextView director = (TextView) findViewById(R.id.director);
                director.setText("Director: " + object.getString("Director"));

                TextView writer = (TextView) findViewById(R.id.writer);
                writer.setText("Writer(s): " + object.getString("Writer"));

                TextView actors = (TextView) findViewById(R.id.actors);
                actors.setText("Actors: " + object.getString("Actors"));

                TextView plot = (TextView) findViewById(R.id.plot);
                plot.setText("Plot: " + object.getString("Plot"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to go back?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
