package com.example.dimov.moviesproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import com.bumptech.glide.Glide;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

/**
 * Created by dimov on 11/14/2017.
 */

public class MovieAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<MovieData> data = Collections.emptyList();
    MovieData current;
    int currentPos = 0;


    public MovieAdapter(Context context, List<MovieData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_movie, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        final MovieData current = data.get(position);
        myHolder.title.setText(current.Title);
        myHolder.year.setText("Year: " + current.Year);
        myHolder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,MovieDetailsActivity.class);
                i.putExtra("id",current.imdbID);
                context.startActivity(i);
            }});
        myHolder.rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                data.remove(current);
                notifyDataSetChanged();
                return true;
            }});


        // load image into imageview using glide
        Glide.with(context).load(current.Poster)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.poster);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView poster;
        TextView year;
        RelativeLayout rl;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            year = (TextView) itemView.findViewById(R.id.year);
            rl = (RelativeLayout) itemView.findViewById(R.id.layout);

        }


    }
}