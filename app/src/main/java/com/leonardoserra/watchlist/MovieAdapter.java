package com.leonardoserra.watchlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

public final class MovieAdapter extends ArrayAdapter<MovieViewModel> {

    private final int movieItemLayoutResource;
    private final String gTerm;
    private Context gContext;
    //private Integer MOVIEACTION;
    private Fragment gF;

    public MovieAdapter(final Context context, final int movieItemLayoutResource, String term, Fragment f) {
        super(context, 0);
        gContext = context;
        this.movieItemLayoutResource = movieItemLayoutResource;
        this.gTerm = term;
        this.gF = f;
    }

    private MovieViewModel gEntry;

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        final ViewHolder viewHolder = getViewHolder(view);
        gEntry = getItem(position);

        // Setting the title view is straightforward
        String title = gEntry.getName();
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
                callMovieFragment(gEntry, gContext);
            }
        });

        return view;
    }

    private void callMovieFragment(MovieViewModel movieViewModelEntry, Context pContext) {
        FragmentMovie blankFragment = new FragmentMovie();
        Bundle b = new Bundle();
        b.putString("movieViewModelEntry", new Gson().toJson(movieViewModelEntry));
        blankFragment.setArguments(b);

        FragmentManager fm = ((FragmentActivity)pContext).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, blankFragment);

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    /* //TODO: IMPLEMENTAR
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MOVIEACTION) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("action_result");
                try {
                    JSONObject json = new JSONObject(result);
                    String movieId = json.getString("movie_id");
                    Boolean isInMyList = json.getBoolean("isInMyList");
                    if (gEntry.get_id().equals(movieId)) {
                        gEntry.setIsInMyList(isInMyList);
                    } else {
                        Toast.makeText(gContext, "algum erro aconteceu", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    */

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
