package com.leonardoserra.watchlist;

import java.io.Serializable;

/**
 * Created by leonardo on 26/05/16.
 */
public class Filme implements Serializable {

    private String nome;
    private int id;
    private Boolean estaNaMinhaLista;

    public Filme(String pNome, int pId) {
        this.nome = pNome;
        this.id = pId;
        this.estaNaMinhaLista = false;
    }

    public String obterNome() {
        return this.nome;
    }

    public int obterId() {
        return this.id;
    }

    public Boolean getEstaNaMinhaLista() {
        return this.estaNaMinhaLista;
    }

    public void setEstaNaMinhaLista(Boolean esta) {
        this.estaNaMinhaLista = esta;
    }
}
