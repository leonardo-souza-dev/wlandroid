package com.leonardoserra.watchlist.Helpers;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    private Context context;
    private String action;

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String ADDMOVIE = "addmovie";
    private final String REMOVEMOVIE = "removemovie";
    private final String OBTERMYLISTT = "obtermylistt";
    private final String OBTERFILMESRECOMENDADOS = "obterfilmesrecomendados";
    private final String ENVIARLOG = "enviarlog";

    private final String UNKNOWHOSTEXCEPTION = "unknowhostexception";

    public ApiHelper(Context current) {
        context = current;
    }

    public ApiHelper() {
    }

    private Resources getResources(){
        return context.getResources();
    }

    public void enviarLog(String msg, String pHash){
        String[] parametros = {ENVIARLOG, pHash};
        Message message = call(true, parametros);
    }

    public Message obterFilmesRecomendados(String pHash) {
        String[] lParameters = {OBTERFILMESRECOMENDADOS, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }
    public Message obterMyListt(String pHash) {
        String[] lParameters = {OBTERMYLISTT, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message createuser(String pHash) {
        String[] lParameters = {CREATEUSER, pHash};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message addMovie(String pHash, String pMovieId) {
        String[] lParameters = {ADDMOVIE, pHash, pMovieId };
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message removeMovie(String pHash, String pMovieId) {
        String[] lParameters = {REMOVEMOVIE, pHash, pMovieId };
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message search(String hash, String term) {
        String[] lParameters = { SEARCH, hash, term};
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message call(boolean sync, String... params) {

        Message msg;

        try {

            WLWebApi api = new WLWebApi();
            String response = sync ? api.execute(params).get() : null;

            if (!response.equals(UNKNOWHOSTEXCEPTION)) {

                JSONObject hashToken = new JSONObject(response);

                boolean success = Boolean.parseBoolean(hashToken.getString("success"));
                String message = hashToken.getString("message");
                JSONObject object = hashToken.getJSONObject("object");

                msg = new Message(success, message, object);
            } else {
                msg = new Message(false, UNKNOWHOSTEXCEPTION, null);
            }
        } catch (Exception e) {

            msg = new Message(false, getResources().getString(R.string.some_error_occurred), null);
            e.printStackTrace();
        }

        return msg;
    }

    private class WLWebApi extends AsyncTask<String, Void, String> {

        private String searchTerm;

        @Override
        protected String doInBackground(String... params) {

            String responseStr = "", lHash = "";
            String baseUrlApi = Singleton.getInstance().obterUrlBaseApi() + "/";

            try {

                action = params[0];
                lHash = (action == OBTERFILMESRECOMENDADOS || action == OBTERMYLISTT ||
                        action == CREATEUSER || action == SEARCH || action == ADDMOVIE ||
                        action == REMOVEMOVIE || action == ENVIARLOG) ? params[1] : "";
                String uri = baseUrlApi + action;

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonParam = new JSONObject();

                if (action == SEARCH) {
                    if (params[2].equals("")) {
                        return null;
                    }
                    searchTerm = params[2].trim().replace(",", "").replace("-", "").replace(".", "");
                    jsonParam.put("searchterm", searchTerm);
                    jsonParam.put("hash", lHash);
                } else if (action == ADDMOVIE || action == REMOVEMOVIE) {
                    jsonParam.put("movieid", params[2]);
                    jsonParam.put("hash", lHash);
                } else if (action == CREATEUSER || action == OBTERMYLISTT || action == OBTERFILMESRECOMENDADOS) {
                    jsonParam.put("hash", lHash);
                } else if (action == ENVIARLOG) {
                    jsonParam.put("hash", lHash);
                    jsonParam.put("logmsg", params[2]);
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
            } catch (java.net.UnknownHostException e) {
                e.printStackTrace();
                responseStr = UNKNOWHOSTEXCEPTION;
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                return responseStr;
            }
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
