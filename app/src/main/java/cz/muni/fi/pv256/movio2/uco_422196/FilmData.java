package cz.muni.fi.pv256.movio2.uco_422196;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ja on 28.12.2017.
 */

public class FilmData {

    private static FilmData sInstance;
    private ArrayList<Film> mFilmList = new ArrayList<>();

    private FilmData() {
        initFilmList();
    }

    public static FilmData getInstance() {
        if (sInstance == null) {
            sInstance = new FilmData();
        }
        return sInstance;
    }

    private void initFilmList() {

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));

        cal.set(2015, 07, 10);
        mFilmList.add(new Film(cal.getTimeInMillis(), "minions", "Minions", "minions_back", 7.4f));
        cal.set(2016, 01, 23);
        mFilmList.add(new Film(cal.getTimeInMillis(), "sleight", "Sleight", "sleight_back", 6.7f));
        cal.set(2017, 11, 10);
        mFilmList.add(new Film(cal.getTimeInMillis(), "justice_league", "Justice League", "justice_league_back", 5.7f));
    }

    public ArrayList<Film> getFilmList() {
        return mFilmList;
    }

    public void setFilmList(ArrayList<Film> filmList) {
        mFilmList = filmList;
    }

}
