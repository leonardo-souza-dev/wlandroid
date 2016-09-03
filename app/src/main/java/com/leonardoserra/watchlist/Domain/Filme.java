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

    public String getName() {
        return name;
    }

    public Boolean getIsInMyList() {
        return isInMyList;
    }

    public String getPoster() {
        return poster;
    }

    public int getAno() {
        return ano;
    }
}
