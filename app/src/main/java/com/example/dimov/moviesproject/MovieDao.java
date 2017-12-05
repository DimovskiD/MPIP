package com.example.dimov.moviesproject;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

import java.util.List;

/**
 * Created by dimov on 12/3/2017.
 */
@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieData")
    List<MovieData> getAll();

    @Query("SELECT * FROM MovieData WHERE imdbID IN (:movieIDs)")
    List<MovieData> loadAllById (String[] movieIDs);

    @Query("SELECT * FROM MovieData WHERE imdbID IN (:movieID)")
    MovieData loadById (String movieID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert (MovieData  movie);

    @Delete
    void deleteAll (MovieData ... movies);

}
