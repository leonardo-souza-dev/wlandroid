package com.leonardoserra.watchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class FirstActivity extends AppCompatActivity {

    private TextView termoTextView;
    private Api api;

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
        String termo = "";
        if (termoTextView.getText() != null)
            termo = termoTextView.getText().toString();
        else
            return;

        //solicita resultado da busca
        api = new Api();
        ArrayList<Filme> resultadoDaBusca = api.busca(termo, 13);

        //redireciona para tela de busca
        Intent intent = new Intent(this, ResultadoBuscaActivity.class);
        intent.putExtra("bundle_searchResult", resultadoDaBusca);
        intent.putExtra("termo", termo);
        intent.putExtra("qtd", resultadoDaBusca.size());
        startActivity(intent);
    }
}
