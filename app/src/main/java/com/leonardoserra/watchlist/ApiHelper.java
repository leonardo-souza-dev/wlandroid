package com.leonardoserra.watchlist;

import android.content.Intent;
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
    private final String AUTHENTICATE = "authenticate";

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

            gAction = params[0].toString();
            String termParam = params.length > 1 ? params[1] : "";

            if (gAction == SEARCH && termParam == "") {
                //Toast.makeText(MainActivity.this, "Insira um termo de busca", Toast.LENGTH_LONG).show();
                return null;
            }
            try {

                String uri = "http://10.0.2.2:8080/api/" + gAction;

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                JSONObject jsonParam = new JSONObject();

                if (gAction == AUTHENTICATE) {
                    jsonParam.put("hash", params[1]);
                    jsonParam.put("password", "");
                }

                if (gAction == "search") {
                    searchTerm = termParam.trim().replace(",", "").replace("-", "").replace(".", "");
                    jsonParam.put("searchterm", searchTerm);
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

                    return response.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
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

                if (gAction == AUTHENTICATE) {
                    JSONObject json = new JSONObject(s);
                    String temp = json.getString("token");
                    gToken = temp.toString();
                    return;
                }

                if (gAction == SEARCH) {
                    int len;
                    JSONArray jsonArray = new JSONArray(s);

                    if (jsonArray != null) {

                        ArrayList<Movie> list = new ArrayList<>();
                        len = jsonArray.length();

                        for (int i = 0; i < len; i++) {
                            String str = jsonArray.get(i).toString();
                            Movie f = new Gson().fromJson(str, Movie.class);
                            list.add(f);
                        }

//                        Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
//                        intent.putExtra("bundle_searchResult", list);
//                        intent.putExtra("termo", searchTerm);
//                        intent.putExtra("qtd", len);
//                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
