package com.leonardoserra.watchlist.Helpers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.leonardoserra.watchlist.Models.MovieViewModel;

import java.util.ArrayList;

public class FragWrapper {

    public static class FragHelper{

        private FragmentManager fragmentManager;
        private int container;
        private FragHistorico fragHistorico;
        private Bundle bundle;

        public FragHelper(FragmentManager pFragmentManager, int pContainer){
            bundle = new Bundle();
            fragmentManager = pFragmentManager;
            container = pContainer;
            fragHistorico = new FragHistorico();
        }

        public void setBundleHash(String pHash) {
            bundle.putString("user_hash", pHash);
        }

        public String getBundleHash() {
            String r = bundle.getString("user_hash");
            return r;
        }

        /*public void setTermo(String pTermo) {
            bundle.putString("termo", pTermo);
        }*/

        /*public void setQtd(int pQtd) {
            bundle.putInt("qtd", pQtd);
        }*/

        /*public void setBundleSearchResult(ArrayList<MovieViewModel> pList) {
            bundle.putSerializable("bundle_searchResult", pList);
        }*/

        public void trocaFrag(Fragment fragment){
            fragment.setArguments(bundle);
            fragHistorico.addItem(fragment);

            FragmentTransaction ft = fragmentManager.beginTransaction();

            ft.replace(container, fragment, "");

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();

            fragmentManager.executePendingTransactions();
        }

    }

    private static class FragHistorico{
        private ArrayList<FragHistoricoItem> fragHistoricoItems;
        private int ordem = 0;

        public FragHistorico(){
            fragHistoricoItems = new ArrayList<>();
        }

        public void addItem(Fragment pFragment){
            FragHistoricoItem item = new FragHistoricoItem(pFragment);
            item.setOrdem(ordem);
            fragHistoricoItems.add(item);
            ordem++;
        }
    }

    private static class FragHistoricoItem{
        private Fragment fragment;
        private int ordem;

        public FragHistoricoItem(Fragment pFragment){
            fragment = pFragment;
        }

        public void setOrdem(int pOrdem){
            ordem = pOrdem;
        }
    }

}
