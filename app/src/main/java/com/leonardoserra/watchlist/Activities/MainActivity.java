package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.R;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);


        DefaultSliderView textSliderView = new DefaultSliderView(this);
        textSliderView
                .description("Game of Thrones")
                .image("http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        DefaultSliderView textSliderView2 = new DefaultSliderView(this);
        textSliderView2
                .description("Hannibal")
                .image("http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");

        mDemoSlider.addSlider(textSliderView);
        mDemoSlider.addSlider(textSliderView2);

        Singleton.getInstance(getBaseContext());//INIT CUSTOM
        Singleton.getInstance().getWLService().criarOuObterUsuario("");

        configuraActionbar();

        //setarBanner();
    }

    @Override
     protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    public void busca(View view) {
        try {
            EditText termoTextView = (EditText) findViewById(R.id.edtSearchAMovie);
            String termoDaBusca = "";

            if (!termoTextView.getText().toString().equals(""))
                termoDaBusca = termoTextView.getText().toString();
            else
                return;

            Intent intent = new Intent(this, ResultadoBuscaActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("resultadodabusca_termo", termoDaBusca);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch(Exception ex) {
            Singleton.getInstance().getHelper().enviarLogException(ex);
            ex.printStackTrace();
        }
    }

    private void configuraActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //titulo
        String titulo = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);
        getSupportActionBar().setTitle(titulo);

        //desabilita lupa
        ImageView imgLupa = (ImageView) toolbar.findViewById(R.id.imgLupa);
        imgLupa.setEnabled(false);

        //habilita my watchlistt
        ImageView imgLista = (ImageView) toolbar.findViewById(R.id.imgLista);
        imgLista.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mylistt:
                Intent intentMyListt = new Intent(this, MyWatchListt.class);
                startActivity(intentMyListt);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void vaiParaMyWatchListt(View view){
        Intent intent = new Intent(this, MyWatchListt.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
