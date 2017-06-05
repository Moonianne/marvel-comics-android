package com.segunfamisa.sample.comics;

import android.app.Application;

import com.segunfamisa.sample.comics.util.RealmHelper;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // init realm
        RealmHelper.init(this);
    }
}
