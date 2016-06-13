package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class RepositorioBusca {

    public ArrayList<Movie> buscaFilmesPorTermo(String termo, int usuarioId) {

        ArrayList<Movie> filmesEncontrados = new RepositorioFilme().buscaFilmesPorTermo(termo);
        ArrayList<Movie> myMovies = new RepositorioUsuario().obterMyWatchListt(usuarioId);

        for(Movie movieEncontrado : filmesEncontrados) {
            if (jaExisteNaLista(movieEncontrado, myMovies)) {
                movieEncontrado.setEstaNaMinhaLista(true);
            }
        }

        return filmesEncontrados;
    }

    private Boolean jaExisteNaLista(Movie movie, ArrayList<Movie> lista) {
        int idFilmeEncontrado = movie.getId();
        for(Movie myMovie : lista) {
            int idMeuFilme = myMovie.getId();
            if (idFilmeEncontrado == idMeuFilme) {
                return true;
            }
        }
        return false;
    }
}


