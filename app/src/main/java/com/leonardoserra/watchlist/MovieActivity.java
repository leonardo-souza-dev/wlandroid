package com.leonardoserra.watchlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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

        //setando titulo do filme
        tituloTextView = (TextView) findViewById(R.id.txtMovieTitle);
        String titulo = movieViewModel.getName();
        tituloTextView.setText(titulo);

        getSupportActionBar().setTitle(titulo);

        //setando botao de acao
        btnAcao = (Button)findViewById(R.id.btnAddRemove);
        //esta na minha lista?
        gIsInMyList = movieViewModel.getIsInMyList();

        btnAcao.setText(gIsInMyList ? remove : add);

        String nomeArquivo = movieViewModel.getPoster();

        new DownloadImageTask((ImageView) findViewById(R.id.imgPoster))
                .execute("http://10.0.2.2:8080/poster?p=" + nomeArquivo);


    }

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

        User lUser = movieViewModel.getUser();
        String hash = lUser.getHash();
        String movieId = movieViewModel.get_id();

        ApiHelper api = new ApiHelper();

        if (gIsInMyList) {
            api.removeMovie(hash, movieId);
            Toast.makeText(this, "filme retirado da sua lista",Toast.LENGTH_LONG).show();
        } else {
            api.addMovie(hash, movieId);
            Toast.makeText(this, "filme adicionado à sua lista",Toast.LENGTH_LONG).show();
        }

        gIsInMyList = !gIsInMyList;

        btnAcao.setText(gIsInMyList ? remove : add);
    }
}
