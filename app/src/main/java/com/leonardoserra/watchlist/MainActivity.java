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
    private String termoStr;
    private String gUserHash;
    private String gUserToken;
    private String gAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nomeApp = getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);

        try {
            //tenta obter o hash do usuario
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            String userHash = sp.getString("wl_user_hash", null);
            String userToken = sp.getString("wl_user_token", null);

            SharedPreferences.Editor e = sp.edit();
            BuscaFilmeTask task = new BuscaFilmeTask();
            
            if (userHash == null) {

                //se nao encontra nenhum hash, gera um na web api
                String jsonHash = task.execute("createuser").get();
                JSONObject hashJson = new JSONObject(jsonHash);
                gUserHash = hashJson.getString("hash").toString();
                e.putString("wl_user_hash", gUserHash);
            } else {
                gUserHash = userHash;
            }
            
            String jsonToken = task.execute("authenticate", gUserHash).get();
            JSONObject hashToken = new JSONObject(jsonToken);
            gUserToken = hashJson.getString("token").toString();
            e.putString("wl_user_token", gUserToken);
            
            e.commit();

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
        task.execute("search", termoStr);
    }

    private class BuscaFilmeTask extends AsyncTask<String, Void, String> {

        private String termoBusca;

        @Override
        protected String doInBackground(String... params) {

            gAction = params[0].toString();
            String termParam = params.length == 2 ? params[1] : "";

            if (gAction == "search" && termParam == "") {
                Toast.makeText(MainActivity.this, "Insira um termo de busca", Toast.LENGTH_LONG).show();
                return null;
            }
            try {
                termoBusca = termParam.trim().replace(",", "").replace("-", "").replace(".", "");

                String uri = "http://10.0.2.2:8080/api/" + gAction;

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");

                if (gAction == "search") {
                    connection.setRequestProperty("Content-Type", "application/json");


                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("searchterm", termoBusca);

                    byte[] outputBytes = jsonParam.toString().getBytes();
                    OutputStream os = connection.getOutputStream();
                    os.write(outputBytes);
                }

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

                if (gAction == "createuser"){
                    JSONObject hashJson = new JSONObject(s);
                    gUserHash = hashJson.getString("hash").toString();

                    return;
                }

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
