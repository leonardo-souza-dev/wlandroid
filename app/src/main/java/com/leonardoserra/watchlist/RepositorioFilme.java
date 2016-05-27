package com.leonardoserra.watchlist;


import java.util.ArrayList;

public class RepositorioFilme {

    private Filme m2 = new Filme("Batman (1989)", 2);
    private Filme m3 = new Filme("Cidade de Deus (2002)", 3);
    private Filme m4 = new Filme("Domésticas - O Filme (2001)", 4);
    private Filme m19 = new Filme("The Dominici Affair", 19);

    public ArrayList<Filme> buscaFilmesPorTermo(String termo) {
        ArrayList<Filme> filmes = new ArrayList<>();

        ArrayList<Filme> todosOsFilmesDoBanco = this.obterTodosOsFilmesDoBanco(termo);

        for (Filme filmeDoBanco: todosOsFilmesDoBanco) {
            String nomeDoFilmeDoBanco = filmeDoBanco.obterNome();
            if (nomeDoFilmeDoBanco.contains(termo)) {
                filmes.add(filmeDoBanco);
            }
        }

        return filmes;
    }

    private ArrayList<Filme> obterTodosOsFilmesDoBanco(String termo) {
        ArrayList<Filme> filmes = new ArrayList<>();

        filmes.add(m2);
        filmes.add(m3);
        filmes.add(m4);
        filmes.add(m19);

        return filmes;
    }
}

