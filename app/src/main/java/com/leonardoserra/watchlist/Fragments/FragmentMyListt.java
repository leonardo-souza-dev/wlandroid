package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Helpers.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentMyListt extends Fragment {

    private ListView listView;
    private MovieAdapter movieAdapter;
    private View rootView;
    private TextView txtMsg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mylistt, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewMyListt);

        txtMsg = (TextView)rootView.findViewById(R.id.txtMsg);

        movieAdapter = new MovieAdapter(getContext(), R.layout.linha, "");
        listView.setAdapter(movieAdapter);

        for(final MovieViewModel entry : Singleton.getInstance().getMyListt()) {
            movieAdapter.add(entry);
        }

        if (Singleton.getInstance().getQtdMyListt() > 0){
            txtMsg.setEnabled(false);
            txtMsg.setVisibility(View.INVISIBLE);
            txtMsg.setText("");
        } else {
            txtMsg.setEnabled(true);
            txtMsg.setVisibility(View.VISIBLE);
            txtMsg.setText("There's no movies in your WatchListt :(");
        }

        return rootView;
    }
}
