package com.leonardoserra.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Created by leonardo on 15/05/16.
 */
public class DetalheFilmeActivity extends AppCompatActivity {

    private Button btnAcao;
    private String jsonMyObject;
    private Filme myObject;
    private TextView tituloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme_detalhe);

        //receber filme da tela principal
        myObject = null;
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            jsonMyObject = savedInstanceState.getString("movieEntry");
            myObject = new Gson().fromJson(jsonMyObject, Filme.class);
        }

        //setando titulo do filme
        tituloTextView = (TextView) findViewById(R.id.txtMovieTitle);
        String titulo = myObject.obterNome();
        tituloTextView.setText(titulo);

        btnAcao = (Button)findViewById(R.id.btnAddRemove);
        Boolean estaNaLista = myObject.getEstaNaMinhaLista();

        String remove = getResources().getString(R.string.remove_filme).toString();
        String adiciona = getResources().getString(R.string.adiciona_filme).toString();

        btnAcao.setText(estaNaLista ? remove : adiciona);
    }

    public void adicionaOuRemove(View view) {

    }
}
