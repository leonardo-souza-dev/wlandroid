package com.leonardoserra.watchlist.Repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leonardoserra.watchlist.Bean.Filme;
import com.leonardoserra.watchlist.Interfaces.IObservador;
import com.leonardoserra.watchlist.Interfaces.IRepository;
import com.leonardoserra.watchlist.Interfaces.ISujeito;

import java.util.ArrayList;
import java.util.Set;

public class DeviceRepository implements IRepository, ISujeito, IObservador {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor e;
    private ArrayList<IObservador> observadores = new ArrayList<>();

    public DeviceRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("", Context.MODE_PRIVATE);
        e = sharedPreferences.edit();
    }

    public String[] obterItensCarrossel() {
        Set<String> urlsSp = sharedPreferences.getStringSet("itenscarrossel", null);

        if (urlsSp == null)
            return null;

        return urlsSp.toArray(new String[urlsSp.size()]);

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

    /*
        Padrao Observer
     */

    public boolean adicionarFilme(Filme filme) {
        return false;
    }

    public void registrarObservador(IObservador o) {
        observadores.add(o);
    }

    public void removerObservador(IObservador o) {
        observadores.remove(o);
    }

    public void notificarObservadores(String propriedade, Object valor) {
        for(IObservador o : observadores) {
            o.atualizar(this, propriedade, valor);
        }
    }

    public void atualizar(ISujeito s, String param, Object valor) {
        if (s != this) {

            if (param.equals("mylistt_zerada")){
                if(valor != null){
                    ArrayList<Filme> myListt = new ArrayList<>();
                    e.putString("mylistt", new Gson().toJson(myListt));
                }
            }

            if (param.equals("filme_adicionado")){
                if(valor != null && valor instanceof Filme){
                    ArrayList<Filme> myListt = obterMyListt();
                    myListt.add((Filme) valor);

                    e.putString("mylistt", new Gson().toJson(myListt));
                }
            }

            if (param.equals("filme_removido")){
                if(valor != null && valor instanceof Filme){
                    ArrayList<Filme> myListt = obterMyListt();
                    ArrayList<Filme> novaMyListt = new ArrayList<>();
                    for (int i = 0; i < myListt.size(); i++){
                        if (!myListt.get(i).get_id().equals(((Filme) valor).get_id())){
                            novaMyListt.add(myListt.get(i));
                        }
                    }

                    e.putString("mylistt", new Gson().toJson(novaMyListt));
                }
            }

            if (param.equals("usuario")){
                if (valor != null && !valor.toString().equals("")){
                    e.putString("wl_user_hash", valor.toString());
                }
            }
            e.commit();
        }
    }
}
