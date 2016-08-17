package com.leonardoserra.watchlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText termoTextView;

    private Toolbar mToolbar;
    private EditText edtSearch;
    private String termoDaBusca;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.getInstance(getBaseContext(), getResources(), getSupportFragmentManager());//INIT CUSTOM FRAGMENTMANAGER

        criaOuObtemUsuario();

        configuraActionbar();

        //setarBanner();
    }

    public void busca(View view) {
        termoTextView = (EditText) findViewById(R.id.edtSearchAMovie);
        termoDaBusca = "";
        if (!termoTextView.getText().equals(""))
            termoDaBusca = termoTextView.getText().toString();
        else
            return;

        Intent intent = new Intent(this,ResultadoBuscaActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<MovieViewModel> resultado = Singleton.getInstance().buscaFilme(termoDaBusca);
        bundle.putSerializable("resultadodabusca_lista", resultado);
        bundle.putString("resultadodabusca_termo", termoDaBusca);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void criaOuObtemUsuario(){
        String resultado = Singleton.getInstance().criaOuObtemUsuario();
        Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
    }

    private void configuraActionbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        String nomeApp = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);
    }
}
