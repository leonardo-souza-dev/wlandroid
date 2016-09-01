package com.leonardoserra.watchlist;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Helpers.ApiHelper;
import com.leonardoserra.watchlist.ViewModels.Message;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */
public class FilmeRepository implements IFilmeRepository {

    private ApiHelper apiHelper;
    private String hash;

    public FilmeRepository(String pHash){
        hash = pHash;
        apiHelper = new ApiHelper();
    }

    @Override
    public ArrayList<Filme> obterMyListt(){

        ArrayList<Filme> myListt = null;
        Message msg = apiHelper.obterMyListt(hash);

        if (msg != null && msg.getObject() != null) {
            JSONObject mylisttJson = msg.getObject();
            JSONArray jsonArray;

            try {
                jsonArray = mylisttJson.getJSONArray("mylistt");

                if (jsonArray != null) {
                    myListt = new ArrayList<>();

                    int qtdMyListt = jsonArray.length();

                    if (qtdMyListt > 0) {

                        for (int i = 0; i < qtdMyListt; i++) {
                            String str = jsonArray.get(i).toString();
                            Filme f = new Gson().fromJson(str, Filme.class);
                            myListt.add(f);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return myListt;
    }

    @Override
    public ArrayList<Filme> busca(String termo) {

        ArrayList<Filme> resultadoDaBusca = null;
        Message msg = apiHelper.search(hash, termo);

        if (msg != null && msg.getObject() != null) {
            try {

                JSONObject object = msg.getObject();
                JSONArray jsonArray = object.getJSONArray("movies");

                if (jsonArray != null) {
                    resultadoDaBusca = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String str = jsonArray.get(i).toString();
                        Filme f = new Gson().fromJson(str, Filme.class);
                        resultadoDaBusca.add(f);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultadoDaBusca;
    }
}