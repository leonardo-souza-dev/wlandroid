package com.leonardoserra.watchlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leonardoserra.watchlist.Activities.FilmeActivity;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;

public final class MovieAdapter extends ArrayAdapter<MovieViewModel> {

    private final int gLayout;
    private final String gTerm;
    private Context gContext;
    private ImageLoader imgLoader;
    private String nomeActivityAnterior;

    public MovieAdapter(final Context context, final int lLayout, String term) {
        super(context, 0);

        gContext = context;
        switch (gContext.getClass().getSimpleName().toLowerCase()){
            case "mylisttactivity":
                nomeActivityAnterior = "MyListt";
                break;
            case "resultadobuscaactivity":
                nomeActivityAnterior = "Search";
                break;
        }
        gLayout = lLayout;
        gTerm = term;

        imgLoader = new ImageLoader(context);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final View view = getWorkingView(convertView);
        final MovieViewModel entry = getItem(position);

        if (gLayout == R.layout.simple_row) {
            final ViewHolderSimpleRow viewHolderSimpleRow = (ViewHolderSimpleRow) getViewHolder(view);
            setElements(viewHolderSimpleRow, entry);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vaiParaActivity(entry, position);
            }
        });

        return view;
    }

    private void vaiParaActivity(MovieViewModel fruitEntry, int position) {
        Activity origin = (Activity)gContext;
        Intent intent = new Intent(gContext, FilmeActivity.class);

        Bundle b = new Bundle();
        b.putString("filme2_titulo", fruitEntry.getName());
        b.putBoolean("filme2_estaNaMyListt", fruitEntry.getIsInMyList());
        b.putString("filme2_nomeArquivo", fruitEntry.getPoster());
        b.putString("filme2_filmeId", fruitEntry.get_id());
        b.putInt("filme2_position", position);
        b.putString("nomeActivityAnterior", nomeActivityAnterior);

        intent.putExtras(b);

        origin.startActivityForResult(intent, 13);
    }

    private void setElements(ViewHolderSimpleRow viewHolderSimpleRow, MovieViewModel pEntry) {
        // Setting the title view is straightforward
        String tituloFilme = pEntry.getName();
        String partOne = "";
        String partTwo = "";
        String termOriginal = "";

        if (!gTerm.equals("") && tituloFilme != null) {
            int p = tituloFilme.toLowerCase().indexOf(gTerm.toLowerCase());
            termOriginal = tituloFilme.substring(p, p + gTerm.length());
            partOne = tituloFilme.substring(0, p);
            partTwo = tituloFilme.substring(p + gTerm.length(), tituloFilme.length());
        }
        if (gTerm.equals("") && tituloFilme != null) {
            partOne = tituloFilme;
        }

        viewHolderSimpleRow.titleView1.setText(partOne);
        viewHolderSimpleRow.titleView1.setTextColor(Color.WHITE);
        viewHolderSimpleRow.titleView2.setText(termOriginal);
        viewHolderSimpleRow.titleView2.setTextColor(Color.GREEN);
        viewHolderSimpleRow.titleView3.setText(partTwo);
        viewHolderSimpleRow.titleView3.setTextColor(Color.WHITE);
        imgLoader.DisplayImage(Singleton.getInstance().obterUrlBasePoster(pEntry.getPoster()), viewHolderSimpleRow.imgFilmePoster);
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(gLayout, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewBase getViewHolder(final View workingView) {

        ViewBase viewBase = null;

        if (gLayout == R.layout.simple_row) {
            ViewHolderSimpleRow viewHolderSimpleRow = new ViewHolderSimpleRow();
            viewHolderSimpleRow.titleView1 = (TextView) workingView.findViewById(R.id.rowTextView1);
            viewHolderSimpleRow.titleView2 = (TextView) workingView.findViewById(R.id.rowTextView2);
            viewHolderSimpleRow.titleView3 = (TextView) workingView.findViewById(R.id.rowTextView3);
            viewHolderSimpleRow.addRemoveBtn = (Button)workingView.findViewById(R.id.btnAddRemove);
            viewHolderSimpleRow.imgFilmePoster = (ImageView) workingView.findViewById(R.id.imgFilmePoster);

            workingView.setTag(viewHolderSimpleRow);
            viewBase = viewHolderSimpleRow;
        }

        return viewBase;
    }

    private static class ViewHolderSimpleRow extends ViewBase {
        public TextView titleView1;
        public TextView titleView2;
        public TextView titleView3;
        public ImageView imgFilmePoster;
        public Button addRemoveBtn;
    }


    private static class ViewBase {

    }
}
