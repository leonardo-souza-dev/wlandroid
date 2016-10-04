package com.leonardoserra.watchlist.Interfaces;

public interface ISujeito {
    void registrarObservador(IObservador o);
    void removerObservador(IObservador o);
    void notificarObservadores(String p, Object v);
}
