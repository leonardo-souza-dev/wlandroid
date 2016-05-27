package com.leonardoserra.watchlist;

import java.util.ArrayList;

public class RepositorioUsuario {

    public Usuario obterUsuario(int usuarioId) {

        Usuario usuario = new Usuario();
        usuario.definirNome("Leonardo");

        return usuario;
    }

    public ArrayList<Filme> obterMyWatchListt(int usuarioId) {
        ArrayList<Filme> lista = new ArrayList<>();
        Api api = new Api();

        lista.add(api.obterFilme(2));
        lista.add(api.obterFilme(3));

        return lista;
    }
}