package com.leonardoserra.watchlist;

import android.widget.TextView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private TextView txtFraseBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String nomeApp = getResources().getString(R.string.resultado_busca);
        getSupportActionBar().setTitle(nomeApp);

        //mostra listagem da busca
        final ListView newsEntryListView = (ListView) findViewById(R.id.listView);

        String termo = getIntent().getStringExtra("termo");

        final MovieAdapter fruitEntryAdapter = new MovieAdapter(this, R.layout.simple_row, termo);
        newsEntryListView.setAdapter(fruitEntryAdapter);

        // Populate the list, through the adapter
        for(final MovieViewModel entry : getNewsEntries()) {
            fruitEntryAdapter.add(entry);
        }

        txtFraseBusca = (TextView)findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = getResources().getString(R.string.sua_busca_para);
        String retornou = getResources().getString(R.string.retornou);
        Integer qtd = getIntent().getIntExtra("qtd", 0);
        String resultados = qtd == 1 ? getResources().getString(R.string.resultado) : getResources().getString(R.string.resultados);
        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);
    }

    @Override
    public void onResume(){
        super.onResume();
        Bundle b = getIntent().getBundleExtra("updateuser");
        // put your code here...

    }

    //obtem resultados da busca
    private List<MovieViewModel> getNewsEntries() {
        ArrayList<MovieViewModel> myListItems = null;
        myListItems = (ArrayList<MovieViewModel>)getIntent().getSerializableExtra("bundle_searchResult");

        return myListItems;
    }
}