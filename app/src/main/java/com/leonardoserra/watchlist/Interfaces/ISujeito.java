package com.leonardoserra.watchlist.Interfaces;

/**
 * Created by leonardo on 04/09/16.
 */
public interface ISujeito {

    void registrarObservador(IObservador o);
    void removerObservador(IObservador o);
    void notificarObservadores(String p, String v);
}
