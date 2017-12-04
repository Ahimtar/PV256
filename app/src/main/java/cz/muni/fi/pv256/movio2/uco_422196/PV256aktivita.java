package cz.muni.fi.pv256.movio2.uco_422196;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

public class PV256aktivita extends AppCompatActivity
        implements FilmListFragment.OnFilmSelectListener {

        private boolean mTwoPane;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.news_articles);

            setContentView(R.layout.activity_pv256aktivita);
            if (findViewById(R.id.film_detail_container) != null) {
                mTwoPane = true;
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.film_detail_container, new FilmDetailFragment(), FilmDetailFragment.TAG)
                            .commit();
                }
            } else {
                mTwoPane = false;
                getSupportActionBar().setElevation(0f);
            }
        }


    @Override
    public void onFilmSelect(Film film) {
        if (mTwoPane) {
            FragmentManager fm = getSupportFragmentManager();
            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            fm.beginTransaction()
                    .replace(R.id.film_detail_container, fragment, FilmDetailFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.EXTRAFILM, film);
            startActivity(intent);
        }
    }

}