package com.example.dimov.moviesproject;

import android.app.Application;
import android.arch.persistence.room.Room;

/**
 * Created by dimov on 12/3/2017.
 */

public class App extends Application {
    public static App INSTANCE;
    private static final String DATABASE_NAME = "movieDatabase";

    private AppDatabase db;

    public static App get() {
        return  INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // create database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).allowMainThreadQueries()
                .build();

        INSTANCE = this;
    }

    public AppDatabase getDB() {
        return db;
    }

}
