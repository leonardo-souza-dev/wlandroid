package com.leonardoserra.watchlist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Domain.Filme;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.R;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;

public class FilmeActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ImageLoader imgLoader;
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

        textView = (TextView)findViewById(R.id.txtMovieTitle);
        button = (Button)findViewById(R.id.btnAddRemove);
        imgLoader = new ImageLoader(this);
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


        Button btnAddOrRemove = (Button)findViewById(R.id.btnAddRemove);
        btnAddOrRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarOuRetirar(v);
            }
        });
        configuraActionbar();

        Log.d("nav", "MOVIE: " + titulo);
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
        String hash = Singleton.getInstance().getUserHash();

        ApiHelper api = new ApiHelper(this);

        if (filmeViewModel.getIsInMyList()) {
            Singleton.getInstance().getWLService().removerFilme(ToModel(filmeViewModel));
            //api.removeMovie(hash, filmeViewModel.get_id());
            Toast.makeText(this, "movie removed from your WatchListt", Toast.LENGTH_LONG).show();
        } else {
            Singleton.getInstance().getWLService().adicionarFilme(ToModel(filmeViewModel));
            //api.addMovie(hash, filmeViewModel.get_id());
            Toast.makeText(this, "movie added to your WatchListt", Toast.LENGTH_LONG).show();
        }
        filmeViewModel.setIsInMyList(!filmeViewModel.getIsInMyList());
        //estaNaMyListt = !estaNaMyListt;

        button.setText(filmeViewModel.getIsInMyList() ? REMOVE : ADD);
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
        String nomeFilme = filmeViewModel.getNome();
        if (nomeFilme.length() > 17) {

            nomeFilme = nomeFilme.substring(0, 17) + "...";
            //Spannable text = new SpannableString(getSupportActionBar().getTitle());
            Spannable text = new SpannableString(nomeFilme);
            text.setSpan(new AbsoluteSizeSpan(20),
                    0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            txtTitulo.setText(text);

        } else {

            txtTitulo.setText(nomeFilme);

        }
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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void vaiParaMyListt(View view){
        Intent intentMyListt = new Intent(this, MyListtActivity.class);
        Bundle b = new Bundle();
        b.putString("nomeActivityAnterior", "Back");
        intentMyListt.putExtras(b);

        startActivity(intentMyListt);
    }
}
