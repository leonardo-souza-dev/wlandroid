package com.leonardoserra.watchlist;

import java.io.Serializable;

/**
 * Created by leonardo on 26/05/16.
 */
public class MovieViewModel implements Serializable {

    private String name;
    private String _id; //movieId
    private Boolean isInMyList;
    private User user;
    private String poster;

    public MovieViewModel(String _id, Boolean pIsInMyList) {
        this._id = _id;
        this.isInMyList = pIsInMyList;
    }

    public String getName() {
        return this.name;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String get_id() {
        return this._id;
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

    public void setPoster(String poster) {
        this.poster = poster;
    }
}

