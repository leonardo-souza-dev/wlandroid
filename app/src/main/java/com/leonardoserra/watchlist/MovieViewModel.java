package com.leonardoserra.watchlist;

import java.io.Serializable;

public class MovieViewModel implements Serializable {
    private String name;
    private int movieId;
    private Boolean isInMyWatchListt;

    public String getName(){
        return this.name;
    }

    public int getMovieId(){
        return this.movieId;
    }

    public Boolean estaNaLista() {
        return this.isInMyWatchListt;
    }

    public void setIsInMyList(Boolean value) {
        this.isInMyWatchListt = value;
    }

    public String getImgName() {
        String imgName = Integer.toString(this.movieId);
        return imgName;
    }

    public MovieViewModel(String pName, int pMovieId) {
        this.name = pName;
        this.movieId = pMovieId;
        this.isInMyWatchListt = false;
    }
}
