package com.leonardoserra.watchlist.Repository;

import android.util.Log;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Interfaces.IObservador;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Interfaces.ISujeito;
import com.leonardoserra.watchlist.ViewModels.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */
public class CloudRepository implements IRepository, IObservador, ISujeito {

    private ApiHelper apiHelper;
    private String hash;
    //private String usuarioApi;
    //private String senhaApi;

    public CloudRepository(){
        String usuarioSenhaApi ="asd:watxi1izTTPWD*";
        String usuarioApi = usuarioSenhaApi.split(":")[0];
        String senhaApi = usuarioSenhaApi.split(":")[1];

        apiHelper = new ApiHelper();
    }

    public String criarOuObterUsuario(String usuario){
        Message msgCreateUser = apiHelper.createuser(usuario);

        try {
            if (msgCreateUser.getSucess()) {
                String usuarioApi = msgCreateUser.getObject("hash");
                if ((usuario == null && usuarioApi != null) || (!usuario.equals(usuarioApi))){
                    notificarObservadores("usuario", usuarioApi);
                    notificarObservadores("mylistt_zerada", new ArrayList<Filme>());
                    hash = usuarioApi;
                }
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return hash;
    }

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

    public ArrayList<Filme> buscar(String termo) {

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

    public boolean removerFilme(Filme filme){
        boolean sucesso = false;
        try {
            apiHelper.removeMovie(hash, filme);
            sucesso = true;
            notificarObservadores("filme_removido", filme);
        } catch (Exception ex){
            sucesso = false;
            ex.printStackTrace();
        }
        return sucesso;
    }

    public boolean adicionarFilme(Filme filme){
        boolean sucesso = false;
        try {
            apiHelper.adicionaFilme(hash, filme);
            sucesso = true;
            notificarObservadores("filme_adicionado", filme);
        } catch (Exception ex){
            sucesso = false;
            ex.printStackTrace();
        }
        return sucesso;
    }

    public String obterUrlPoster(String pTmdbId){

        String urlPoster = null;
        Message msg = apiHelper.obterUrlPoster(pTmdbId);

        if (msg != null && msg.getObject() != null) {
            try {

                JSONObject object = msg.getObject();
                urlPoster = object.get("urlPoster").toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return urlPoster;

    }

    /*
        Padrao Observer
     */

    private ArrayList<IObservador> observadores = new ArrayList<>();

    public void registrarObservador(IObservador o) {
        observadores.add(o);
    }

    public void removerObservador(IObservador o) {
        observadores.remove(o);
    }

    public void notificarObservadores(String param, Object valor) {
        for(IObservador o : observadores) {
            o.atualizar(this, param, valor);
        }
    }

    public void atualizar(ISujeito s, String p, Object v) {
        if (s != this) {

            if (p.equals("usuario")){

                if (v != null && !v.toString().equals("")) {

                    //ApiHelper api = new ApiHelper();
                    //try {
                    //Message msg = api.createuser("");
                    hash = v.toString();

                    //if (msg.getSucess()) {

                    //hash = msg.getObject("hash");
                    //notificarObservadores("usuario", hash);
                    //}
                    //} catch (Exception ex){
                    //    ex.printStackTrace();
                    //}
                }
            }
        }
    }
}