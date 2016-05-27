package com.leonardoserra.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<MovieViewModel> adbPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        setListView();
    }

    private void setListView(){
        // Setup the list view
        final ListView newsEntryListView = (ListView) findViewById(R.id.listView);
        final MovieViewModelAdapter fruitEntryAdapter = new MovieViewModelAdapter(this, R.layout.simple_row);
        newsEntryListView.setAdapter(fruitEntryAdapter);
        // Populate the list, through the adapter
        for(final Filme entry : getNewsEntries()) {
            fruitEntryAdapter.add(entry);
        }
    }

    private List<Filme> getNewsEntries() {
        ArrayList<Filme> myListItems = null;
        myListItems = (ArrayList<Filme>)getIntent().getSerializableExtra("bundle_searchResult");

        return myListItems;
    }
}
