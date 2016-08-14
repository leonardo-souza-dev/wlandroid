package com.leonardoserra.watchlist;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leonardoserra.watchlist.Fragments.FragmentMovie;
import com.leonardoserra.watchlist.Helpers.Singleton;
import com.leonardoserra.watchlist.ImageCaching.ImageLoader;
import com.leonardoserra.watchlist.Models.MovieViewModel;

public final class MovieAdapter extends ArrayAdapter<MovieViewModel> {

    private final int gLayout;
    private final String gTerm;
    private Context gContext;
    private ImageLoader imgLoader;

    public MovieAdapter(final Context context, final int lLayout, String term) {
        super(context, 0);

        gContext = context;
        gLayout = lLayout;
        gTerm = term;

        imgLoader = new ImageLoader(getContext());
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final View view = getWorkingView(convertView);
        final MovieViewModel entry = getItem(position);

        if (gLayout == R.layout.simple_row) {
            final ViewHolderSimpleRow viewHolderSimpleRow = (ViewHolderSimpleRow) getViewHolder(view);
            setElements(viewHolderSimpleRow, entry);
        } else if (gLayout == R.layout.linha) {
            final ViewHolderLinha viewHolderLinha = (ViewHolderLinha) getViewHolder(view);
            setElementsLinha(viewHolderLinha, entry);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TabLayout tabLayout = ((MainActivity)gContext).getAllTabs();
                tabLayout.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                        tabLayout.getLayoutParams();
                layoutParams.weight = 1;
                callMovieFragmentSingleton(entry);
            }
        });

        return view;
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
        viewHolderSimpleRow.titleView2.setText(termOriginal);
        viewHolderSimpleRow.titleView2.setTextColor(Color.GREEN);
        viewHolderSimpleRow.titleView3.setText(partTwo);
        imgLoader.DisplayImage(Singleton.getInstance().getUrl(pEntry.getPoster()), viewHolderSimpleRow.imgFilmePoster);
    }

    private void setElementsLinha(ViewHolderLinha viewHolderLinha, MovieViewModel pEntry) {
        String titulo = pEntry.getName();
        String ano = pEntry.getAno();

        viewHolderLinha.txtTitulo.setText(titulo);
        viewHolderLinha.txtAno.setText(ano);
        imgLoader.DisplayImage(Singleton.getInstance().getUrl(pEntry.getPoster()), viewHolderLinha.imgPoster);
    }

    private void callMovieFragmentSingleton(MovieViewModel movieViewModel){
        Singleton.getInstance().setMovieViewModel(movieViewModel);
        Singleton.getInstance().trocaFrag(new FragmentMovie(), false);
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
        } else if (gLayout == R.layout.linha) {
            ViewHolderLinha viewHolderLinha = new ViewHolderLinha();
            viewHolderLinha.txtTitulo = (TextView) workingView.findViewById(R.id.txtTitulo);
            viewHolderLinha.txtAno = (TextView) workingView.findViewById(R.id.txtAno);
            viewHolderLinha.imgPoster = (ImageView) workingView.findViewById(R.id.imgPoster);

            workingView.setTag(viewHolderLinha);

            viewBase = viewHolderLinha;
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

    private static class ViewHolderLinha extends ViewBase {
        public TextView txtTitulo;
        public TextView txtAno;
        public ImageView imgPoster;
    }

    private static class ViewBase {

    }
}
