package com.leonardoserra.watchlist.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leonardoserra.watchlist.Singleton;
import com.leonardoserra.watchlist.R;

public class MainActivity extends AppCompatActivity {

    //private String hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Singleton.getInstance(getBaseContext());//INIT CUSTOM
        Singleton.getInstance().getWLService().criarOuObterUsuario("");

        configuraActionbar();
        //setarBanner();
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
            //bundle.putString("hash", hash);
            intent.putExtras(bundle);
            startActivity(intent);

        } catch(Exception ex) {
            Singleton.getInstance().enviarLogException(ex);
            ex.printStackTrace();
        }
    }

    private void configuraActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarra);
        setSupportActionBar(toolbar);

        String titulo = getResources().getString(R.string.app_name) == null ? "WatchListt" : getResources().getString(R.string.app_name);

        getSupportActionBar().setTitle(titulo);

        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(this.getResources().getColor(R.color.cinze)),
                0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //text.setSpan(new AbsoluteSizeSpan(34), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new RelativeSizeSpan(1.2f), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new TextAppearanceSpan(this, R.style.FonteBold),
                0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        getSupportActionBar().setTitle(text);

        //titulo central
        TextView txtTitulo = (TextView) toolbar.findViewById(R.id.txtTituloToolbar);
        txtTitulo.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mylistt:
                Intent intentMyListt = new Intent(this, MyListtActivity.class);
                startActivity(intentMyListt);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void vaiParaMyListt(View view){
        Intent intentMyListt = new Intent(this, MyListtActivity.class);
        //Bundle b = new Bundle();
        //b.putString("hash", hash);
        //intentMyListt.putExtras(b);

        startActivity(intentMyListt);
    }
}
