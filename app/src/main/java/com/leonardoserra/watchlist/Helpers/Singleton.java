package com.leonardoserra.watchlist.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Fragments.FragmentSearchResult;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.parsers.FactoryConfigurationError;

//https://gist.github.com/Akayh/5566992
public class Singleton  {

    private static Singleton mInstance = null;
    private static FragmentManager fm;
    private static Context context;
    private static Historico historicoFragment = null;
    private static Resources resources;

    private String mString;
    private MovieViewModel movieViewModel;
    private String userHash;
    private Bundle bundle;
    private User user;
    //private ArrayList<MovieViewModel> resultadosDaBusca;
    private ArrayList<MovieViewModel> recomendados;
    private String basePosterUrl;
    private Integer qtdMyListt;
    private ResultadoBuscaViewModel resultadoDaBuscaViewModel;

    private Singleton(){
        mString = "Hello";
        if (historicoFragment == null) {
            historicoFragment = new Historico();
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

    public static Singleton getInstance(Context pContext, Resources pResources, FragmentManager pFm){
        context = pContext;
        fm = pFm;
        resources = pResources;

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

    public String criaOuObtemUsuario(){
        SharedPreferences sp = context.getSharedPreferences("", context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String resultado;

        try {
            user = new User();

            //sp = getPreferences(MODE_PRIVATE);
            userHash = sp.getString("wl_user_hash", null);
            //Singleton.getInstance().setUserHash(gHash);

            Message msgCreateUser = new ApiHelper(context).createuser(userHash);

            userHash = msgCreateUser.getObject("hash");

            e.putString("wl_user_hash", userHash);
            e.commit();

            resultado = msgCreateUser.getMessage();

        } catch (Exception ex) {
            ex.printStackTrace();
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
                    f.setUser(user);
                    resultadoDaBusca.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultadoDaBusca;
    }

    private void insereNoHistoricoDeBuscas(ResultadoBuscaViewModel r){
        if (buscas == null) {
            buscas = new ArrayList<>();
        }
        buscas.add(r);

    }

    private ArrayList<ResultadoBuscaViewModel> buscas;

    public ArrayList<ResultadoBuscaViewModel> getBuscas(){
        return buscas;
    }

    public ResultadoBuscaViewModel getResultadoBuscaViewModel(){
        int qtdTotal = buscas.size();
        ResultadoBuscaViewModel ultima = buscas.get(qtdTotal-1);

        return ultima;
    }

    public void mostraPenultimoResultadoBusca(){

        int qtdTotal = buscas.size();
        buscas.remove(qtdTotal-1);
        //historicoFragment.removeUltimo();

        Singleton.getInstance().trocaFrag(new FragmentSearchResult());
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

    public void removeUltimoFragmentDoHistorico(){
        Singleton.historicoFragment.removeUltimo();
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

    //public void setResultadosDaBusca(ArrayList<MovieViewModel> list){
    //    resultadosDaBusca = list;
        //bundle.putSerializable("bundle_searchResult", list);
    //}

    //public ArrayList<MovieViewModel> getResultadosDaBusca(){
        //resultadosDaBusca = (ArrayList<MovieViewModel>)bundle.getSerializable("bundle_searchResult");
    //    return resultadosDaBusca;
    //}

    /*
    public void updateSearchResult(MovieViewModel movieUpdate){
        ArrayList<MovieViewModel> myListItemsUpdated = new ArrayList<>();

        for(MovieViewModel local : resultadosDaBusca){
            if (local.get_id() == movieUpdate.get_id()) {
                myListItemsUpdated.add(movieUpdate);
            } else {
                myListItemsUpdated.add(local);
            }
        }

        resultadosDaBusca = myListItemsUpdated;
    }
    */

    /*
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
    */

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
        return Singleton.historicoFragment.getFragmentAtual();
    }

    public String getNomeFragmentAtual(){
        return Singleton.historicoFragment.getNomeFragmentAtual();
    }

    public String getNomeFragmentAnterior(){
        return Singleton.historicoFragment.getNomeFragmentAnterior();
    }

    public int getHistoricoSize(){

        return historicoFragment.items.size();
    }

    public void trocaFrag(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frame_container, fragment, "");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public void trocaFragEInsereNoHistorico(Fragment f){
        trocaFrag(f);
        insereNoHistorico(f);
    }

    public void insereNoHistorico(Fragment f){
        if (historicoFragment == null) {
            historicoFragment = new Historico();
        }

        historicoFragment.addItem(f);
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
            if (items.size() > 0) {
                items.remove(items.size() - 1);
            }
        }
    }

    /*
        ResultadoDaBusca
     */
    public class ResultadoBuscaViewModel{

        private ArrayList<MovieViewModel> resultadoDaBusca;
        private String termoBusca;
        private Integer qtd;

        public ResultadoBuscaViewModel(ArrayList<MovieViewModel> pResultadoDaBusca, String pTermoBusca, Integer pQtd ){
            resultadoDaBusca = pResultadoDaBusca;
            termoBusca = pTermoBusca;
            qtd = pQtd;
        }

        public ArrayList<MovieViewModel> getResultadoDaBusca(){
            return resultadoDaBusca;
        }

        public String getTermoBusca(){
            return termoBusca;
        }

        public Integer getQtd(){
            return qtd;
        }

    }
}
