package com.leonardoserra.watchlist;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper  {

    private String gHash;
    private String gToken;
    private String gAction;

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String ADDMOVIE = "addmovie";
    private final String REMOVEMOVIE = "removemovie";

    public Message createuser() {
        String[] params = {CREATEUSER};

        return callSync(params);
    }

    public Message addMovie(String pHash, String pMovieId) {
        String[] lParameters = {ADDMOVIE, pHash, pMovieId };
        Message msg = callSync(lParameters);

        return msg;
    }

    public Message removeMovie(String pHash, String pMovieId) {
        String[] lParameters = {REMOVEMOVIE, pHash, pMovieId };
        Message msg = callSync(lParameters);

        return msg;
    }

    public Message search(User user, String term) {
        String[] lParameters = { SEARCH, user.getHash(), term};
        Message msg = callSync(lParameters);

        return msg;
    }

    public Message callSync(String... params) {
        Message msg;
        try {
            WLWebApi api = new WLWebApi();
            String response = api.execute(params).get();

            JSONObject hashToken = new JSONObject(response);

            boolean success = Boolean.parseBoolean(hashToken.getString("success"));
            String message = hashToken.getString("message");
            JSONObject object = hashToken.getJSONObject("object");

            msg = new Message(success, message, object);
        } catch (Exception e) {
            msg = new Message(false, "Some error occurred!", null);
            e.printStackTrace();
        }

        return msg;
    }

    private class WLWebApi extends AsyncTask<String, Void, String> {

        private String searchTerm;

        @Override
        protected String doInBackground(String... params) {

            String responseStr = "", lHash = "";

            try {

                gAction = params[0];
                lHash = (gAction == SEARCH || gAction == ADDMOVIE || gAction == REMOVEMOVIE) ? params[1] : "";
                String uri = "http://10.0.2.2:8080/api/" + gAction;

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonParam = new JSONObject();

                if (gAction == SEARCH) {
                    if (params[2].equals("")) {
                        return null;
                    }
                    searchTerm = params[2].trim().replace(",", "").replace("-", "").replace(".", "");
                    jsonParam.put("searchterm", searchTerm);
                    jsonParam.put("hash", lHash);
                } else if (gAction == ADDMOVIE || gAction == REMOVEMOVIE) {
                    jsonParam.put("movieid", params[2]);
                    jsonParam.put("hash", lHash);
                }

                byte[] outputBytes = jsonParam.toString().getBytes();
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);

                if (connection.getResponseCode() == 200) {
                    BufferedReader stream =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = "";
                    StringBuilder response = new StringBuilder();

                    while ((line = stream.readLine()) != null) {
                        response.append(line);
                    }

                    connection.disconnect();

                    responseStr = response.toString();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return responseStr;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (s == null) {
                //Toast.makeText(MainActivity.this, "Erro ao buscar", Toast.LENGTH_LONG).show();
                return;
            }

            try {

                if (gAction == CREATEUSER) {
                    JSONObject json = new JSONObject(s);
                    String temp  = json.getString("hash");
                    gHash = temp.toString();
                    return;
                }

                if (gAction == SEARCH) {
                    int len;
                    JSONArray jsonArray = new JSONArray(s);

                    if (jsonArray != null) {

                        MoviesViewModel f = new MoviesViewModel();
                        len = jsonArray.length();

                        for (int i = 0; i < len; i++) {
                            String str = jsonArray.get(i).toString();
                            f = new Gson().fromJson(str, MoviesViewModel.class);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
