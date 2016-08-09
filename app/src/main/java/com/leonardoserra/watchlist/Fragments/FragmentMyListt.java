package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentMyListt extends Fragment {

    private ListView gListView;
    private MovieAdapter gMovieAdapter;
    private View rootView;
    private TextView txtMsg;
    private int gCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mylistt, null);

        gListView = (ListView) rootView.findViewById(R.id.listViewMyListt);
        //FragmentManager fm = getFragmentManager();
        //AtomicReference<Object> ref = new AtomicReference<Object>(fm);
        //gCount = getArguments().getInt("count_fragments");
        gMovieAdapter = new MovieAdapter(getContext(), R.layout.simple_row, "", this, gCount);
        gListView.setAdapter(gMovieAdapter);

        txtMsg = (TextView)rootView.findViewById(R.id.txtMsg);

        for(final MovieViewModel entry : getNewsEntries()) {
            gMovieAdapter.add(entry);
        }

        return rootView;
    }

    private List<MovieViewModel> getNewsEntries() {
        ArrayList<MovieViewModel> myListItems = new ArrayList<>();

        //String hash = getArguments().get("user_hash").toString();
        String hash = Singleton.getInstance().getUserHash();

        Message msg = new ApiHelper(getContext()).obterMyListt(hash);

        JSONObject mylisttJson = msg.getObject();
        JSONArray jsonArray = null;

        try {
            jsonArray = mylisttJson.getJSONArray("mylistt");

            if (jsonArray != null) {

                int len = jsonArray.length();

                if (len != 0) {

                    for (int i = 0; i < len; i++) {
                        String str = jsonArray.get(i).toString();
                        MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                        myListItems.add(f);
                    }
                    txtMsg.setEnabled(false);
                    txtMsg.setVisibility(View.INVISIBLE);
                    txtMsg.setText("");
                } else {
                    txtMsg.setEnabled(true);
                    txtMsg.setVisibility(View.VISIBLE);
                    txtMsg.setText("There's no movies in your WatchListt :(");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myListItems;
    }
}
