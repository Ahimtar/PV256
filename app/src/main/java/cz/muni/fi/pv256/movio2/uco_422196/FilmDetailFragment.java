package cz.muni.fi.pv256.movio2.uco_422196;

import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Ja on 4.12.2017.
 */

public class FilmDetailFragment extends Fragment {

    public static final String TAG = FilmDetailFragment.class.getSimpleName();
    private static final String ARGS_FILM = "args_film";

    private Context mContext;
    private Film mFilm;
    private SQLiteDatabase mDatabase;
    private FilmManager mFilmManager;
    private FilmDbHelper mDbHelper;
    private FloatingActionButton floatingActionButton;

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
        mDbHelper = new FilmDbHelper(getActivity());
        mDatabase = mDbHelper.getWritableDatabase();
        mFilmManager = new FilmManager(mDatabase);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabase.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView titleTv = (TextView) view.findViewById(R.id.detail_film);
        TextView popularityTv = (TextView) view.findViewById(R.id.detail_film_popularity);
        TextView dateTv = (TextView) view.findViewById(R.id.detail_film_date);
        TextView descTv = (TextView) view.findViewById(R.id.detail_film_desc);
        ImageView imageView = (ImageView) view.findViewById(R.id.detail_icon);
        ImageView imageSmallView = (ImageView) view.findViewById(R.id.detail_small_icon);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.detail_layout);

        if (mFilm != null) {
            relativeLayout.setVisibility(View.VISIBLE);
            titleTv.setText(mFilm.getTitle());
            popularityTv.setText(String.valueOf(mFilm.getPopularity()));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String dateString = formatter.format(mFilm.getReleaseDate());
            dateTv.setText(dateString);
            descTv.setText(mFilm.getDescription());
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mFilm.getCoverPath()).into(imageView);
            Picasso.with(mContext).load("https://image.tmdb.org/t/p/w500/" + mFilm.getSmallPath()).into(imageSmallView);
            setFloatingActionButton(view);
        }
        else{
            relativeLayout.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void setFloatingActionButton(View view) {

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_favourite);

        if (mFilmManager.containsId(mFilm.getId())) {
            floatingActionButton.setImageResource(R.drawable.remove_favourites);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFilmManager.containsId(mFilm.getId())) {
                    mFilmManager.removeFilm(mFilm);
                    Toast.makeText(getActivity(), mFilm.getTitle() + " removed from database.", Toast.LENGTH_LONG).show();
                    floatingActionButton.setImageResource(R.drawable.add_favourites);
                } else {
                    mFilmManager.createFilm(mFilm);
                    Toast.makeText(getActivity(), mFilm.getTitle() + " added to database.", Toast.LENGTH_LONG).show();
                    floatingActionButton.setImageResource(R.drawable.remove_favourites);
                }
                if (getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_list) != null) {
                    ((FilmListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_list)).updateData();
                }
            }
        });
    }
}
