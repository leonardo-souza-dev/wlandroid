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
import com.leonardoserra.watchlist.ViewModels.MovieViewModel;
import com.squareup.picasso.Picasso;

public final class MovieAdapter extends ArrayAdapter<MovieViewModel> {

    private final int gLayout;
    private Context gContext;

    public MovieAdapter(final Context context, final int lLayout, String term) {
        super(context, 0);

        gContext = context;
        gLayout = lLayout;
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

    private void vaiParaActivity(MovieViewModel filmeViewModel, int position) {
        Activity origin = (Activity)gContext;
        Intent intent = new Intent(gContext, FilmeActivity.class);

        Bundle b = new Bundle();
        b.putString("filme2_titulo", filmeViewModel.getTitulo());
        b.putBoolean("filme2_estaNaMyListt", filmeViewModel.getIsInMyList());
        b.putString("filme2_nomeArquivo", filmeViewModel.getPoster());
        b.putString("filme2_filmeId", filmeViewModel.get_id());
        b.putInt("filme2_position", position);

        b.putString("filme2_urlPoster", filmeViewModel.getUrlPoster());

        intent.putExtras(b);

        origin.startActivityForResult(intent, 13);
    }

    private void setElements(ViewHolderSimpleRow viewHolderSimpleRow, MovieViewModel pEntry) {
        String titulo = pEntry.getTitulo();

        viewHolderSimpleRow.titleView1.setText(titulo);
        viewHolderSimpleRow.titleView1.setTextColor(Color.WHITE);

        String url = pEntry.getUrlPoster();
        Picasso.with(gContext).load(url)
                .placeholder(R.drawable.film_strip)
                .into(viewHolderSimpleRow.imgFilmePoster);
    }

    private View getWorkingView(final View convertView) {
        View workingView;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater =
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(gLayout, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolderSimpleRow getViewHolder(final View workingView) {

        ViewHolderSimpleRow viewBase = null;

        if (gLayout == R.layout.simple_row) {
            ViewHolderSimpleRow viewHolderSimpleRow = new ViewHolderSimpleRow();
            viewHolderSimpleRow.titleView1 = (TextView) workingView.findViewById(R.id.rowTextView1);
            viewHolderSimpleRow.addRemoveBtn = (Button)workingView.findViewById(R.id.btnAddRemove);
            viewHolderSimpleRow.imgFilmePoster = (ImageView) workingView.findViewById(R.id.imgFilmePoster);

            workingView.setTag(viewHolderSimpleRow);
            viewBase = viewHolderSimpleRow;
        }

        return viewBase;
    }

    private static class ViewHolderSimpleRow  {
        public TextView titleView1;
        public ImageView imgFilmePoster;
        public Button addRemoveBtn;
    }
}
