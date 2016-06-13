package com.leonardoserra.watchlist;

import android.content.Intent;
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
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    private TextView termoTextView;
    private Api api;
    private String termoStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nomeApp = getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);
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
//
//        //solicita resultado da busca
//        api = new Api();
//        ArrayList<Filme> resultadoDaBusca = api.busca(termoStr, 13);
//
//        //redireciona para tela de busca
//        Intent intent = new Intent(this, ResultadoBuscaActivity.class);
//        intent.putExtra("bundle_searchResult", resultadoDaBusca);
//        intent.putExtra("termoStr", termoStr);
//        intent.putExtra("qtd", resultadoDaBusca.size());
//        startActivity(intent);
    }

    private class BuscaFilmeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            if (params.length < 1 || params[0] == "" || params[0] == null)
                Toast.makeText(FirstActivity.this, "Insira um CEP", Toast.LENGTH_LONG).show();

            try {
                String termoBusca = params[0].trim().replace(",", "").replace("-", "").replace(".", "");

                String uri = "http://10.0.2.2:8080/api/search";

                URL url = new URL(uri);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("searchterm", termoBusca);

                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(jsonParam.toString());

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
            if (s == null)
                Toast.makeText(FirstActivity.this, "Erro ao buscar o Cep", Toast.LENGTH_LONG).show();

            try {
                int len;
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray != null) {

                    ArrayList<Filme> list = new ArrayList<>();
                    len = jsonArray.length();

                    for (int i=0;i<len;i++) {
                        String str = jsonArray.get(i).toString();
                        Filme f = new Gson().fromJson(str, Filme.class);
                        list.add(f);
                    }

                    Intent intent = new Intent(getBaseContext(), ResultadoBuscaActivity.class);
                    intent.putExtra("bundle_searchResult", list);
                    intent.putExtra("termoStr", termoStr);
                    intent.putExtra("qtd", len);
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
