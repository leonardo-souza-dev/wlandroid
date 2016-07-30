package com.leonardoserra.watchlist;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.leonardoserra.watchlist.Fragments.FragmentMovie;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.Models.MovieViewModel;

public final class MovieAdapter extends ArrayAdapter<MovieViewModel> {

    private final int gLayout;
    private final String gTerm;
    private Context gContext;
    private Fragment gF;
    private MovieViewModel gEntry;
    private ImageLoader imgLoader;
    private String gBasePosterUrl;

    public MovieAdapter(final Context context, final int lLayout, String term, Fragment f) {
        super(context, 0);

        gContext = context;
        this.gLayout = lLayout;
        this.gTerm = term;
        this.gF = f;

        imgLoader = new ImageLoader(getContext());
    }

    private String getUrl(){

        gBasePosterUrl = "http://10.0.2.2:8080/poster?p=" + gEntry.getPoster();
        if (!isEmulator()) {
            gBasePosterUrl = "http://192.168.1.5:8080/poster?p=" + gEntry.getPoster();
        }

        return gBasePosterUrl;
    }
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        // We need to get the best view (re-used if possible) and then
        // retrieve its corresponding ViewHolder, which optimizes lookup efficiency
        final View view = getWorkingView(convertView);
        gEntry = getItem(position);

        if (gLayout == R.layout.simple_row) {
            final ViewHolderSimpleRow viewHolderSimpleRow = (ViewHolderSimpleRow) getViewHolder(view);
            setElements(viewHolderSimpleRow);
        } else if (gLayout == R.layout.movie_thumb) {
            final ViewHolderThumb viewHolderSimpleRow = (ViewHolderThumb) getViewHolder(view);
            setElementsThumb(viewHolderSimpleRow);
        }


        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callMovieFragment(gEntry, gContext);
            }
        });

        return view;
    }

    private void setElements(ViewHolderSimpleRow viewHolderSimpleRow) {
        // Setting the title view is straightforward
        String tituloFilme = gEntry.getName();
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
        viewHolderSimpleRow.titleView2.setText(termOriginal);
        viewHolderSimpleRow.titleView2.setTextColor(Color.GREEN);
        viewHolderSimpleRow.titleView3.setText(partTwo);
        imgLoader.DisplayImage(getUrl(), viewHolderSimpleRow.imgFilmePoster);
    }

    private void setElementsThumb(ViewHolderThumb viewHolderThumb) {
        String tituloFilme = gEntry.getName();

        viewHolderThumb.txtTitulo.setText(tituloFilme);

        imgLoader.DisplayImage(getUrl(), viewHolderThumb.imgPosterThumb);
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
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

            workingView = inflater.inflate(gLayout, null);
        } else {
            workingView = convertView;
        }

        return workingView;
    }

    private ViewBase getViewHolder(final View workingView) {

        final Object tag = workingView.getTag();
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
        } else if (gLayout == R.layout.movie_thumb) {
            ViewHolderThumb viewHolderThumb = new ViewHolderThumb();
            viewHolderThumb.txtTitulo = (TextView) workingView.findViewById(R.id.txtTituloThumb);
            viewHolderThumb.imgPosterThumb = (ImageView) workingView.findViewById(R.id.imgPosterThumb);

            workingView.setTag(viewHolderThumb);

            viewBase = viewHolderThumb;
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

    private static class ViewHolderThumb extends ViewBase {
        public TextView txtTitulo;
        public ImageView imgPosterThumb;
    }

    private static class ViewBase {

    }
}
