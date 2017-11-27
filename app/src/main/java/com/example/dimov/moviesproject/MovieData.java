package com.example.dimov.moviesproject;

/**
 * Created by dimov on 11/14/2017.
 */

class MovieData {
    String imdbID;
    String Title;
    String Year;
    String Poster;

    MovieData(String i, String t, String y, String im) {
        imdbID = i;
        Title =t;
        Year = y;
        Poster = im;
    }

    MovieData() {}
}
