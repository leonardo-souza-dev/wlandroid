package com.leonardoserra.watchlist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;

public class BlankFragment extends Fragment {

    private View gRootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gRootView = inflater.inflate(R.layout.activity_movie, container, false);

        remove = getResources().getString(R.string.remove_movie).toString();
        add = getResources().getString(R.string.add_movie).toString();

        //receber dados do filme
        movieViewModel = null;
        savedInstanceState = getArguments();
        if (savedInstanceState != null) {
            jsonMyObject = savedInstanceState.getString("movieViewModelEntry");
            movieViewModel = new Gson().fromJson(jsonMyObject, MovieViewModel.class);
        }

        //setando user que veio da tela de resultado da busca
        gUser = movieViewModel.getUser();

        //setando titulo do filme
        tituloTextView = (TextView) gRootView.findViewById(R.id.txtMovieTitle);
        String titulo = movieViewModel.getName();
        tituloTextView.setText(titulo);

        //getSupportActionBar().setTitle(titulo);

        //esta na minha lista?
        gIsInMyList = movieViewModel.getIsInMyList();
        //setando botao de acao
        btnAcao = (Button)gRootView.findViewById(R.id.btnAddRemove);
        btnAcao.setText(gIsInMyList ? remove : add);

        String nomeArquivo = movieViewModel.getPoster();
        String baseUrl = "http://10.0.2.2:8080/";
        if (!isEmulator())
            baseUrl = "http://192.168.1.5:8080/";
        new DownloadImageTask((ImageView)gRootView.findViewById(R.id.imgPoster))
                .execute(baseUrl + "poster?p=" + nomeArquivo);

        return gRootView;
    }

    public void addOrRemove(View view) {

        //User lUser = movieViewModel.getUser();
        String hash = gUser.getHash();
        gMovieId = movieViewModel.get_id();

        ApiHelper api = new ApiHelper(gRootView.getContext());

        if (gIsInMyList) {
            api.removeMovie(hash, gMovieId);
            gUser.removeFilme(new MovieViewModel(gMovieId, false));
            Toast.makeText(gRootView.getContext(), "filme retirado da sua lista", Toast.LENGTH_LONG).show();
        } else {
            api.addMovie(hash, gMovieId);
            gUser.adicionaFilme(new MovieViewModel(gMovieId, true));
            Toast.makeText(gRootView.getContext(), "filme adicionado à sua lista", Toast.LENGTH_LONG).show();
        }

        gIsInMyList = !gIsInMyList;

        btnAcao.setText(gIsInMyList ? remove : add);
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
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
}
