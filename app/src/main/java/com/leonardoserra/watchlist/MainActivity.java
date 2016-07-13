package com.leonardoserra.watchlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    private TextView termoTextView;
    private String searchTerm;

    private String gHash;
    private String gToken;
    private String gAction;

    private final String SEARCH = "search";
    private final String CREATEUSER = "createuser";
    private final String AUTHENTICATE = "authenticate";
    private User gUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nomeApp = getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String lHash;

        try {
            gUser = new User();

            sp = getPreferences(MODE_PRIVATE);
            lHash = sp.getString("wl_user_hash", null);
            Boolean estaRegistrado = lHash != null;

            if (!estaRegistrado) {

                Message msgCreateUser = new ApiHelper().createuser();

                if (msgCreateUser.getSucess()) {

                    lHash = msgCreateUser.getObject("hash");
                    e.putString("wl_user_hash", lHash);
                    e.commit();



                    Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_SHORT).show();//"novo usuario"

                } else {
                    Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_LONG).show();//"erro ao obter o hash"
                    return;
                }
            }

            gUser.setHash(lHash);
            TextView txtHash = (TextView)findViewById(R.id.txtHash);
            txtHash.setText(lHash);

            return;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void search(View view) {
        //obtem ter_mo da busca
        termoTextView = (TextView)findViewById(R.id.txtTerm);
        searchTerm = "";
        if (!termoTextView.getText().equals(""))
            searchTerm = termoTextView.getText().toString();
        else
            return;

        Message msg = new ApiHelper().search(gUser, searchTerm);

        if (msg.getSucess()) {

            try {

                int len;
                JSONObject object = msg.getObject();
                JSONArray jsonArray = object.getJSONArray("movies");

                if (jsonArray != null) {

                    ArrayList<MovieViewModel> list = new ArrayList<>();
                    len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        String str = jsonArray.get(i).toString();
                        MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                        list.add(f);
                    }

                    Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                    intent.putExtra("bundle_searchResult", list);
                    intent.putExtra("termo", searchTerm);
                    intent.putExtra("qtd", len);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //WLWebApi task = new WLWebApi();
        //task.execute(SEARCH, searchTerm);
    }

    /*
    private class WLWebApi extends AsyncTask<String, Void, String> {

        private String searchTerm;

        @Override
        protected String doInBackground(String... params) {

            gAction = params[0].toString();
            String termParam = params.length == 2 ? params[1] : "";

            if (gAction == SEARCH && termParam == "") {
                Toast.makeText(MainActivity.this, "Insira um termo de busca", Toast.LENGTH_LONG).show();
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
                    jsonParam.put("hash", gHash);
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
                Toast.makeText(MainActivity.this, "Erro ao buscar", Toast.LENGTH_LONG).show();
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

                        ArrayList<MovieViewModel> list = new ArrayList<>();
                        len = jsonArray.length();

                        for (int i = 0; i < len; i++) {
                            String str = jsonArray.get(i).toString();
                            MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                            list.add(f);
                        }

                        Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                        intent.putExtra("bundle_searchResult", list);
                        intent.putExtra("termo", searchTerm);
                        intent.putExtra("qtd", len);
                        startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } */
}
