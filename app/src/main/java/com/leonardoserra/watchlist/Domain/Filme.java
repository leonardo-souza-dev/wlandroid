package com.leonardoserra.watchlist.Domain;

import com.leonardoserra.watchlist.ViewModels.User;

import java.io.Serializable;

/**
 * Created by leonardo on 01/09/16.
 */
public class Filme implements Serializable {

    private String _id; //movieId
    private String name;
    private Boolean isInMyList;
    private String poster;
    private int ano;

    public String get_id() {
        return _id;
    }

    public void set_id(String p_id){
        this._id = p_id;
    }

    public String getName() {
        return name;
    }

    public void setNome(String pNome){
        this.name = pNome;
    }

    public Boolean getIsInMyList() {
        return isInMyList;
    }

    public void setEstaNaMinhaLista(boolean esta){
        this.isInMyList = esta;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String pPoster){
        this.poster = pPoster;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int pAno){
        this.ano = pAno;
    }
}
