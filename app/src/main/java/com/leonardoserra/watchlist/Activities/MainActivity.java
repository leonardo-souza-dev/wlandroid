package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.R;

public class MainActivity extends AppCompatActivity {

    private SliderLayout carrossel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializaSingleton();

        obterCarrossel();

        criarOuObterUsuario();

        configuraActionbar();

        //setarBanner();
    }

    private void criarOuObterUsuario() {
        Singleton.getInstance().getWLService().criarOuObterUsuario("");
    }

    private void inicializaSingleton() {
        Singleton.getInstance(getBaseContext());//INIT CUSTOM
    }

    private void obterCarrossel() {
        carrossel = (SliderLayout)findViewById(R.id.slider);

        String[] urls = Singleton.getInstance().getWLService().obterItensCarrossel();

        for(String url : urls){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView.description("").image(url).setScaleType(BaseSliderView.ScaleType.CenterCrop);
            carrossel.addSlider(textSliderView);
        }
        carrossel.setDuration(8100);
        carrossel.setPresetTransformer(5);
        carrossel.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
    }

    @Override
     protected void onStop() {
        carrossel.stopAutoCycle();
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
