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

import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.R;

public class FragmentMovie extends Fragment {

    private Button btnAcao;
    private MovieViewModel movieViewModel;
    private TextView tituloTextView;
    private Boolean gIsInMyList;
    private String remove;
    private String add;
    private User user;
    private String gMovieId;
    private View gRootView;
    private ImageLoader imgLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imgLoader = new ImageLoader(getContext());

        gRootView = inflater.inflate(R.layout.fragment_movie, container, false);

        remove = getResources().getString(R.string.remove_movie);
        add = getResources().getString(R.string.add_movie);

        //receber dados do filme
        movieViewModel =  Singleton.getInstance().getMovieViewModel();

        //setando user que veio da tela de resultado da busca
        user = Singleton.getInstance().getUser();

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

        imgLoader.DisplayImage(baseUrl, (ImageView) gRootView.findViewById(R.id.imgPoster));

        Button btnAddOrRemove = (Button)gRootView.findViewById(R.id.btnAddRemove);
        btnAddOrRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrRemove(v);
            }
        });

//        FragmentManager fm = getFragmentManager();
//        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if (getFragmentManager().getBackStackEntryCount() == 0) {
//                    try {
//                        finalize();
//                    }
//                    catch (java.lang.Throwable e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

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

        String hash = Singleton.getInstance().getUserHash();
        gMovieId = movieViewModel.get_id();

        ApiHelper api = new ApiHelper(gRootView.getContext());

        if (gIsInMyList) {
            api.removeMovie(hash, gMovieId);
            user.removeFilme(new MovieViewModel(gMovieId, false));

            Toast.makeText(gRootView.getContext(), "filme retirado da sua lista", Toast.LENGTH_LONG).show();
        } else {
            api.addMovie(hash, gMovieId);
            user.adicionaFilme(new MovieViewModel(gMovieId, true));

            Toast.makeText(gRootView.getContext(), "filme adicionado à sua lista", Toast.LENGTH_LONG).show();
        }

        gIsInMyList = !gIsInMyList;

        btnAcao.setText(gIsInMyList ? remove : add);
    }
}
