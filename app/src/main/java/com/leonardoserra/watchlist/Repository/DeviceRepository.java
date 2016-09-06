package com.leonardoserra.watchlist.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Interfaces.IObservador;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Interfaces.ISujeito;
import com.leonardoserra.watchlist.ViewModels.Message;

import java.util.ArrayList;

/**
 * Created by leonardo on 03/09/16.
 */
public class DeviceRepository implements IRepository, ISujeito, IObservador {

    private String hash;
    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor e;


    private boolean novoElemento = false;

    public DeviceRepository(Context pContext){
        context = pContext;
        sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
        e = sharedPreferences.edit();
    }







    public String criarOuObterUsuario(String usuario){
        usuario = sharedPreferences.getString("wl_user_hash", null);

        if (usuario != null) notificarObservadores("usuario", usuario);

        return usuario;
    }

    public ArrayList<Filme> obterMyListt(){
        String myListt = sharedPreferences.getString("mylistt", null);

        Gson gson = new Gson();

        ArrayList<Filme> filmes = gson.fromJson(myListt, new TypeToken<ArrayList<Filme>>(){}.getType());

        return filmes;

    }

    public ArrayList<Filme> buscar(String termo) {
        return null;
    }



    public boolean removerFilme(Filme filme){
        return false;
    }

    public boolean adicionarFilme(Filme filme){
        return false;
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

    public void notificarObservadores(String propriedade, String valor) {
        for(IObservador o : observadores) {
            o.atualizar(this, propriedade, valor);
        }
    }

    public void atualizar(ISujeito s, String p, String v) {
        if (s == this) {
            Log.d("WL", "mesmo objeto");
        } else {
            if (p.equals("usuario")){
                if (v != null || !v.equals("")){

                    e.putString("wl_user_hash", v);

                    e.commit();
                }
            }
        }
    }



}
