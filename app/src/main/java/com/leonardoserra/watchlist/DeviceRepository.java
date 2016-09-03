package com.leonardoserra.watchlist;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Helpers.ApiHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonardo on 03/09/16.
 */
public class DeviceRepository implements IRepository {
    private String hash;
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor e;

    public DeviceRepository(Context pContext){
        context = pContext;
        sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
        e = sharedPreferences.edit();
    }
    public ArrayList<Filme> obterMyListt(){
        ArrayList<Filme> myListtArray = null;
        String myListt = sharedPreferences.getString("mylistt", null);

        Gson gson = new Gson();

        ArrayList<Filme> filmes = gson.fromJson(myListt, new TypeToken<ArrayList<Filme>>(){}.getType());

        return filmes;

    }

}
