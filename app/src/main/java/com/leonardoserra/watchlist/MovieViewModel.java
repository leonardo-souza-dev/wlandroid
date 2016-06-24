package com.leonardoserra.watchlist;

import java.io.Serializable;

/**
 * Created by leonardo on 26/05/16.
 */
public class MovieViewModel implements Serializable {

    private String name;
    private String _id;
    private Boolean estaNaMinhaLista;

    public MovieViewModel(String pNome, String pId) {
        this.name = pNome;
        this._id = pId;
        this.estaNaMinhaLista = false;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this._id;
    }

    public Boolean getEstaNaMinhaLista() {
        return this.estaNaMinhaLista;
    }

    public void setEstaNaMinhaLista(Boolean esta) {
        this.estaNaMinhaLista = esta;
    }
}

