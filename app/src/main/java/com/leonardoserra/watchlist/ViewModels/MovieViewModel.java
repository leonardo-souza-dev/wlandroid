package com.leonardoserra.watchlist.ViewModels;

import java.io.Serializable;

public class MovieViewModel implements Serializable, Cloneable {

    private String _id; //movieId
    private String name;
    private Boolean isInMyList;
    private String poster;
    private int ano;
    //private User user;

    public MovieViewModel(String _id, Boolean pIsInMyList) {
        this._id = _id;
        this.isInMyList = pIsInMyList;
    }

    public MovieViewModel() {
    }


    public String getNome() {
        return this.name;
    }

    public void setNome(String pNome){
        name = pNome;
    }


    public int getAno() {
        return this.ano;
    }

    public void setAno(int pAno){
        ano = pAno;
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



    /*public void setUser(User user) {
        this.user = user;
    }*/
}

