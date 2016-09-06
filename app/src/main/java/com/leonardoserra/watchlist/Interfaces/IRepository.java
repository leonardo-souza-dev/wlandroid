package com.leonardoserra.watchlist.Interfaces;

import com.leonardoserra.watchlist.Domain.Filme;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */

public interface IRepository extends IObservador {

    String criarOuObterUsuario(String usuario);
    ArrayList<Filme> obterMyListt();
    ArrayList<Filme> buscar(String termo);
    boolean removerFilme(Filme filme);
    boolean adicionarFilme(Filme filme);

}
