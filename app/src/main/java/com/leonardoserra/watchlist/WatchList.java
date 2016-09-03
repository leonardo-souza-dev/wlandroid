package com.leonardoserra.watchlist;

import android.app.Application;

/**
 * Created by leonardo on 02/09/16.
 */
public class WatchList /*extends Application*/ {

    private static WLService _WLService;

    //@Override
    public void onCreate() {
        //super.onCreate();
        _WLService = new WLService(null);
    }

    synchronized public WLService getFilmeService(){
        if (_WLService == null){
            _WLService = new WLService(null);
        }
        return _WLService;
    }

}
