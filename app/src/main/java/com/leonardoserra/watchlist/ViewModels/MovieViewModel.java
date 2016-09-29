package com.leonardoserra.watchlist.ViewModels;

import java.io.Serializable;

public class MovieViewModel implements Serializable, Cloneable {

    private String _id; //movieId
    private String titulo;
    private String tituloOriginal;
    private Boolean isInMyList;
    private String poster;
    private String dataLancamento;

    public MovieViewModel(String _id, Boolean pIsInMyList) {
        this._id = _id;
        this.isInMyList = pIsInMyList;
    }

    public MovieViewModel() {
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getTituloOriginal() {
        return this.tituloOriginal;
    }

    public void setTitulo(String pNome){
        titulo = pNome;
    }

    public void setTituloOriginal(String pTituloOriginal){
        this.tituloOriginal = pTituloOriginal;
    }


    public String getDataLancamento() {
        return this.dataLancamento;
    }

    public void setDataLancamento(String pAno){
        dataLancamento = pAno;
    }


    public String get_id() {
        return this._id;
    }

    public void set_id(String pId){
        _id = pId;
    }


    public Boolean getIsInMyList() {
        return this.isInMyList;
    }

    public void setIsInMyList(Boolean esta) {
        this.isInMyList = esta;
    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String pPoster){
        poster = pPoster;
    }


}

