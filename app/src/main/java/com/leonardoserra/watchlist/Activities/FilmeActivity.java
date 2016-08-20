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
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardoserra.watchlist.Helpers.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.R;

public class FilmeActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private ImageLoader imgLoader;
    private String REMOVE;
    private String ADD;
    private Boolean estaNaMyListt;
    private Bundle bundle;
    private Toolbar toolbar;
    private String filmeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme);

        textView = (TextView)findViewById(R.id.txtMovieTitle);
        button = (Button)findViewById(R.id.btnAddRemove);
        imgLoader = new ImageLoader(this);
        REMOVE = getResources().getString(R.string.remove_movie);
        ADD = getResources().getString(R.string.add_movie);

        bundle = getIntent().getExtras();

        String titulo = bundle.getString("filme2_titulo");
        textView.setText(titulo);
        estaNaMyListt = bundle.getBoolean("filme2_estaNaMyListt", false);
        button.setText(estaNaMyListt ? REMOVE : ADD);

        String nomeArquivo = bundle.getString("filme2_nomeArquivo");
        String baseUrl = "http://10.0.2.2:8080/" + "poster?p=" + nomeArquivo;

        if (!Singleton.getInstance().isEmulator())
            baseUrl = "http://192.168.1.5:8080/" + "poster?p=" + nomeArquivo;

        imgLoader.DisplayImage(baseUrl, (ImageView) findViewById(R.id.imgPoster));

        filmeId = bundle.getString("filme2_filmeId");

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

    public void adicionarOuRetirar(View view) {
        String hash = Singleton.getInstance().getUserHash();

        ApiHelper api = new ApiHelper(this);

        if (estaNaMyListt) {
            api.removeMovie(hash, filmeId);
            Toast.makeText(this, "filme retirado da sua lista", Toast.LENGTH_LONG).show();
        } else {
            api.addMovie(hash, filmeId);
            Toast.makeText(this, "filme adicionado à sua lista", Toast.LENGTH_LONG).show();
        }

        estaNaMyListt = !estaNaMyListt;

        button.setText(estaNaMyListt ? REMOVE : ADD);
    }

    private void configuraActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarra);
        setSupportActionBar(toolbar);

        //configuraActionBarTitle
        String nomeActivityAnterior = bundle.getString("nomeActivityAnterior");
        getSupportActionBar().setTitle(nomeActivityAnterior);
        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.laranja)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        //botao voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.laranja), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filme_estaNaMyListt", estaNaMyListt);
        returnIntent.putExtra("filme_filmeId", filmeId);
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
