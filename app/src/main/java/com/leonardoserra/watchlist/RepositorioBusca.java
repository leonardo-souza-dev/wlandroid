package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class RepositorioBusca {

    public ArrayList<Filme> buscaFilmesPorTermo(String termo, int usuarioId) {

        ArrayList<Filme> filmesEncontrados = new RepositorioFilme().buscaFilmesPorTermo(termo);
        ArrayList<Filme> meusFilmes = new RepositorioUsuario().obterMyWatchListt(usuarioId);

        for(Filme filmeEncontrado: filmesEncontrados) {
            if (jaExisteNaLista(filmeEncontrado, meusFilmes)) {
                filmeEncontrado.setEstaNaMinhaLista(true);
            }
        }

        return filmesEncontrados;
    }

    private Boolean jaExisteNaLista(Filme filme, ArrayList<Filme> lista) {
        int idFilmeEncontrado = filme.obterId();
        for(Filme meuFilme: lista) {
            int idMeuFilme = meuFilme.obterId();
            if (idFilmeEncontrado == idMeuFilme) {
                return true;
            }
        }
        return false;
    }
}


