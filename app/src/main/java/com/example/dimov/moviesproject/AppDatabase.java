package com.example.dimov.moviesproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by dimov on 12/3/2017.
 */

@Database(entities = {MovieData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
