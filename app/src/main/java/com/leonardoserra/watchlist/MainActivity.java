package com.leonardoserra.watchlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Fragments.FragmentHome;
import com.leonardoserra.watchlist.Fragments.FragmentMyListt;
import com.leonardoserra.watchlist.Fragments.FragmentSearchResult;
import com.leonardoserra.watchlist.Helpers.FragWrapper;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.Message;
import com.leonardoserra.watchlist.Models.MovieViewModel;
import com.leonardoserra.watchlist.Models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //fonte: http://blog.rhesoft.com/2015/03/30/tutorial-android-actionbar-with-material-design-and-search-field/
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
    private TabHost gTabHost;
    //private FragmentManager fm;
    private String fragmentAtiva;
    //private Bundle gBundle;
    //private FragWrapper.FragHelper fragHelper;

    private void trocaViaHelper(Fragment f){
        Singleton.getInstance().trocaFrag(f);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Singleton.getInstance(getSupportFragmentManager());//INIT CUSTOM FRAGMENTMANAGER

        //fragHelper = new FragWrapper.FragHelper(getSupportFragmentManager(), R.id.frame_container);

        criaOuObtemUsuario();

        //gBundle = new Bundle();
        //fm = getSupportFragmentManager();


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
        String nomeApp = getResources().getString(R.string.app_name);
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

        //configura abas

        //fragHelper.setBundleHash(gUser.getHash());
        //gBundle.putString("user_hash", gUser.getHash());

        fragmentHome = new FragmentHome();
        //fragmentHome.setArguments(gBundle);

        fragmentMyListt = new FragmentMyListt();
        //fragmentMyListt.setArguments(gBundle);

        allTabs.addTab(allTabs.newTab().setText("Home"), true);
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
                //Boolean exists = Boolean.parseBoolean(msgCreateUser.getObject("exists"));

                //if (!exists) {
                    gHash = msgCreateUser.getObject("hash");

                Singleton.getInstance().setUserHash(gHash);
                //fragHelper.setBundleHash(gHash);
                    //e.putString("wl_user_hash", gHash);
                    e.commit();
                //}

                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            gUser.setHash(gHash);

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

    private void setCurrentTabFragment(int tabPosition)
    {

        switch (tabPosition)
        {
            case 0 :
                trocaViaHelper(fragmentHome);
                //insereBundleETrocaOFragment(fragmentHome);
                break;
            case 1 :
                trocaViaHelper(fragmentMyListt);
                //insereBundleETrocaOFragment(fragmentMyListt);
                break;
        }
    }

    /*public void insereBundleETrocaOFragment(Fragment fragment){
        Bundle b = fragment.getArguments();
        boolean fragmentNaoTemBundleSetado = b == null;

        if (fragmentNaoTemBundleSetado) {
            fragment.setArguments(gBundle);
        }
        trocaFragment(fragment);
    }*/

    public void trocaFragment(Fragment fragment) {
        Log.d("wl", "Indo para " + fragment.getClass().getSimpleName());

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frame_container, fragment, "");

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();

        getSupportFragmentManager().executePendingTransactions();

        fragmentAtiva = fragment.getClass().getSimpleName();

        count++;
        //gBundle.putInt("count_fragments", count);
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

                    //insere resultado da busca, termo da busca, qtd encontrada e historico de nav
                    Singleton.getInstance().setBundleSearchResult(list);
                    //fragHelper.setBundleSearchResult(list);
                    //gBundle.putSerializable("bundle_searchResult", list);

                    Singleton.getInstance().setTermo(searchTerm);
                    //fragHelper.setTermo(searchTerm);
                    //gBundle.putString("termo", searchTerm);

                    Singleton.getInstance().setQtd(len);
                    //fragHelper.setQtd(len);
                    //gBundle.putInt("qtd", len);

                    Singleton.getInstance().trocaFrag(fragmentSearchResult);
                    //fragHelper.trocaFrag(fragmentSearchResult);
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
            case R.id.action_settings:
                return true;
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

            //gBundle.putInt("count_fragments", count);
            Singleton.getInstance().trocaFrag(fragmentHome);

            //fragHelper.trocaFrag(fragmentHome);
            //insereBundleETrocaOFragment(fragmentHome);

        } else {
            ActionBar action = getSupportActionBar();
            
            escondeAbas(layoutParams);

            action.setDisplayShowCustomEnabled(true); //enable it to display custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
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

    private int count = 0;

    @Override
    public void onBackPressed() {
        //ViewParent viewParent = allTabs.getParent();
        //TabLayout.Tab tab = new TabLayout.Tab(viewParent);
        //int selectTab = allTabs.getSelectedTabPosition();
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        } else {
            int qtd = Singleton.getInstance().getHistoricoSize();

            if (qtd == 1){
                Singleton.getInstance().popBackStackk();
                super.onBackPressed();
            } else{

                String ultimoFragment = Singleton.getInstance().getNomeUltimoFragment();
                String penultimaFragment = Singleton.getInstance().getNomePenultimoFragment();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) allTabs.getLayoutParams();
                Singleton.getInstance().popBackStackk();
                if (ultimoFragment.equals("FragmentHome") && penultimaFragment.equals("FragmentMyListt")) {
                    allTabs.getTabAt(1).select();
                } else if (ultimoFragment.equals("FragmentMyListt") && penultimaFragment.equals("FragmentHome")) {
                    allTabs.getTabAt(0).select();//ok
                } else if (ultimoFragment.equals("FragmentHome") && penultimaFragment.equals("FragmentSearchResult")) {
                    allTabs.getTabAt(0).select();
                    revelaAbas(layoutParams);
                } else if (ultimoFragment.equals("FragmentMyListt") && penultimaFragment.equals("FragmentSearchResult")) {
                    allTabs.getTabAt(1).select();
                    revelaAbas(layoutParams);
                } else if (ultimoFragment.equals("FragmentMovie") && penultimaFragment.equals("FragmentHome")){
                    allTabs.getTabAt(0);
                    revelaAbas(layoutParams);
                }

                //gBundle.putInt("count_fragments", count);
            }
        }
    }

    public TabLayout getAllTabs(){
        return allTabs;
    }
}
