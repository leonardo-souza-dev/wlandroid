package com.leonardoserra.watchlist;

import com.leonardoserra.watchlist.Domain.Filme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leonardo on 02/09/16.
 */

public interface Iterador {
    boolean hasNext();
    Object next();
}
