package com.leonardoserra.watchlist.Helpers;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//https://gist.github.com/Akayh/5566992
public class Singleton  {

    private static Singleton mInstance = null;
    private static FragmentManager fm;
    private static Historico historico = null;

    private String mString;
    private MovieViewModel movieViewModel;
    private String userHash;
    private Bundle bundle;
    private User user;
    private ArrayList<MovieViewModel> bundleSearchResult;
    private ArrayList<MovieViewModel> recomendados;
    private String basePosterUrl;
    private Integer qtdMyListt;

    private Singleton(){
        mString = "Hello";
        if (historico == null) {
            historico = new Historico();
        }
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

    public static Singleton getInstance(FragmentManager pFm){
        fm = pFm;
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public String getUrl(String pPoster){

        basePosterUrl = "http://10.0.2.2:8080/poster?p=" + pPoster;
        if (!isEmulator()) {
            basePosterUrl = "http://192.168.1.5:8080/poster?p=" + pPoster;
        }

        return basePosterUrl;
    }

    public String getString(){ return this.mString; }

    public void setString(String value){
        mString = value;
    }

    public void setUserHash(String pHash){
        userHash = pHash;
    }

    public void setUser(User pUser){
        user = pUser;
    }

    public User getUser(){
        return user;
    }

    public String getUserHash(){
        return userHash;
    }

    public void setMovieViewModel(MovieViewModel pMovieViewModel){
        movieViewModel = pMovieViewModel;
    }

    public MovieViewModel getMovieViewModel(){
        return movieViewModel;
    }

    public void popBackStackk(){
        Singleton.historico.removeUltimo();
        fm.popBackStackImmediate();
    }

    public int getQtdMyListt(){
        return qtdMyListt;
    }

    public ArrayList<MovieViewModel> getMyListt(){
        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        Message msg = new ApiHelper().obterMyListt(userHash);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray;

        try {
            jsonArray = mylisttJson.getJSONArray("mylistt");

            if (jsonArray != null) {

                qtdMyListt = jsonArray.length();

                if (qtdMyListt > 0) {

                    for (int i = 0; i < qtdMyListt; i++) {
                        String str = jsonArray.get(i).toString();
                        MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                        myListItems.add(f);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myListItems;
    }

    public ArrayList<MovieViewModel> getRecomendados(){
        recomendados = new ArrayList<>();

        Message msg = new ApiHelper().obterFilmesRecomendados(userHash);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray;

        try {
            jsonArray = mylisttJson.getJSONArray("filmesrecomendados");

            if (jsonArray != null) {

                int len = jsonArray.length();

                for (int i = 0; i < len; i++) {
                    String str = jsonArray.get(i).toString();
                    MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                    f.setUser(user);
                    recomendados.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return recomendados;
    }

    public void setBundleSearchResult(ArrayList<MovieViewModel> list){
        bundle.putSerializable("bundle_searchResult", list);
    }

    public ArrayList<MovieViewModel> getBundleSearchResult(){
        bundleSearchResult =
                (ArrayList<MovieViewModel>)bundle.getSerializable("bundle_searchResult");
        return bundleSearchResult;
    }

    public void updateSearchResult(MovieViewModel movieUpdate){
        ArrayList<MovieViewModel> myListItemsUpdated = new ArrayList<>();

        for(MovieViewModel local : bundleSearchResult){
            if (local.get_id() == movieUpdate.get_id()) {
                myListItemsUpdated.add(movieUpdate);
            } else {
                myListItemsUpdated.add(local);
            }
        }

        bundleSearchResult = myListItemsUpdated;
    }

    public void updateRecomendados(MovieViewModel movieUpdate){
        ArrayList<MovieViewModel> myListItemsUpdated = new ArrayList<>();

        for(MovieViewModel recomendado : recomendados){
            if (recomendado.get_id() == movieUpdate.get_id()) {
                myListItemsUpdated.add(movieUpdate);
            } else {
                myListItemsUpdated.add(recomendado);
            }
        }

        recomendados = myListItemsUpdated;
    }

    public void setTermo(String termo){
        bundle.putSerializable("termo", termo);
    }

    public String getTermo(){
        return bundle.getString("termo");
    }

    public void setQtd(int len){
        bundle.putSerializable("qtd", len);
    }

    public int getQtd(){
        return bundle.getInt("qtd");
    }

    public Fragment getFragmentAtual(){
        return Singleton.historico.getFragmentAtual();
    }

    public String getNomeFragmentAtual(){
        return Singleton.historico.getNomeFragmentAtual();
    }

    public String getNomeFragmentAnterior(){
        return Singleton.historico.getNomeFragmentAnterior();
    }

    public int getHistoricoSize(){
        return historico.items.size();
    }

    public void trocaFrag(Fragment fragment){
        historico.addItem(fragment);

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frame_container, fragment, "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
        fm.executePendingTransactions();
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

    /*
        HistoricoItem
     */
    private class HistoricoItem{
        private Fragment fragment;
        private int ordem;

        public HistoricoItem(Fragment pFragment, Integer pOrdem){
            fragment = pFragment;
            ordem = pOrdem;
        }

        public String getFragmentName(){
            return fragment.getClass().getSimpleName();
        }
    }

    /*
        Historico
     */
    private class Historico{
        private ArrayList<HistoricoItem> items;
        private int ordem;

        public Historico(){
            items = new ArrayList<>();
            ordem = 0;
        }

        public void addItem(Fragment fragment){

            HistoricoItem item = new HistoricoItem(fragment, ordem);
            items.add(item);
            ordem++;
        }

        private Fragment getFragmentAtual(){
            if (items.size() > 0) {
                HistoricoItem hi = items.get(items.size() - 1);
                return hi.fragment;
            }
            return null;
        }

        private String getNomeFragmentAtual(){
            String nome = "";
            if (items.size() >0 ) {
                HistoricoItem hi = items.get(items.size() - 1);
                nome = hi.getFragmentName();
            }
            return nome;
        }

        private String getNomeFragmentAnterior(){
            String nome = "";
            if (items.size() > 1){
                HistoricoItem hi = items.get(items.size() - 2);
                nome = hi.getFragmentName();
            }
            return nome;
        }

        public void removeUltimo(){
            items.remove(items.size()-1);
        }
    }
}
