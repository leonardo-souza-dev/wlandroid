package com.leonardoserra.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ViewModels.Message;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;
import com.leonardoserra.watchlist.ViewModels.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Singleton implements Serializable {

    private static Singleton mInstance = null;
    private static Context context;
    private String mString;
    private Bundle bundle;
    private String basePosterUrl;

    private static WLService wlService;

    private Singleton(){
        mString = "Hello";

        if (bundle == null){
            bundle = new Bundle();
        }
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

        if(mInstance == null)
        {
            mInstance = new Singleton();
        }

        wlService = new WLService(context);
        return mInstance;
    }

    public WLService getWLService(){
        return this.wlService;
    }

    public String obterUrlBase(){
        String urlBase;

        String testarApiLocal = context.getResources().getString(R.string.testarApiLocal);

        if (testarApiLocal.equals("true")) {
            if (isEmulator()) {
                urlBase = context.getResources().getString(R.string.baseUrlLocalEmulator);
            }else{
                urlBase = context.getResources().getString(R.string.baseUrlLocalDevice);
            }
        } else {
            urlBase = context.getResources().getString(R.string.baseUrlNuvem);

        }
        return urlBase;
    }

    public String obterUrlBasePoster(String pPoster){
        basePosterUrl = obterUrlBase() + "/poster?p=" + pPoster;

        return basePosterUrl;
    }

    public void enviarLogException(Exception e){
        ApiHelper apiHelper = new ApiHelper();
        apiHelper.enviarLog(TextUtils.join("\r\n", e.getStackTrace()));
    }

    public String obterUrlBaseApi(){
        basePosterUrl = obterUrlBase() + "/api";

        return basePosterUrl;
    }

    public String getString(){ return this.mString; }

    public void setString(String value){
        mString = value;
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
