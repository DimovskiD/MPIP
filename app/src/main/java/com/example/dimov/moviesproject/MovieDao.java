package com.example.dimov.moviesproject;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import java.util.List;

/**
 * Created by dimov on 12/3/2017.
 */

public interface MovieDao {

    @Query("SELECT * FROM MovieData")
    List<MovieData> getAll();

    @Query("SELECT * FROM MovieData WHERE imdbID IN (:movieIDs)")
    List<MovieData> loadAllById (String[] movieIDs);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll (MovieData ... movies);

    @Delete
    void deleteAll (Movie ... movies);

}
