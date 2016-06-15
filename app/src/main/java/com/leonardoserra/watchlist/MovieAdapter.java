package com.leonardoserra.watchlist;

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

import com.google.gson.Gson;

//
public final class MovieAdapter extends ArrayAdapter<Movie> {

    private final int movieItemLayoutResource;
    private final String gTerm;

    public MovieAdapter(final Context context, final int movieItemLayoutResource, String term) {
        super(context, 0);
        this.movieItemLayoutResource = movieItemLayoutResource;
        this.gTerm = term;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        final Movie entry = getItem(position);

        // Setting the title view is straightforward
        String title = entry.getName();
        String partOne = "";
        String partTwo = "";
        String termOriginal = "";

        if (title != null) {
            int p = title.toLowerCase().indexOf(gTerm.toLowerCase());
            termOriginal = title.substring(p, p + gTerm.length());
            partOne = title.substring(0, p);
            partTwo = title.substring(p + gTerm.length(), title.length());
        }

        viewHolder.titleView1.setText(partOne);
        viewHolder.titleView2.setText(termOriginal);
        viewHolder.titleView2.setTextColor(Color.GREEN);
        viewHolder.titleView3.setText(partTwo);

        // Setting image view is also simple
        //viewHolder.imageView.setImageResource(entry.getImage());

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callMovieActivity(entry);
            }
        });

        return view;
    }

    private void callMovieActivity(Movie movieEntry) {
        Intent intent = new Intent(getContext(), MovieActivity.class);

        Bundle b = new Bundle();
        b.putString("movieEntry", new Gson().toJson(movieEntry));
        intent.putExtras(b);

        getContext().startActivity(intent);
    }

    private View getWorkingView(final View convertView) {
        // The workingView is basically just the convertView re-used if possible
        // or inflated new if not possible
        View workingView = null;

        if(null == convertView) {
            final Context context = getContext();
            final LayoutInflater inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            workingView = inflater.inflate(movieItemLayoutResource, null);
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
            viewHolder.titleView1 = (TextView) workingView.findViewById(R.id.rowTextView1);
            viewHolder.titleView2 = (TextView) workingView.findViewById(R.id.rowTextView2);
            viewHolder.titleView3 = (TextView) workingView.findViewById(R.id.rowTextView3);
            viewHolder.addRemoveBtn = (Button)workingView.findViewById(R.id.btnAddRemove);
            //viewHolder.imageView = (ImageView) workingView.findViewById(R.id.fruit_entry_image);

            workingView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) tag;
        }

        return viewHolder;
    }

    private static class ViewHolder {
        public TextView titleView1;
        public TextView titleView2;
        public TextView titleView3;
        //public ImageView imageView;
        public Button addRemoveBtn;
    }
}
