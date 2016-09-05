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
import java.util.ArrayList;

public class Singleton implements Serializable {

    private static Singleton mInstance = null;
    private static Context context;
    private static Resources resources;

    private String mString;
    private String userHash;
    private Bundle bundle;
    private User user;
    private String basePosterUrl;

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

    private static WLService wlService;

    public static Singleton getInstance(Context pContext, Resources pResources){
        context = pContext;
        resources = pResources;

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

    public String criaOuObtemUsuario(){
        SharedPreferences sp = context.getSharedPreferences("", context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String resultado;

        try {
            user = new User();

            userHash = sp.getString("wl_user_hash", null);

            Message msgCreateUser = new ApiHelper(context).createuser(userHash);

            if(msgCreateUser.getSucess()) {
                userHash = msgCreateUser.getObject("hash");

                e.putString("wl_user_hash", userHash);

                e.commit();

                return userHash;
            }else{
                if (msgCreateUser.getMessage().toUpperCase().equals("UNKNOWHOSTEXCEPTION")){
                    //resultado = "Check your internet connection";
                    return userHash;
                } else{
                    //resultado = resources.getString(R.string.some_error_occurred);
                    return userHash;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Singleton.getInstance().enviarLogException(ex);
            resultado = resources.getString(R.string.some_error_occurred);
        }

        return resultado;
    }

    public ArrayList<MovieViewModel> buscaFilme(String termoBusca){

        ArrayList<MovieViewModel> resultadoDaBusca = null;

        Message msg = new ApiHelper(context).search(userHash, termoBusca);

        try {

            JSONObject object = msg.getObject();
            JSONArray jsonArray = object.getJSONArray("movies");

            if (jsonArray != null) {

                resultadoDaBusca = new ArrayList<>();
                int len = jsonArray.length();

                for (int i = 0; i < len; i++) {
                    String str = jsonArray.get(i).toString();
                    MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                    //f.setUser(user);
                    resultadoDaBusca.add(f);
                }
            }
        } catch (Exception e) {
            Singleton.getInstance().enviarLogException(e);
            e.printStackTrace();
        }

        return resultadoDaBusca;
    }

    public String getUserHash(){
        return userHash;
    }

    public ArrayList<MovieViewModel> getMyListt(){
        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        Message msg = new ApiHelper().obterMyListt(userHash);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray;

        try {
            jsonArray = mylisttJson.getJSONArray("mylistt");

            if (jsonArray != null) {

                int qtdMyListt = jsonArray.length();

                if (qtdMyListt > 0) {

                    for (int i = 0; i < qtdMyListt; i++) {
                        String str = jsonArray.get(i).toString();
                        MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                        myListItems.add(f);
                    }

                }
            }
        } catch (Exception e) {
            Singleton.getInstance().enviarLogException(e);
            e.printStackTrace();
        }

        return myListItems;
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
