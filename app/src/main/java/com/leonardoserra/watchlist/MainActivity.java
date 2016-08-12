package com.leonardoserra.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Fragments.FragmentHome;
import com.leonardoserra.watchlist.Fragments.FragmentMyListt;
import com.leonardoserra.watchlist.Fragments.FragmentSearchResult;
import com.leonardoserra.watchlist.Helpers.ApiHelper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private TextView termoTextView;
    private String searchTerm;
    private User gUser;
    private String gHash;
    private FragmentHome fragmentHome;
    private FragmentMyListt fragmentMyListt;
    private FragmentSearchResult fragmentSearchResult;
    private TabLayout allTabs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.getInstance(getSupportFragmentManager());//INIT CUSTOM FRAGMENTMANAGER

        criaOuObtemUsuario();

        configuraActionbar();

        configurasAbas();

        //setar banner
        //AdView mAdView = (AdView) findViewById(R.id.adViewMain);
        //AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        //AdRequest adRequest = adRequestBuilder.addTestDevice(getResources().getString(R.string.device_id)).build();
        //mAdView.loadAd(adRequest);
    }

    private void configuraActionbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        String nomeApp = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);
    }

    private void configurasAbas() {
        allTabs = (TabLayout) findViewById(R.id.tabs);

        allTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragmentHome = new FragmentHome();
        allTabs.addTab(allTabs.newTab().setText("Home"), true);

        fragmentMyListt = new FragmentMyListt();
        allTabs.addTab(allTabs.newTab().setText("MyListt"));
    }

    private void criaOuObtemUsuario(){

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        try {
            gUser = new User();

            sp = getPreferences(MODE_PRIVATE);
            gHash = sp.getString("wl_user_hash", null);
            Singleton.getInstance().setUserHash(gHash);

            Message msgCreateUser = new ApiHelper(this).createuser(gHash);

            if (msgCreateUser.getSucess()) {
                gHash = msgCreateUser.getObject("hash");
                Singleton.getInstance().setUserHash(gHash);

                e.putString("wl_user_hash", gHash);

                e.commit();

                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            gUser.setHash(gHash);
            Singleton.getInstance().setUser(gUser);

        } catch (Exception ex) {

            ex.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setCurrentTabFragment(int tabPosition){
        switch (tabPosition)
        {
            case 0 :
                Singleton.getInstance().trocaFrag(fragmentHome);
                break;
            case 1 :
                Singleton.getInstance().trocaFrag(fragmentMyListt);
                break;
        }
    }

    public void search() {
        fragmentSearchResult = new FragmentSearchResult();

        termoTextView = (EditText) findViewById(R.id.edtSearch);
        searchTerm = "";
        if (!termoTextView.getText().equals(""))
            searchTerm = termoTextView.getText().toString();
        else
            return;

        Message msg = new ApiHelper(this).search(gUser, searchTerm);

        if (msg.getSucess()) {

            try {

                int len;
                JSONObject object = msg.getObject();
                JSONArray jsonArray = object.getJSONArray("movies");

                if (jsonArray != null) {

                    ArrayList<MovieViewModel> list = new ArrayList<>();
                    len = jsonArray.length();

                    for (int i = 0; i < len; i++) {
                        String str = jsonArray.get(i).toString();
                        MovieViewModel f = new Gson().fromJson(str, MovieViewModel.class);
                        f.setUser(gUser);
                        list.add(f);
                    }

                    Singleton.getInstance().setBundleSearchResult(list);
                    Singleton.getInstance().setTermo(searchTerm);
                    Singleton.getInstance().setQtd(len);
                    Singleton.getInstance().trocaFrag(fragmentSearchResult);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, msg.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            //case R.id.action_settings:
            //    return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) allTabs.getLayoutParams();

        if (isSearchOpened){ //test if the search is open
            revelaAbas(layoutParams);
            configuraLayoutActionbarPadrao();

            if (Singleton.getInstance().getNomeFragmentAtual().equals("FragmentSearchResult")){
                escondeAbas(layoutParams);
            }
            Singleton.getInstance().trocaFrag(Singleton.getInstance().getFragmentAtual());

        } else {
            ActionBar action = getSupportActionBar();
            
            escondeAbas(layoutParams);

            action.setDisplayShowCustomEnabled(true); //enable it to display custom view in the action bar.
            action.setCustomView(R.layout.search_bar); //add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        search();
                        configuraLayoutActionbarPadrao();
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        search();
                        configuraLayoutActionbarPadrao();
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }

    private void revelaAbas(LinearLayout.LayoutParams layoutParams) {
        allTabs.setVisibility(View.VISIBLE);
        layoutParams.weight = 0;
        allTabs.setLayoutParams(layoutParams);
    }

    private void escondeAbas(LinearLayout.LayoutParams layoutParams) {
        allTabs.setVisibility(View.INVISIBLE);
        layoutParams.weight = 1;
        allTabs.setLayoutParams(layoutParams);
    }

    private void configuraLayoutActionbarPadrao() {
        ActionBar action2 = getSupportActionBar();
        //layout normal - INICIO
        action2.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
        action2.setDisplayShowTitleEnabled(true); //show the title in the action bar

        //hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

        //add the search icon in the action bar
        mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

        isSearchOpened = false;
    }

    @Override
    public void onBackPressed() {

        if(isSearchOpened) {
            handleMenuSearch();
            return;
        } else {
            int qtd = Singleton.getInstance().getHistoricoSize();

            if (qtd == 1){
                Singleton.getInstance().popBackStackk();
                super.onBackPressed();
            } else{

                String fragmentAtual = Singleton.getInstance().getNomeFragmentAtual();
                String fragmentAnterior = Singleton.getInstance().getNomeFragmentAnterior();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) allTabs.getLayoutParams();

                Singleton.getInstance().popBackStackk();

                if (fragmentAtual.equals("FragmentHome") && fragmentAnterior.equals("FragmentMyListt")) {
                    allTabs.getTabAt(1).select();
                } else if (fragmentAtual.equals("FragmentMyListt") && fragmentAnterior.equals("FragmentHome")) {
                    allTabs.getTabAt(0).select();//ok
                } else if (fragmentAtual.equals("FragmentHome") && fragmentAnterior.equals("FragmentSearchResult")) {
                    allTabs.getTabAt(0).select();
                    revelaAbas(layoutParams);
                } else if (fragmentAtual.equals("FragmentMyListt") && fragmentAnterior.equals("FragmentSearchResult")) {
                    allTabs.getTabAt(1).select();
                    revelaAbas(layoutParams);
                } else if (fragmentAtual.equals("FragmentMovie") && fragmentAnterior.equals("FragmentHome")){
                    allTabs.getTabAt(0);
                    revelaAbas(layoutParams);
                } else if (fragmentAtual.equals("FragmentSearchResult") && fragmentAnterior.equals("FragmentHome")){
                    allTabs.getTabAt(0);
                    revelaAbas(layoutParams);
                } else if (fragmentAtual.equals("FragmentSearchResult") && fragmentAnterior.equals("FragmentMyListt")){
                    allTabs.getTabAt(1);
                    revelaAbas(layoutParams);
                } else if (fragmentAtual.toLowerCase().equals("fragmentmovie") &&
                        fragmentAnterior.toLowerCase().equals("fragmentsearchresult")){
                    //allTabs.getTabAt(1);
                    //int a = 1;
                    //escondeAbas(layoutParams);
                }

                //gBundle.putInt("count_fragments", count);
            }
        }
    }

    public TabLayout getAllTabs(){
        return allTabs;
    }
}
