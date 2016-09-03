package com.leonardoserra.watchlist;

/**
 * Created by leonardo on 02/09/16.
 */
public class RepositoryIterator implements Iterador {

    IRepository[] itens;
    int posicao = 0;

    public RepositoryIterator(IRepository[] repos){
        itens = repos;
    }

    public Object next(){
        IRepository iRepository = itens[posicao];
        posicao++;
        return iRepository;
    }

    public boolean hasNext() {
        if (posicao >= itens.length || itens[posicao] == null) {
            return false;
        } else {
            return true;
        }
    }

}
