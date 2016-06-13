package com.leonardoserra.watchlist;


import java.util.ArrayList;

public class RepositorioFilme {

    private Movie m2 = new Movie("Batman (1989)", 2);
    private Movie m3 = new Movie("Cidade de Deus (2002)", 3);
    private Movie m4 = new Movie("Domésticas - O Movie (2001)", 4);
    private Movie m19 = new Movie("The Dominici Affair", 19);

    public ArrayList<Movie> buscaFilmesPorTermo(String termo) {
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

