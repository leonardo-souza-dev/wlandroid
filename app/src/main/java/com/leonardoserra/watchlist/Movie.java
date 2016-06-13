package com.leonardoserra.watchlist;

import java.io.Serializable;

/**
 * Created by leonardo on 26/05/16.
 */
public class Movie implements Serializable {

    private String name;
    private int id;
    private Boolean estaNaMinhaLista;

    public Movie(String pNome, int pId) {
        this.name = pNome;
        this.id = pId;
        this.estaNaMinhaLista = false;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public Boolean getEstaNaMinhaLista() {
        return this.estaNaMinhaLista;
    }

    public void setEstaNaMinhaLista(Boolean esta) {
        this.estaNaMinhaLista = esta;
    }
}
