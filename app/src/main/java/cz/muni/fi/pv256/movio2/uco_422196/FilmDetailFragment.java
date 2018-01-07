package cz.muni.fi.pv256.movio2.uco_422196;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Ja on 4.12.2017.
 */

public class FilmDetailFragment extends Fragment {

    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";

    private Context mContext;
    private Film mFilm;

    public static FilmDetailFragment newInstance(Film film) {
        FilmDetailFragment fragment = new FilmDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_FILM, film);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            mFilm = args.getParcelable(ARGS_FILM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleTv = (TextView) view.findViewById(R.id.detail_film);
        TextView titleLowTv = (TextView) view.findViewById(R.id.detail_film_low);
        TextView descTv = (TextView) view.findViewById(R.id.detail_film_desc);
        ImageView imageView = (ImageView) view.findViewById(R.id.detail_icon);

        if (mFilm != null) {
            titleTv.setText(mFilm.getTitle());
            titleLowTv.setText(mFilm.getCoverPath());
            //int coverId = mContext.getResources().getIdentifier(mFilm.getCoverPath(), "drawable", mContext.getPackageName());
            //Drawable cover = mContext.getResources().getDrawable(coverId);
            //imageView.setImageDrawable(cover);
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mFilm.getCoverPath()).into(imageView);
            
        }

        return view;
    }
}
