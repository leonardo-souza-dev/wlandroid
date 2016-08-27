package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import java.util.ArrayList;

public class ResultadoBuscaActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listView;
    private String termo;
    private MovieAdapter movieAdapter;
    private TextView txtFraseBusca;
    private Toolbar toolbar;
    private ArrayList<MovieViewModel> lista;

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

        lista = Singleton.getInstance().buscaFilme(termo);
        //lista = (ArrayList<MovieViewModel>) bundle.getSerializable("resultadodabusca_lista");

        for(final MovieViewModel entry : lista) {
            movieAdapter.add(entry);
        }

        txtFraseBusca = (TextView) findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = r.getString(R.string.sua_busca_para);
        String retornou = r.getString(R.string.retornou);
        Integer qtd = lista.size();
        String resultados = qtd == 1 ? r.getString(R.string.resultado) : r.getString(R.string.resultados);
        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);

        configuraActionbar();

        Log.d("nav", ">SEARCH_RESULT: " + termo + "|" + qtd);
    }

    private void configuraActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarra);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        //botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.laranja), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //titulo central
        TextView txtTitulo = (TextView)toolbar.findViewById(R.id.txtTituloToolbar);
        txtTitulo.setText("Search");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle b = data.getExtras();

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get_id().equals(b.getString("filme_filmeId"))) {
                    Boolean esta = b.getBoolean("filme_estaNaMyListt");
                    lista.get(i).setIsInMyList(esta);
                }
            }
        }
    }

    public void vaiParaMyListt(View view){
        Intent intentMyListt = new Intent(this, MyListtActivity.class);
        Bundle b = new Bundle();
        b.putString("nomeActivityAnterior", "Search");
        intentMyListt.putExtras(b);

        startActivity(intentMyListt);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
