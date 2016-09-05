package com.leonardoserra.watchlist.Interfaces;

import com.leonardoserra.watchlist.Domain.Filme;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leonardo on 02/09/16.
 */

public interface IITerador {
    boolean hasNext();
    Object next();
}
