package com.leonardoserra.watchlist.Fragments;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

public class FragmentMovie extends Fragment {

    private Button btnAcao;
    private String jsonMyObject;
    private MovieViewModel movieViewModel;
    private TextView tituloTextView;
    private Boolean gIsInMyList;
    private String remove;
    private String add;
    private User gUser;
    private String gMovieId;
    private View gRootView;
    private ImageLoader imgLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imgLoader = new ImageLoader(getContext());

        gRootView = inflater.inflate(R.layout.fragment_movie, container,false);

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
        tituloTextView = (TextView)gRootView.findViewById(R.id.txtMovieTitle);
        String titulo = movieViewModel.getName();
        tituloTextView.setText(titulo);

        //esta na minha lista?
        gIsInMyList = movieViewModel.getIsInMyList();
        //setando botao de acao
        btnAcao = (Button)gRootView.findViewById(R.id.btnAddRemove);
        btnAcao.setText(gIsInMyList ? remove : add);

        String nomeArquivo = movieViewModel.getPoster();
        String baseUrl = "http://10.0.2.2:8080/" + "poster?p=" + nomeArquivo;

        if (!isEmulator())
            baseUrl = "http://192.168.1.5:8080/" + "poster?p=" + nomeArquivo;

        imgLoader.DisplayImage(baseUrl, (ImageView)gRootView.findViewById(R.id.imgPoster));

        Button btnAddOrRemove = (Button)gRootView.findViewById(R.id.btnAddRemove);
        btnAddOrRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemove(v);
            }
        });

        return gRootView;
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

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Log.d(this.getClass().getName(), "back button pressed");
        }
        return super.onKeyDown(keyCode, event);
    }
*/
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

    /*
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
    } */
}
