package com.leonardoserra.watchlist;

import android.content.Context;
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

import com.leonardoserra.watchlist.Fragments.FragmentHome;
import com.leonardoserra.watchlist.Fragments.FragmentMyListt;
import com.leonardoserra.watchlist.Helpers.Singleton;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSearch;
    private String searchTerm;
    //private User gUser;
    private FragmentHome fragmentHome;
    private FragmentMyListt fragmentMyListt;
    //private FragmentSearchResult fragmentSearchResult;
    private TabLayout allTabs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.getInstance(getBaseContext(), getResources(), getSupportFragmentManager());//INIT CUSTOM FRAGMENTMANAGER

        criaOuObtemUsuario();

        configuraActionbar();

        configurasAbas();

        //setarBanner();
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
                Singleton.getInstance().trocaFrag(fragmentHome, false);
                break;
            case 1 :
                Singleton.getInstance().trocaFrag(fragmentMyListt, false);
                break;
        }
    }

    public void search() {
        //fragmentSearchResult = new FragmentSearchResult();

        TextView termoTextView = (EditText) findViewById(R.id.edtSearch);
        searchTerm = "";
        if (!termoTextView.getText().equals(""))
            searchTerm = termoTextView.getText().toString();
        else
            return;

        Singleton.getInstance().buscaFilme(searchTerm);
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
            Singleton.getInstance().trocaFrag(Singleton.getInstance().getFragmentAtual(), false);

        } else {
            ActionBar actionBar = getSupportActionBar();
            
            escondeAbas(layoutParams);

            if (actionBar != null) {
                actionBar.setDisplayShowCustomEnabled(true); //enable it to display custom view in the action bar.
                actionBar.setCustomView(R.layout.search_bar); //add the custom view
                actionBar.setDisplayShowTitleEnabled(false); //hide the title
                edtSearch = (EditText)actionBar.getCustomView().findViewById(R.id.edtSearch);
            }
            edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            edtSearch.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        search();
                        configuraLayoutActionbarPadrao();
                        return true;
                    }
                    return false;
                }
            });

            edtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
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
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            actionBar.setDisplayShowTitleEnabled(true); //show the title in the action bar
        }
        //hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

        //add the search icon in the action bar
        mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

        isSearchOpened = false;
    }

    @Override
    public void onBackPressed() {

        if(isSearchOpened) {
            handleMenuSearch();
        } else {
            int qtd = Singleton.getInstance().getHistoricoSize();

            if (qtd == 1){
                super.onBackPressed();
            } else{

                String fragmentAtual = Singleton.getInstance().getNomeFragmentAtual();

                String fragmentAnterior = Singleton.getInstance().getNomeFragmentAnterior();

                //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) allTabs.getLayoutParams();

                switch (fragmentAtual) {
                    case "FragmentHome":
                        switch (fragmentAnterior) {
                            case "FragmentMyListt":

                                ajeita(Aba.MYLISTT, true);
                                //allTabs.getTabAt(1).select();
                                break;
                            case "FragmentSearchResult":
                                ajeita(Aba.HOME, true);
                                //allTabs.getTabAt(0).select();
                                //revelaAbas(layoutParams);
                                break;
                        }
                        break;
                    case "FragmentMyListt":
                        switch (fragmentAnterior) {
                            case "FragmentHome":
                                ajeita(Aba.HOME, true);
                                //allTabs.getTabAt(0).select();
                                break;
                            case "FragmentSearchResult":
                                ajeita(Aba.MYLISTT, true);
                                //allTabs.getTabAt(1).select();
                                //revelaAbas(layoutParams);
                                break;
                        }
                        break;
                    case "FragmentMovie":
                        switch (fragmentAnterior) {
                            case "FragmentHome":
                                ajeita(Aba.HOME, true);
                                //allTabs.getTabAt(0);
                                //revelaAbas(layoutParams);
                                Singleton.getInstance().trocaFrag(fragmentHome, true);
                                break;
                            case "FragmentMyListt":
                                ajeita(Aba.MYLISTT, true);
                                //allTabs.getTabAt(1);
                                //revelaAbas(layoutParams);
                                Singleton.getInstance().trocaFrag(fragmentMyListt, true);
                                break;
                            case "FragmentSearchResult":
                                int a = 1;
                                break;
                        }
                        break;
                    case "FragmentSearchResult":
                        switch (fragmentAnterior) {
                            case "FragmentHome":
                                ajeita(Aba.HOME, true);
                                //allTabs.getTabAt(0);
                                //revelaAbas(layoutParams);
                                Singleton.getInstance().trocaFrag(fragmentHome, true);
                                break;
                            case "FragmentMyListt":
                                ajeita(Aba.MYLISTT, true);
                                //allTabs.getTabAt(1);
                                //revelaAbas(layoutParams);
                                Singleton.getInstance().trocaFrag(fragmentMyListt, true);
                                break;
                            case "FragmentSearchResult":
                                Singleton.getInstance().mostraPenultimoResultadoBusca(true);
                                break;
                        }
                        break;
                }
            }
        }
    }

    private void ajeita(Aba aba, Boolean revela) throws NullPointerException {
        if (allTabs != null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) allTabs.getLayoutParams();
            allTabs.getTabAt(aba.getValor()).select();
            if (revela) {
                revelaAbas(layoutParams);
            } else {
                escondeAbas(layoutParams);
            }
        } else{
            throw new NullPointerException();
        }
    }

    public TabLayout getAllTabs(){
        return allTabs;
    }

    /* MainActivity stuff */
    private void criaOuObtemUsuario(){
        String resultado = Singleton.getInstance().criaOuObtemUsuario();
        Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show();
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
        fragmentMyListt = new FragmentMyListt();

        allTabs.addTab(allTabs.newTab().setText(getResources().getString(R.string.home)), true);
        allTabs.addTab(allTabs.newTab().setText(getResources().getString(R.string.mylistt)));
    }

    private enum Aba{
        HOME(0), MYLISTT(1);

        private final int valor;
        Aba(int valorOpcao){
            valor = valorOpcao;
        }
        public int getValor(){
            return valor;
        }
    }
}
