package com.leonardoserra.watchlist;

import com.leonardoserra.watchlist.Domain.Filme;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */
public class FilmeBusiness {

    private FilmeRepository repository;

    public FilmeBusiness(String pHash){
        repository = new FilmeRepository(pHash);
    }

    public ArrayList<Filme> busca(String termo){
        return repository.busca(termo);
    }

    public ArrayList<Filme> obterMyListt(){
        return repository.obterMyListt();
    }
}
