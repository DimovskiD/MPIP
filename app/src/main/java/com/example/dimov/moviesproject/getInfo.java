package com.example.dimov.moviesproject;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

/**
 * Created by dimov on 12/3/2017.
 */

public class getInfo extends IntentService {

    public getInfo() {
        super("getInfo");
    }
    AppDatabase db = App.get().getDB();
    @Override
    protected void onHandleIntent(Intent workIntent) {

    }
}
