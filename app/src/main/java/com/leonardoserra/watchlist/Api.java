package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class Api {

    private Movie m2 = new Movie("Batman (1989)", 2);
    private Movie m3 = new Movie("Cidade de Deus (2002)", 3);
    private Movie m4 = new Movie("Domésticas - O Movie (2001)", 4);
    private Movie m19 = new Movie("The Dominici Affair", 19);

    public Usuario obterUsuario(int usuarioId) {

        Usuario usuario = new Usuario();
        usuario.definirNome("Leonardo");

        return usuario;
    }

    public ArrayList<Movie> busca(String termo, int usuarioId) {

        RepositorioBusca http = new RepositorioBusca();
        ArrayList<Movie> retorno = http.buscaFilmesPorTermo(termo, usuarioId);
        return retorno;
    }

    public Movie obterFilme(int id) {
        switch (id) {
            case 2:
                return m2;
            case 3:
                return m3;
            case 4:
                return m4;
            case 19:
                return m19;
            default:
                return null;
        }
    }

    public ArrayList<Movie> buscaFilmes(String termo) {
        ArrayList<Movie> movies = new ArrayList<>();

        ArrayList<Movie> todosOsFilmesDoBanco = this.obterTodosOsFilmesDoBanco(termo);

        for (Movie movieDoBanco : todosOsFilmesDoBanco) {
            String nomeDoFilmeDoBanco = movieDoBanco.getName();
            if (nomeDoFilmeDoBanco.contains(termo)) {
                movies.add(movieDoBanco);
            }
        }

        return movies;
    }

    private ArrayList<Movie> obterTodosOsFilmesDoBanco(String termo) {
        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(m2);
        movies.add(m3);
        movies.add(m4);
        movies.add(m19);

        return movies;
    }
}
