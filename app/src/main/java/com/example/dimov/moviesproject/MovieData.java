package com.example.dimov.moviesproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by dimov on 11/14/2017.
 */

@Entity (tableName = "MovieData")
class MovieData {
    @PrimaryKey
    String imdbID;

    @ColumnInfo(name = "title")
    String Title;

    @ColumnInfo(name="year")
    String Year;

    @ColumnInfo(name="poster")
    String Poster;

    MovieData(String i, String t, String y, String im) {
        imdbID = i;
        Title =t;
        Year = y;
        Poster = im;
    }

    MovieData() {}

    public String getImdbID () {return imdbID;}
    public String getTitle () {return Title;}
    public String getYear () {return Year;}
    public String getPoster () {return  Poster;}
}
