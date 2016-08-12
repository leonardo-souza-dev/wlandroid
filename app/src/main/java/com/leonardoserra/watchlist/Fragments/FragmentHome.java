package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Helpers.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import android.support.v4.app.Fragment;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    private ListView listView;
    private MovieAdapter movieAdapter;
    private View rootView;
    //private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewFilmesRecomendados);

        //user = Singleton.getInstance().getUser();

        movieAdapter = new MovieAdapter(getContext(), R.layout.simple_row, "");
        listView.setAdapter(movieAdapter);

        for(final MovieViewModel entry : Singleton.getInstance().getRecomendados()) {
            movieAdapter.add(entry);
        }

        return rootView;
    }
}
