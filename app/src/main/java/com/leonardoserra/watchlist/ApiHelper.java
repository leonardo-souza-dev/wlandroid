package com.leonardoserra.watchlist;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by leonardo on 18/06/16.
 */
public class ApiHelper  {

    private TextView termoTextView;
    private String termoStr;

    private String gHash;
    private String gToken;
    private String gAction;

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String UPDATEUSER = "updateuser";
    private final String AUTHENTICATE = "authenticate";

    public Message createuser() {
        String[] params = {CREATEUSER};

        return callSync(params);
    }

    public Message update(String pToken, User pUser) {
        String[] lParameters = {pToken, UPDATEUSER, new Gson().toJson(pUser)};
        return callSync(lParameters);
    }

    public Message search(User user, String term) {
        //Gson gson = new Gson();
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
                lHash = gAction == SEARCH ? params[1] : "";
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
                            //list.add(f);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
