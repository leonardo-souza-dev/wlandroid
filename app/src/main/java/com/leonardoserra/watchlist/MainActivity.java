package com.leonardoserra.watchlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView termoTextView;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void search(View view) {

        //obtem ter_mo da buscaFilmesPorTermo
        termoTextView = (TextView)findViewById(R.id.term);
        String termo = "";
        if (termoTextView.getText() != null)
            termo = termoTextView.getText().toString();
        else
            return;

        api = new Api();
        ArrayList<Filme> resultadoDaBusca = api.busca(termo, 13);

        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("bundle_searchResult", resultadoDaBusca);

        startActivity(intent);
    }
}
