package com.example.dimov.moviesproject;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

/**
 * Created by dimov on 11/14/2017.
 */

@Entity (tableName = "MovieData")
class MovieData {
    @PrimaryKey
            @NotNull
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

    public String getImdbID = imdbID;
    public String getTitle = Title;
    public String getYear = Year;
    public String getPoster = Poster;


}
