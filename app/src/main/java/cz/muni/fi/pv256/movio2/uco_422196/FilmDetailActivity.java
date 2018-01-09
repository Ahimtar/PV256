package cz.muni.fi.pv256.movio2.uco_422196;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by Ja on 4.12.2017.
 */

public class FilmDetailActivity extends AppCompatActivity {

    public static final String EXTRAFILM = "extrafilm";
    public static final String SWITCH = "switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Film film = getIntent().getParcelableExtra(EXTRAFILM);

            if(savedInstanceState == null){
            FragmentManager fm = getSupportFragmentManager();
            FilmDetailFragment fragment = (FilmDetailFragment) fm.findFragmentById(R.id.film_detail_container);

                if (fragment == null) {
                fragment = FilmDetailFragment.newInstance(film);
                fm.beginTransaction()
                        .add(R.id.film_detail_container, fragment)
                        .commit();
                }
            }
        }
}
