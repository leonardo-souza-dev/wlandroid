package com.leonardoserra.watchlist;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserActivity extends AppCompatActivity {

    //public static UserActivity instance;
    private FragmentHome fragmentHome;
    private FragmentMyListt fragmentMyListt;
    private TabLayout allTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //instance = this;
        getAllWidgets();
        bindWidgetsWithAnEvent();
        setupTabLayout();
    }

    //public static UserActivity getInstance() {
    //    return instance;
    //}

    private void getAllWidgets() {
        allTabs = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupTabLayout(){
        fragmentHome = new FragmentHome();
        fragmentMyListt = new FragmentMyListt();
        allTabs.addTab(allTabs.newTab().setText("Search"),true);
        allTabs.addTab(allTabs.newTab().setText("MyListt"));
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

    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(fragmentHome);
                break;
            case 1 :
                replaceFragment(fragmentMyListt);
                break;
        }
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}