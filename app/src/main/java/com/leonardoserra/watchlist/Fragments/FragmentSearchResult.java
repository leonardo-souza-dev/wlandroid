package com.leonardoserra.watchlist.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.MovieAdapter;
import com.leonardoserra.watchlist.R;

public class FragmentSearchResult extends Fragment {

    private TextView txtFraseBusca;
    private MovieAdapter gFruitEntryAdapter;
    private ListView gNewsEntryListView;
    private View rootView;
    private String termo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        gNewsEntryListView = (ListView) rootView.findViewById(R.id.listViewResultadoBusca);

        termo = Singleton.getInstance().getTermo();

        gFruitEntryAdapter = new MovieAdapter(getContext(), R.layout.simple_row, termo, this);

        gNewsEntryListView.setAdapter(gFruitEntryAdapter);

        for(final MovieViewModel entry : Singleton.getInstance().getBundleSearchResult()) {
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
}