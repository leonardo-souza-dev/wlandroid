package com.leonardoserra.watchlist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leonardo on 24/06/16.
 */
public class MoviesViewModel implements Serializable {

    private ArrayList<MovieViewModel> movies;
    private User user;
    //private String hash;
    //private String token;


    public ArrayList<MovieViewModel> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<MovieViewModel> movies) {
        this.movies = movies;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

/*    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getToken() {
        return token;
    }
I
    public void setToken(String token) {
        this.token = token;
    }
    */
}
