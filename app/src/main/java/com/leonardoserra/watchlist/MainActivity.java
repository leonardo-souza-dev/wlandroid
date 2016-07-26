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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

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
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;
    private FragmentSearchResult fragmentSearchResult;
    private TabLayout allTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //String nomeApp = getResources().getString(R.string.app_name);
        //getSupportActionBar().setTitle(nomeApp);

        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        try {
            gUser = new User();

            sp = getPreferences(MODE_PRIVATE);
            gHash = sp.getString("wl_user_hash", null);

            Message msgCreateUser = new ApiHelper(this).createuser(gHash);

            if (msgCreateUser.getSucess()) {
                Boolean exists = Boolean.parseBoolean(msgCreateUser.getObject("exists"));

                if (!exists) {
                    gHash = msgCreateUser.getObject("hash");
                    e.putString("wl_user_hash", gHash);
                    e.commit();
                }

                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            gUser.setHash(gHash);


            return;

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        TextView txtHash = (TextView) findViewById(R.id.txtHash);
        txtHash.setText(gHash);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(fragmentOne, null);
                break;
            case 1 :
                replaceFragment(fragmentTwo, null);
                break;
        }
    }

    public void replaceFragment(Fragment fragment, Bundle b) {
        if (b != null) {
            fragment.setArguments(b);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void bindWidgetsWithAnEvent() {

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
    }

    private void setupTabLayout(){
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();
        allTabs.addTab(allTabs.newTab().setText("Home"), true);
        allTabs.addTab(allTabs.newTab().setText("MyListt"));
    }



    public void search() {
        fragmentSearchResult = new FragmentSearchResult();
        //obtem ter_mo da busca
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

                    Bundle b = new Bundle();
                    b.putSerializable("bundle_searchResult", list);
                    b.putString("termo", searchTerm);
                    b.putInt("qtd", len);
                    replaceFragment(fragmentSearchResult, b);

                    /*Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                    intent.putExtra("bundle_searchResult", list);
                    intent.putExtra("termo", searchTerm);
                    intent.putExtra("qtd", len);
                    startActivity(intent);*/
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

            //fonte: https://icons8.com/web-app/for/androidL/user
        /*    case R.id.action_user:
                Intent user = new Intent(this, UserActivity.class);
                startActivity(user);
                return true;
*/
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open
            allTabs.setVisibility(View.VISIBLE);

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

            isSearchOpened = false;
            replaceFragment(fragmentOne,null);
        } else { //open the search entry
            allTabs.setVisibility(View.INVISIBLE);

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        search();
                        return true;
                    }
                    return false;
                }
            });

            edtSeach.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Perform action on key press
                        //Toast.makeText(MainActivity.this, "foi", Toast.LENGTH_SHORT).show();
                        search();
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

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }

    private void doSearch() {
        //
    }
}
