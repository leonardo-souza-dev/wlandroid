package com.leonardoserra.watchlist.Helpers;

import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

import java.util.ArrayList;

//https://gist.github.com/Akayh/5566992
public class Singleton  {

    private static Singleton mInstance = null;
    private String mString;
    private static FragmentManager fm;
    private static Historico historico = null;
    private MovieViewModel movieViewModel;
    private String userHash;
    private Bundle bundle;
    private User user;
    private MovieViewModel movieUpdate;

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

    public void setBundleSearchResult(ArrayList<MovieViewModel> list){
        bundle.putSerializable("bundle_searchResult", list);
    }

    public ArrayList<MovieViewModel> getBundleSearchResult(){
        return (ArrayList<MovieViewModel>)bundle.getSerializable("bundle_searchResult");
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

    public void setMovieUpdate(MovieViewModel m){
        movieUpdate = m;
    }

    public MovieViewModel getMovieUpdate(){
        return movieUpdate;
    }

    public void trocaFrag(Fragment fragment){
        //fragment.setArguments(bundle);
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
