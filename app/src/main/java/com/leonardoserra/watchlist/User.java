package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class User {

    private ArrayList<Movie> myMovies;
    private String nome;

    public User() {
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