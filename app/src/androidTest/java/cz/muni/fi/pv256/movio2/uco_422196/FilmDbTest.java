package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ja on 9.1.2018.
 */

public class FilmDbTest{

    private SQLiteDatabase mDatabase;
    private FilmManager mFilmManager;
    private FilmDbHelper mDbHelper;
    private Context mContext;

    @Before
    public void before() {
        mContext = InstrumentationRegistry.getTargetContext();
        FilmDbHelper.DATABASE_NAME = "test.db";
        mDbHelper = new FilmDbHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
        mFilmManager = new FilmManager(mDatabase);
    }

    @After
    public void after() {
        mDatabase.delete(FilmContract.FilmEntry.TABLE_NAME, null, null);
        mDatabase.close();
    }

    @Test
    public void create_Films() {
        Film film1 = new Film(1L, 11111111L, "cover", "Title1", "small", 2f, "Desc");
        Film film2 = new Film(2L, 11112211L, "cover2", "Title2", "small2", 3f, "Desc2");
        Film film3 = new Film(3L, 11112212L, "cover3", "Title3", "small3", 6f, "Desc3");

        mFilmManager.createFilm(film1);
        mFilmManager.createFilm(film2);
        mFilmManager.createFilm(film3);

        Assert.assertEquals(3, mFilmManager.getFavouriteFilms().size());
    }

    @Test
    public void delete_Created_Film() {
        Film film4 = new Film(4L, 11112111L, "cover", "Title4", "small4", 4f, "Desc");

        mFilmManager.createFilm(film4);
        mFilmManager.removeFilm(film4);

        Assert.assertEquals(0, mFilmManager.getFavouriteFilms().size());
    }

}
