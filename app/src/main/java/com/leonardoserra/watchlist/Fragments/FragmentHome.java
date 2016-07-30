package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private ListView gListView;
    private MovieAdapter gMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        gListView = (ListView) rootView.findViewById(R.id.listViewFilmesRecomendados);
        gMovieAdapter = new MovieAdapter(getContext(), R.layout.simple_row, "", this);
        gListView.setAdapter(gMovieAdapter);

        for(final MovieViewModel entry : getNewsEntries()) {
            gMovieAdapter.add(entry);
        }

        return rootView;
    }

    private List<MovieViewModel> getNewsEntries() {
        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        String hash = getArguments().get("user_hash").toString();

        Message msg = new ApiHelper(getContext()).obterFilmesRecomendados(hash);

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
