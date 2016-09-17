package com.leonardoserra.watchlist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.R;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;

public class FilmeActivity extends AppCompatActivity {

    //private TextView textView;
    private Button button;
    //private ImageLoader imgLoader;
    private String REMOVE;
    private String ADD;
    //private Boolean estaNaMyListt;
    //private Bundle bundle;
    private Toolbar toolbar;
    //private String filmeId;
    //private Filme filme;
    private MovieViewModel filmeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        Bundle bundle = getIntent().getExtras();
        filmeViewModel = new MovieViewModel();

        TextView textView = (TextView)findViewById(R.id.txtMovieTitle);
        button = (Button)findViewById(R.id.btnAddRemove);
        ImageLoader imgLoader = new ImageLoader(this);
        REMOVE = getResources().getString(R.string.remove_movie);
        ADD = getResources().getString(R.string.add_movie);

        String titulo = bundle.getString("filme2_titulo");//
        textView.setText(titulo);
        Boolean estaNaMyListt = bundle.getBoolean("filme2_estaNaMyListt", false);//
        button.setText(estaNaMyListt ? REMOVE : ADD);
        String nomeArquivo = bundle.getString("filme2_nomeArquivo");//
        String baseUrl = "http://10.0.2.2:8080/" + "poster?p=" + nomeArquivo;

        if (!Singleton.getInstance().isEmulator())
            baseUrl = "http://192.168.1.5:8080/" + "poster?p=" + nomeArquivo;

        imgLoader.DisplayImage(baseUrl, (ImageView) findViewById(R.id.imgPoster));
        String filmeId = bundle.getString("filme2_filmeId");//

        filmeViewModel.setNome(titulo);
        filmeViewModel.setIsInMyList(estaNaMyListt);
        filmeViewModel.setPoster(nomeArquivo);
        filmeViewModel.set_id(filmeId);

        //Button btnAddOrRemove = (Button)findViewById(R.id.btnAddRemove);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarOuRetirar(v);
            }
        });
        configuraActionbar();
    }

    public Filme ToModel(MovieViewModel m){
        Filme filme = new Filme();
        filme.set_id(m.get_id());
        filme.setNome(m.getNome());
        filme.setEstaNaMinhaLista(m.getIsInMyList());
        filme.setPoster(m.getPoster());
        filme.setAno(m.getAno());

        return filme;
    }

    public void adicionarOuRetirar(View view) {

        if (filmeViewModel.getIsInMyList()) {

            filmeViewModel.setIsInMyList(false);
            Singleton.getInstance().getWLService().removerFilme(ToModel(filmeViewModel));

            Toast.makeText(this, "movie removed from your WatchListt", Toast.LENGTH_LONG).show();

        } else {

            filmeViewModel.setIsInMyList(true);
            Singleton.getInstance().getWLService().adicionarFilme(ToModel(filmeViewModel));

            Toast.makeText(this, "movie added to your WatchListt", Toast.LENGTH_LONG).show();

        }

        button.setText(filmeViewModel.getIsInMyList() ? REMOVE : ADD);
    }

    private void configuraActionbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Back");

        //botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //upArrow.setColorFilter(ContextCompat.getColor(this, R.color.laranja), PorterDuff.Mode.SRC_ATOP);
        //getSupportActionBar().setHomeAsUpIndicator(upArrow);

        //titulo central
        //TextView txtTitulo = (TextView)toolbar.findViewById(R.id.txtTituloToolbar);
        //String nomeFilme = filmeViewModel.getNome();

//        if (nomeFilme.length() > 18) {
//
//            nomeFilme = nomeFilme.substring(0, 18) + "...";
//            Spannable text = new SpannableString(nomeFilme);
//            text.setSpan(new RelativeSizeSpan(0.9f), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//            txtTitulo.setText(text);
//
//        } else {
//            txtTitulo.setText(nomeFilme);
//        }

        //desabilita busca
        ImageView imgBusca = (ImageView) toolbar.findViewById(R.id.imgLupa);
        imgBusca.setVisibility(View.GONE);

        //desabilita my watchListt
        ImageView imgMyWatchListt = (ImageView) toolbar.findViewById(R.id.imgLista);
        imgMyWatchListt.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filme_estaNaMyListt", filmeViewModel.getIsInMyList());
        returnIntent.putExtra("filme_filmeId", filmeViewModel.get_id());
        setResult(Activity.RESULT_OK, returnIntent);

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void vaiParaMyListt(View view){
        Intent intentMyListt = new Intent(this, MyWatchListt.class);
        Bundle b = new Bundle();
        b.putString("nomeActivityAnterior", "Back");
        intentMyListt.putExtras(b);

        startActivity(intentMyListt);
    }
}
