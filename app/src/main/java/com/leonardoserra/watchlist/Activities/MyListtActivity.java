package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.FilmeBusiness;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import java.util.ArrayList;

public class MyListtActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listView;

    private MovieAdapter movieAdapter;
    private Toolbar toolbar;
    private ArrayList<MovieViewModel> lista;
    private TextView msg;
    private Button btnVaiParaBusca;
    private String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listt);

        bundle = getIntent().getExtras();
        hash = bundle.getString("hash");

        msg = (TextView)findViewById(R.id.txtMsg);
        btnVaiParaBusca = (Button)findViewById(R.id.btnVaiParaBusca);

        listView = (ListView) findViewById(R.id.listViewMyListt);

        movieAdapter = new MovieAdapter(this, R.layout.simple_row, "");

        listView.setAdapter(movieAdapter);

        FilmeBusiness filmeBusiness = new FilmeBusiness(hash);
        ArrayList<Filme> filmes = filmeBusiness.obterMyListt();

        //lista = Singleton.getInstance().getMyListt();
        lista = ToViewModel(filmes);

        if (lista.size() > 0){

            for (final MovieViewModel entry : lista) {
                movieAdapter.add(entry);
            }

            msg.setEnabled(false);
            btnVaiParaBusca.setEnabled(false);

        } else {

            msg.setText(getResources().getString(R.string.there_is_no_movies));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(24, 140, 24, 24);
            msg.setLayoutParams(params);

            btnVaiParaBusca.setText(getResources().getString(R.string.search_movies));
        }

        configuraActionbar();

        Log.d("nav", ">MY_LISTT");
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

    public void vaiParaBusca(View view){
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putString("nomeActivityAnterior","MyListt");
        intent.putExtras(b);
        startActivity(intent);
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
        txtTitulo.setText("MyListt");

        //botao mylistt
        TextView txtMenuItem = (TextView)toolbar.findViewById(R.id.txtItemMenuMyListt);
        txtMenuItem.setVisibility(View.GONE);

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

    public void vaiParaMyListt(View view){
        Intent intentMyListt = new Intent(this, MyListtActivity.class);
        Bundle b = new Bundle();
        b.putString("nomeActivityAnterior","MyListt");
        intentMyListt.putExtras(b);

        startActivity(intentMyListt);
    }
}
