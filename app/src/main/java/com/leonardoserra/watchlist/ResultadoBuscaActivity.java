package com.leonardoserra.watchlist;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;

import java.util.ArrayList;

public class ResultadoBuscaActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listView;
    private String termo;
    private MovieAdapter movieAdapter;
    private TextView txtFraseBusca;
    private Toolbar mToolbar;
    private ArrayList<MovieViewModel> resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca);

        bundle = getIntent().getExtras();
        listView = (ListView) findViewById(R.id.listViewResultadoBusca);
        termo = bundle.getString("resultadodabusca_termo");
        movieAdapter = new MovieAdapter(this, R.layout.simple_row, termo);

        Resources r = getResources();

        listView.setAdapter(movieAdapter);

        resultado = (ArrayList<MovieViewModel>) bundle.getSerializable("resultadodabusca_lista");

        for(final MovieViewModel entry : resultado) {
            movieAdapter.add(entry);
        }

        txtFraseBusca = (TextView) findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = r.getString(R.string.sua_busca_para);
        String retornou = r.getString(R.string.retornou);
        Integer qtd = resultado.size();
        String resultados = qtd == 1 ? r.getString(R.string.resultado) : r.getString(R.string.resultados);
        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);

        configuraActionbar();

        Log.d("nav", ">SEARCH_RESULT: " + termo + "|" + qtd);
    }

    private void configuraActionbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        String nomeApp = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //movieAdapter.onActivityResult(requestCode, resultCode, data);
        Bundle b = data.getExtras();

        for(int i = 0; i < resultado.size(); i++){
            if (resultado.get(i).get_id().equals(b.getString("filme2_filmeId"))){
                Boolean esta = b.getBoolean("filme2_estaNaMyListt");
                resultado.get(i).setIsInMyList(esta);
            }
        }
    }
}
