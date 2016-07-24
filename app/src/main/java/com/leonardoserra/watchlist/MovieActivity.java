package com.leonardoserra.watchlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;

public class MovieActivity extends AppCompatActivity {

    private Button btnAcao;
    private String jsonMyObject;
    private MovieViewModel movieViewModel;
    private MoviesViewModel moviesViewModel;
    private TextView tituloTextView;
    private boolean gIsInMyList;
    private String remove;
    private String add;
    private User gUser;
    private String gMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        remove = getResources().getString(R.string.remove_movie).toString();
        add = getResources().getString(R.string.add_movie).toString();

        //receber dados do filme
        movieViewModel = null;
        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null) {
            jsonMyObject = savedInstanceState.getString("movieViewModelEntry");
            movieViewModel = new Gson().fromJson(jsonMyObject, MovieViewModel.class);
        }

        //setando user que veio da tela de resultado da busca
        gUser = movieViewModel.getUser();

        //setando titulo do filme
        tituloTextView = (TextView) findViewById(R.id.txtMovieTitle);
        String titulo = movieViewModel.getName();
        tituloTextView.setText(titulo);

        getSupportActionBar().setTitle(titulo);

        //esta na minha lista?
        gIsInMyList = movieViewModel.getIsInMyList();
        //setando botao de acao
        btnAcao = (Button)findViewById(R.id.btnAddRemove);
        btnAcao.setText(gIsInMyList ? remove : add);

        String nomeArquivo = movieViewModel.getPoster();
        new DownloadImageTask((ImageView) findViewById(R.id.imgPoster))
                .execute("http://10.0.2.2:8080/poster?p=" + nomeArquivo);
    }

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Log.d(this.getClass().getName(), "back button pressed");
        }
        return super.onKeyDown(keyCode, event);
    }
    */

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void addOrRemove(View view) {

        //User lUser = movieViewModel.getUser();
        String hash = gUser.getHash();
        gMovieId = movieViewModel.get_id();

        ApiHelper api = new ApiHelper(this);

        if (gIsInMyList) {
            api.removeMovie(hash, gMovieId);
            gUser.removeFilme(new MovieViewModel(gMovieId, false));
            Toast.makeText(this, "filme retirado da sua lista", Toast.LENGTH_LONG).show();
        } else {
            api.addMovie(hash, gMovieId);
            gUser.adicionaFilme(new MovieViewModel(gMovieId, true));
            Toast.makeText(this, "filme adicionado à sua lista", Toast.LENGTH_LONG).show();
        }

        gIsInMyList = !gIsInMyList;

        btnAcao.setText(gIsInMyList ? remove : add);
    }

    @Override
    public void onBackPressed(){

        Intent returnIntent = new Intent();
        JSONObject json = new JSONObject();
        try {
            json.put("movie_id", gMovieId);
            json.put("isInMyList", gIsInMyList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        returnIntent.putExtra("action_result", json.toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }
}
