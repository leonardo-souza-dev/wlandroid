package com.leonardoserra.watchlist.Bean;

import java.io.Serializable;

public class Filme implements Serializable {

    private String _id; //movieId
    private String titulo;
    private String tituloOriginal;
    private Boolean isInMyList;
    private String poster;
    private String urlPoster;
    private String dataLancamento;
    private Float popularidade;

    public String get_id() {
        return _id;
    }

    public void set_id(String p_id){
        this._id = p_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String pTitulo){
        this.titulo = pTitulo;
    }

    public String getTituloOriginal() {
        return this.tituloOriginal;
    }

    public Boolean getIsInMyList() {
        return isInMyList;
    }

    public void setIsInMyList(boolean esta){
        this.isInMyList = esta;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String pPoster){
        this.poster = pPoster;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public String getDataLancamento() {
        return this.dataLancamento;
    }

    public void setDataLancamento(String pDataLancamento){
        this.dataLancamento = pDataLancamento;
    }

    public Float getPopularidade() {
        return this.popularidade;
    }
}
