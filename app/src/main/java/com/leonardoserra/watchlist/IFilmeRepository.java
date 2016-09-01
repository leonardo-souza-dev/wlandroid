package com.leonardoserra.watchlist;

import com.leonardoserra.watchlist.Domain.Filme;

import java.util.ArrayList;

/**
 * Created by leonardo on 01/09/16.
 */

public interface IFilmeRepository{

    ArrayList<Filme> busca(String termo);
    ArrayList<Filme> obterMyListt();
}
