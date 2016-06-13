package com.leonardoserra.watchlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView termoTextView;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeira);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        ArrayList<Movie> resultadoDaBusca = api.busca(termo, 13);

        //redireciona para tela de busca
        Intent intent = new Intent(this, ResultadoBuscaActivity.class);
        intent.putExtra("bundle_searchResult", resultadoDaBusca);
        startActivity(intent);
    }
}
