package com.leonardoserra.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MovieActivity extends AppCompatActivity {

    private Button btnAcao;
    private String jsonMyObject;
    private Movie myObject;
    private TextView tituloTextView;
    private boolean gIsInMyList;
    private String remove;
    private String add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        remove = getResources().getString(R.string.remove_movie).toString();
        add = getResources().getString(R.string.add_movie).toString();

        //receber dados do filme
        myObject = null;
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            jsonMyObject = savedInstanceState.getString("movieEntry");
            myObject = new Gson().fromJson(jsonMyObject, Movie.class);
        }

        //setando titulo do filme
        tituloTextView = (TextView) findViewById(R.id.txtMovieTitle);
        String titulo = myObject.getName();
        tituloTextView.setText(titulo);

        getSupportActionBar().setTitle(titulo);

        //setando botao de acao
        btnAcao = (Button)findViewById(R.id.btnAddRemove);
        //Boolean gIsInMyList = myObject.getEstaNaMinhaLista();
        gIsInMyList = false;

        btnAcao.setText(gIsInMyList ? remove : add);
    }

    public void addOrRemove(View view) {

        if (gIsInMyList) {
            Toast.makeText(this, "filme retirado da sua lista",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "filme adicionado à sua lista",Toast.LENGTH_LONG).show();
        }

        gIsInMyList = !gIsInMyList;

        btnAcao.setText(gIsInMyList ? remove : add);
    }
}
