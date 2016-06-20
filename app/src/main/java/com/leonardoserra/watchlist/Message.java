package com.leonardoserra.watchlist;

import org.json.JSONObject;

/**
 * Created by leonardo on 18/06/16.
 */
public final class Message {

    private final boolean sucess;
    private final String message;
    private final JSONObject object;

    public Message(final boolean s, final String m, final JSONObject o) {
        this.sucess = s;
        this.message = m;
        this.object = o;
    }

    public boolean getSucess() {
        return this.sucess;
    }

    public String getMessage() {
        return this.message;
    }

    public JSONObject getObject() {
        return this.object;
    }

    public String getObject(String key) throws Exception {
        return this.object.getString(key);
    }
}
