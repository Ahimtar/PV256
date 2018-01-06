package cz.muni.fi.pv256.movio2.uco_422196;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ja on 28.12.2017.
 */

public class FilmData {

    private static FilmData sInstance;
    private ArrayList<Film> mFilmNewList;
    private ArrayList<Film> mFilmPopularList;
    private Film film1;
    private Film film2;
    private Film film3;

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

        mFilmNewList = new ArrayList<>();
        mFilmPopularList = new ArrayList<>();

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));

        cal.set(2015, 07, 10);
        film1 = new Film(cal.getTimeInMillis(), "minions", "Minions", "minions_small", 7.4f);
        cal.set(2016, 01, 23);
        film2 = new Film(cal.getTimeInMillis(), "sleight", "Sleight", "sleight_small", 6.7f);
        cal.set(2017, 11, 10);
        film3 = new Film(cal.getTimeInMillis(), "justice_league", "Justice League", "justice_league_small", 5.4f);

        mFilmNewList.add(film3);
        mFilmNewList.add(film2);
        mFilmNewList.add(film1);
        mFilmPopularList.add(film1);
        mFilmPopularList.add(film2);
        mFilmPopularList.add(film3);
    }

    public ArrayList<Film> getFilmNewList() {
        return mFilmNewList;
    }

    public ArrayList<Film> getFilmPopularList() {
        return mFilmPopularList;
    }

    public void setFilmNewList(ArrayList<Film> filmList) {
        mFilmNewList = filmList;
    }

    public void setFilmPopularList(ArrayList<Film> filmList) {
        mFilmPopularList = filmList;
    }

}
