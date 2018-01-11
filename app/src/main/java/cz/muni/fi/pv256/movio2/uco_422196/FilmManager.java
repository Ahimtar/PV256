package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ja on 8.1.2018.
 */

public class FilmManager {
    public static final int COL_FILM_ID = 0;
    public static final int COL_FILM_RELEASE_DATE = 1;
    public static final int COL_FILM_COVER_PATH = 2;
    public static final int COL_FILM_TITLE = 3;
    public static final int COL_FILM_SMALL_PATH = 4;
    public static final int COL_FILM_POPULARITY = 5;
    public static final int COL_FILM_DESCRIPTION = 6;

    SQLiteDatabase mDatabase;

    private static final String[] FILM_COLUMNS = {
            FilmContract.FilmEntry._ID,
            FilmContract.FilmEntry.COLUMN_RELEASE_DATE,
            FilmContract.FilmEntry.COLUMN_COVER_PATH,
            FilmContract.FilmEntry.COLUMN_TITLE,
            FilmContract.FilmEntry.COLUMN_SMALL_PATH,
            FilmContract.FilmEntry.COLUMN_POPULARITY,
            FilmContract.FilmEntry.COLUMN_DESCRIPTION,
    };

    public FilmManager(SQLiteDatabase database) {
        mDatabase = database;
    }

    public boolean createFilm(Film film) {

        if (film == null) {
            throw new NullPointerException("Film can't be null");
        }
        if (film.getTitle() == null) {
            throw new IllegalStateException("Title can't be null");
        }
        if (film.getCoverPath() == null) {
            throw new IllegalStateException("Image path can't be null");
        }
        if (film.getSmallPath() == null) {
            throw new IllegalStateException("Backdrop path can't be null");
        }
        if (film.getDescription() == null) {
            throw new IllegalStateException("Description can't be null");
        }

        long result = mDatabase.insert(FilmContract.FilmEntry.TABLE_NAME, null, prepareFilmValues(film));
        return result != -1;
    }

    public List<Film> getFavouriteFilms() {
        Cursor cursor = mDatabase.query(FilmContract.FilmEntry.TABLE_NAME, FILM_COLUMNS, null, null, null, null, null);
        List<Film> films = new ArrayList();
        if (cursor != null){
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    films.add(getFilm(cursor));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return films;
    }

    private Film getFilm(Cursor cursor) {
        return new Film(
            cursor.getLong(COL_FILM_ID),
            FilmContract.getDateFromDb(cursor.getString(COL_FILM_RELEASE_DATE)),
            cursor.getString(COL_FILM_COVER_PATH),
            cursor.getString(COL_FILM_TITLE),
            cursor.getString(COL_FILM_SMALL_PATH),
            cursor.getFloat(COL_FILM_POPULARITY),
            cursor.getString(COL_FILM_DESCRIPTION)
        );
    }

    public boolean removeFilm(Film film) {
        if (film == null) {
            return false;
        }
        if (film.getId() == null) {
            throw new IllegalStateException("Film ID can't be null");
        }

        int result = mDatabase.delete(FilmContract.FilmEntry.TABLE_NAME, FilmContract.FilmEntry._ID + " = " + film.getId(), null);
        return result != 0;
    }

    public boolean containsId(Long id) {
        String Query = "Select * from " + FilmContract.FilmEntry.TABLE_NAME + " where " + FilmContract.FilmEntry._ID + " = " + id;
        Cursor cursor = mDatabase.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private ContentValues prepareFilmValues(Film film) {

        ContentValues values = new ContentValues();
        values.put(FilmContract.FilmEntry._ID, film.getId());
        values.put(FilmContract.FilmEntry.COLUMN_RELEASE_DATE, FilmContract.inputDateToDb(film.getReleaseDate()));
        values.put(FilmContract.FilmEntry.COLUMN_COVER_PATH, film.getCoverPath());
        values.put(FilmContract.FilmEntry.COLUMN_TITLE, film.getTitle());
        values.put(FilmContract.FilmEntry.COLUMN_SMALL_PATH, film.getSmallPath());
        values.put(FilmContract.FilmEntry.COLUMN_POPULARITY, film.getPopularity());
        values.put(FilmContract.FilmEntry.COLUMN_DESCRIPTION, film.getDescription());
        return values;
    }
}
