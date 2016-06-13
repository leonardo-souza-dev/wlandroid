package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class Usuario {

    private ArrayList<Movie> myMovies;
    private String nome;

    public Usuario() {
        this.myMovies = new ArrayList();
    }

    public void adicionaFilme(Movie pMovie) {
        this.myMovies.add(pMovie);
    }

    public void removeFilme(Movie pMovie) {
        this.myMovies.remove(pMovie);
    }

    public String obterNome() {
        return this.nome;
    }

    public void definirNome(String pNome) {
        this.nome = pNome;
    }
}
