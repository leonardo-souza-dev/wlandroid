package com.leonardoserra.watchlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearchResult extends Fragment {

    private TextView txtFraseBusca;
    private MovieAdapter gFruitEntryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_result, container,false);

        ListView newsEntryListView = (ListView) rootView.findViewById(R.id.listView);
        String termo = getArguments().getString("termo");

        gFruitEntryAdapter = new MovieAdapter(getContext(), R.layout.simple_row, termo, this);
        newsEntryListView.setAdapter(gFruitEntryAdapter);

        // Populate the list, through the adapter
        for(final MovieViewModel entry : getNewsEntries()) {
            gFruitEntryAdapter.add(entry);
        }

        txtFraseBusca = (TextView)rootView.findViewById(R.id.txtFraseBusca);
        String suaBuscaPara = getResources().getString(R.string.sua_busca_para);
        String retornou = getResources().getString(R.string.retornou);
        Integer qtd = getArguments().getInt("qtd", 0);
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
        myListItems = (ArrayList<MovieViewModel>)getArguments().getSerializable("bundle_searchResult");

        return myListItems;
    }
}