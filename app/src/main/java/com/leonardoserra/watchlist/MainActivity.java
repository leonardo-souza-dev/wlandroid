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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView termoTextView;
    private String termoStr;
    private String gUserToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nomeApp = getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);

        try {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            String userToken = sp.getString("wl_user_token", null);

            if (userToken == null) {

                SharedPreferences.Editor e = sp.edit();
                String token_generated = java.util.UUID.randomUUID().toString();
                gUserToken = token_generated;
                e.putString("wl_user_token", token_generated);
                e.commit();

            } else {

                gUserToken = userToken;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void search(View view) {
        //obtem ter_mo da busca
        termoTextView = (TextView)findViewById(R.id.txtTerm);
        termoStr = "";
        if (!termoTextView.getText().equals(""))
            termoStr = termoTextView.getText().toString();
        else
            return;

        BuscaFilmeTask task = new BuscaFilmeTask();
        task.execute(termoStr);
    }

    private class BuscaFilmeTask extends AsyncTask<String, Void, String> {

        private String termoBusca;

        @Override
        protected String doInBackground(String... params) {

            if (params.length < 1 || params[0] == "" || params[0] == null) {
                Toast.makeText(MainActivity.this, "Insira um termo de busca", Toast.LENGTH_LONG).show();
                return null;
            }
            try {
                termoBusca = params[0].trim().replace(",", "").replace("-", "").replace(".", "");

                String uri = "http://10.0.2.2:8080/api/search";

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("searchterm", termoBusca);

                byte[] outputBytes = jsonParam.toString().getBytes();
                //String output = "{'searchtem': '"+ termoBusca +"'} ";
                //byte[] outputBytes = output.getBytes();
                OutputStream os = connection.getOutputStream();
                os.write(outputBytes);
                //os.close();

                if (connection.getResponseCode() == 200) {
                    BufferedReader stream =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String linha = "";
                    StringBuilder resposta = new StringBuilder();

                    while ((linha = stream.readLine()) != null) {
                        resposta.append(linha);
                    }

                    connection.disconnect();

                    return resposta.toString();
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
                int len;
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray != null) {

                    ArrayList<Movie> list = new ArrayList<>();
                    len = jsonArray.length();

                    for (int i=0;i<len;i++) {
                        String str = jsonArray.get(i).toString();
                        Movie f = new Gson().fromJson(str, Movie.class);
                        list.add(f);
                    }

                    Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                    intent.putExtra("bundle_searchResult", list);
                    intent.putExtra("termo", termoBusca);
                    intent.putExtra("qtd", len);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
