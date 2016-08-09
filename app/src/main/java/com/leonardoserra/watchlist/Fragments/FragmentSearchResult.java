package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentSearchResult extends Fragment {

    private TextView txtFraseBusca;
    private MovieAdapter gFruitEntryAdapter;
    private ListView gNewsEntryListView;
    private int gCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);

        gNewsEntryListView = (ListView) rootView.findViewById(R.id.listViewResultadoBusca);

        //String termo = getArguments().getString("termo");
        String termo = Singleton.getInstance().getTermo();

        FragmentManager fm = getFragmentManager();
        //AtomicReference<Object> ref = new AtomicReference<Object>(fm);
        //gCount = getArguments().getInt("count_fragments");
        gFruitEntryAdapter = new MovieAdapter(getContext(), R.layout.simple_row, termo, this);
        gNewsEntryListView.setAdapter(gFruitEntryAdapter);

        // Populate the list, through the adapter
        for(final MovieViewModel entry : getNewsEntries()) {

            gFruitEntryAdapter.add(entry);

        }

        txtFraseBusca = (TextView)rootView.findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = getResources().getString(R.string.sua_busca_para);
        String retornou = getResources().getString(R.string.retornou);
        Integer qtd = Singleton.getInstance().getQtd();
        String resultados = qtd == 1 ? getResources().getString(R.string.resultado) : getResources().getString(R.string.resultados);
        txtFraseBusca.setText(suaBuscaPara + " \"" + termo + "\" " + retornou + " " + qtd + " " + resultados);

        return rootView;
    }

    /*
    @Override
    public void onResume(){
        super.onResume();
        Bundle b = getIntent().getBundleExtra("updateuser");
        // put your code here...
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gFruitEntryAdapter.onActivityResult(requestCode, resultCode, data);
    }
*/
    //obtem resultados da busca
    private List<MovieViewModel> getNewsEntries() {
        ArrayList<MovieViewModel> myListItems = null;
        myListItems = Singleton.getInstance().getBundleSearchResult();
        //myListItems = (ArrayList<MovieViewModel>)getArguments().getSerializable("bundle_searchResult");

        return myListItems;
    }
}