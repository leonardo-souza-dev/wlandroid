package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class Usuario {

    private ArrayList<Filme> meusFilmes;
    private String nome;

    public Usuario() {
        this.meusFilmes = new ArrayList();
    }

    public void adicionaFilme(Filme pFilme) {
        this.meusFilmes.add(pFilme);
    }

    public void removeFilme(Filme pFilme) {
        this.meusFilmes.remove(pFilme);
    }

    public String obterNome() {
        return this.nome;
    }

    public void definirNome(String pNome) {
        this.nome = pNome;
    }
}
