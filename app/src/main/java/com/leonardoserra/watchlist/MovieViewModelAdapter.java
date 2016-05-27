package com.leonardoserra.watchlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

//
public final class MovieViewModelAdapter extends ArrayAdapter<Filme> {

    private final int fruitItemLayoutResource;

    public MovieViewModelAdapter(final Context context, final int fruitItemLayoutResource) {
        super(context, 0);
        this.fruitItemLayoutResource = fruitItemLayoutResource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Filme entry = getItem(position);

        // Setting the title view is straightforward
        viewHolder.titleView.setText(entry.obterNome());

        // Setting image view is also simple
        //viewHolder.imageView.setImageResource(entry.getImage());

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callFruitActivity(entry);
            }
        });

        return view;
    }

    private void callFruitActivity(Filme fruitEntry) {
        Intent intent = new Intent(getContext(), DetalheFilmeActivity.class);

        Bundle b = new Bundle();
        b.putString("movieEntry", new Gson().toJson(fruitEntry));
        intent.putExtras(b);

        getContext().startActivity(intent);
        //MainActivity.fa.finish();
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView = null;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(fruitItemLayoutResource, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewHolder getViewHolder(final View workingView) {
        // The viewHolder allows us to avoid re-looking up view references
        // Since views are recycled, these references will never change
        final Object tag = workingView.getTag();
        ViewHolder viewHolder = null;

        if(null == tag || !(tag instanceof ViewHolder)) {
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) workingView.findViewById(R.id.rowTextView);
            viewHolder.addRemoveBtn = (Button)workingView.findViewById(R.id.btnAddRemove);
            //viewHolder.imageView = (ImageView) workingView.findViewById(R.id.fruit_entry_image);

            workingView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    private static class ViewHolder {
        public TextView titleView;
        public ImageView imageView;
        public Button addRemoveBtn;
    }
}
