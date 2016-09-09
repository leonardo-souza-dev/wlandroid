package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;
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
    private String hash;
    private ArrayList<Filme> filmes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_busca);

        bundle = getIntent().getExtras();
        hash = bundle.getString("hash");

        listView = (ListView) findViewById(R.id.listViewResultadoBusca);

        termo = bundle.getString("resultadodabusca_termo");
        movieAdapter = new MovieAdapter(this, R.layout.simple_row, termo);

        Resources r = getResources();

        listView.setAdapter(movieAdapter);

        //wlService = new WLService(getBaseContext());
        //filmes = wlService.buscar(termo);
        filmes = Singleton.getInstance().getWLService().buscar(termo);
        //lista = Singleton.getInstance().buscaFilme(termo);
        lista = ToViewModel(filmes);
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
    }

    private ArrayList<MovieViewModel> ToViewModel(ArrayList<Filme> filmes){

        ArrayList<MovieViewModel> models = new ArrayList<>();
        for (Filme f : filmes){
            MovieViewModel m = new MovieViewModel();
            m.set_id(f.get_id());
            m.setNome(f.getName());
            m.setIsInMyList(f.getIsInMyList());
            m.setPoster(f.getPoster());
            m.setAno(f.getAno());

            models.add(m);
        }
        return models;
    }

    private void configuraActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarra);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

        //botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.laranja), PorterDuff.Mode.SRC_ATOP);
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
