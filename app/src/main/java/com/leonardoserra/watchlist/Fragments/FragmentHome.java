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
import com.leonardoserra.watchlist.Models.User;
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

    private ListView listView;
    private MovieAdapter movieAdapter;
    private View rootView;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = Singleton.getInstance().getUser();
        rootView = inflater.inflate(R.layout.fragment_home, null);
        listView = (ListView) rootView.findViewById(R.id.listViewFilmesRecomendados);

        movieAdapter = new MovieAdapter(getContext(), R.layout.simple_row, "", this);
        listView.setAdapter(movieAdapter);

        for(final MovieViewModel entry : getNewsEntries()) {
            movieAdapter.add(entry);
        }

        return rootView;
    }

    private List<MovieViewModel> getNewsEntries() {

        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        String hashSingleton = Singleton.getInstance().getUserHash();

        Message msg = new ApiHelper(getContext()).obterFilmesRecomendados(hashSingleton);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray;

        try {
            jsonArray = mylisttJson.getJSONArray("filmesrecomendados");

            if (jsonArray != null) {

                int len = jsonArray.length();

                for (int i = 0; i < len; i++) {
                    String str = jsonArray.get(i).toString();
                    MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                    f.setUser(user);
                    myListItems.add(f);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myListItems;
    }

}
