package com.leonardoserra.watchlist;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView termoTextView;
    private String searchTerm;
    private User gUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nomeApp = getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(nomeApp);

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        String lHash;

        try {
            gUser = new User();

            sp = getPreferences(MODE_PRIVATE);
            lHash = sp.getString("wl_user_hash", null);

            Message msgCreateUser = new ApiHelper(this).createuser(lHash);

            if (msgCreateUser.getSucess()) {
                Boolean exists = Boolean.parseBoolean(msgCreateUser.getObject("exists"));

                if (!exists) {
                    lHash = msgCreateUser.getObject("hash");
                    e.putString("wl_user_hash", lHash);
                    e.commit();
                }

                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, msgCreateUser.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            gUser.setHash(lHash);
            TextView txtHash = (TextView) findViewById(R.id.txtHash);
            txtHash.setText(lHash);

            return;

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show();
        }
    }

    public void search(View view) {
        //obtem ter_mo da busca
        termoTextView = (TextView) findViewById(R.id.txtTerm);
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

                    Intent intent = new Intent(getBaseContext(), SearchResultActivity.class);
                    intent.putExtra("bundle_searchResult", list);
                    intent.putExtra("termo", searchTerm);
                    intent.putExtra("qtd", len);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, msg.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
