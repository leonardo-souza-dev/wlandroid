package com.leonardoserra.watchlist;

import android.os.AsyncTask;
import android.util.Base64;

import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.ViewModels.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiHelper {

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String ADDMOVIE = "addmovie";
    private final String REMOVEMOVIE = "removemovie";
    private final String OBTERMYLISTT = "obtermylistt";
    private final String OBTERFILMESRECOMENDADOS = "obterfilmesrecomendados";
    private final String ENVIARLOG = "enviarlog";

    private final String UNKNOWHOSTEXCEPTION = "unknowhostexception";

    public ApiHelper() {
    }

    public void enviarLog(String msg){
        String[] parametros = {ENVIARLOG, msg};
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

    public Message adicionaFilme(String pHash, Filme filme) {
        String[] lParameters = {ADDMOVIE, pHash, filme.get_id() };
        Message msg = call(true, lParameters);

        return msg;
    }

    public Message removeMovie(String pHash, Filme filme) {
        String[] lParameters = {REMOVEMOVIE, pHash, filme.get_id() };
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

            ApiHelper.WLWebApi api = new WLWebApi();
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
            msg = new Message(false, "Some error occurred.", null);
            e.printStackTrace();
        }

        return msg;
    }

    private class WLWebApi extends AsyncTask<String, Void, String> {

        private String searchTerm;

        @Override
        protected String doInBackground(String... params) {

            String responseStr = "", lHash = "";
            String baseUrlApi = "";
            baseUrlApi= Singleton.getInstance().obterUrlBaseApi() + "/";
            HttpURLConnection connection = null;
            try {

                String action = params[0];
                lHash = (action == OBTERFILMESRECOMENDADOS || action == OBTERMYLISTT ||
                        action == CREATEUSER || action == SEARCH || action == ADDMOVIE ||
                        action == REMOVEMOVIE || action == ENVIARLOG) ? params[1] : "";


                String uri = baseUrlApi + action;

                URL url = new URL(uri);

                connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                final String basicAuth = "Basic " + Base64.encodeToString("asd:watxi1izTTPWD*".getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty("Authorization", basicAuth);

                JSONObject jsonParam = new JSONObject();

                if (action == SEARCH) {
                    if (params[2].equals("")) {
                        return null;
                    }
                    searchTerm = params[2].trim().replace(",", "").replace("-", "").replace(".", "");
                    jsonParam.put("termo", searchTerm);
                    jsonParam.put("hash", lHash);
                } else if (action == ADDMOVIE || action == REMOVEMOVIE) {
                    jsonParam.put("movieid", params[2]);
                    jsonParam.put("hash", lHash);
                } else if (action == CREATEUSER || action == OBTERMYLISTT || action == OBTERFILMESRECOMENDADOS) {
                    jsonParam.put("hash", lHash);
                } else if (action == ENVIARLOG) {
                    jsonParam.put("hash", lHash);
                    jsonParam.put("logmsg", params[1]);
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
                //Singleton.getInstance().enviarLogException(e);
            }catch (Exception e) {
                e.printStackTrace();
                //Singleton.getInstance().enviarLogException(e);
            } finally {
                connection.disconnect();
                return responseStr;
            }
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

}
