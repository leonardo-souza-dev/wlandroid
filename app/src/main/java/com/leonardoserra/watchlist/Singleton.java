package com.leonardoserra.watchlist;

import android.content.Context;
import java.io.Serializable;

public class Singleton implements Serializable {

    private static Singleton mInstance = null;
    private static Context context;
    private static WLService wlService;
    private static Helper helper;

    private Singleton(){
    }

    public static Singleton getInstance () {
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public static Singleton getInstance(Context pContext){
        context = pContext;

        if(mInstance == null) {
            mInstance = new Singleton();
        }

        wlService = new WLService(context);
        helper = new Helper(context);
        return mInstance;
    }

    public WLService getWLService(){
        return this.wlService;
    }

    public Helper getHelper(){
        return this.helper;
    }
}
