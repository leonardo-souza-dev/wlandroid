package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Helpers.FragWrapper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentHome extends Fragment {

    private ListView gListView;
    private MovieAdapter gMovieAdapter;
    private int gCount;
    private View gRootView;
    private FragWrapper.FragHelper fragHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getContext();
        gRootView = inflater.inflate(R.layout.fragment_home, null);
        fragHelper = new FragWrapper.FragHelper(getFragmentManager(), R.id.frame_container);
        gListView = (ListView) gRootView.findViewById(R.id.listViewFilmesRecomendados);

        AtomicReference<Object> ref = new AtomicReference<Object>(getFragmentManager());

        //comentado temporariamente ate acertar o fragHelpper
        //gCount = getArguments().getInt("count_fragments");

        gMovieAdapter = new MovieAdapter(getContext(), R.layout.simple_row, "", this, ref, gCount);
        gListView.setAdapter(gMovieAdapter);

        for(final MovieViewModel entry : getNewsEntries()) {
            gMovieAdapter.add(entry);
        }


        return gRootView;
    }

    private List<MovieViewModel> getNewsEntries() {

        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        //String hash = getArguments().get("user_hash").toString();
        //String hash = fragHelper.getBundleHash();
        String hashSingleton = Singleton.getInstance().getUserHash();


        Message msg = new ApiHelper(getContext()).obterFilmesRecomendados(hashSingleton);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray = null;

        try {
            jsonArray = mylisttJson.getJSONArray("filmesrecomendados");

            if (jsonArray != null) {

                int len = jsonArray.length();

                for (int i = 0; i < len; i++) {
                    String str = jsonArray.get(i).toString();
                    MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                    myListItems.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myListItems;
    }

}
