package com.leonardoserra.watchlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.Models.MovieViewModel;

import java.util.ArrayList;

public class MyListtActivity extends AppCompatActivity {

    private Bundle bundle;
    private ListView listView;

    private MovieAdapter movieAdapter;
    private String hash;
    private Toolbar toolbar;
    private ArrayList<MovieViewModel> lista;
    private TextView msg;
    private Button btnVaiParaBusca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listt);

        msg = (TextView)findViewById(R.id.txtMsg);
        btnVaiParaBusca = (Button)findViewById(R.id.btnVaiParaBusca);

        bundle = getIntent().getExtras();
        listView = (ListView) findViewById(R.id.listViewMyListt);

        movieAdapter = new MovieAdapter(this, R.layout.simple_row, "");

        listView.setAdapter(movieAdapter);

        lista = Singleton.getInstance().getMyListt();

        if (lista.size() > 0){

            for (final MovieViewModel entry : lista) {
                movieAdapter.add(entry);
            }

            msg.setEnabled(false);
            btnVaiParaBusca.setEnabled(false);

        }else {

            msg.setText(getResources().getString(R.string.there_is_no_movies));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(24, 200, 24, 24);
            msg.setLayoutParams(params);

            btnVaiParaBusca.setText(getResources().getString(R.string.search_movies));
        }

        configuraActionbar();

        Log.d("nav", ">MY_LISTT");
    }

    public void vaiParaBusca(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle b = data.getExtras();

        for(int i = 0; i < lista.size(); i++){
            if (lista.get(i).get_id().equals(b.getString("filme_filmeId"))){
                Boolean esta = b.getBoolean("filme_estaNaMyListt");
                lista.get(i).setIsInMyList(esta);
            }
        }
    }

    private void configuraActionbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarra);
        setSupportActionBar(toolbar);

        String nomeApp = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);

        getSupportActionBar().setTitle(nomeApp);

        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.colorTextTitle)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
    }
}
