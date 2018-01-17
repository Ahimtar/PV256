package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.Context;

import java.util.List;

/**
 * Created by Ja on 8.1.2018.
 */

public class SqlLiteDataLoader extends AbstractDataLoader<List<Film>> {
    private FilmManager mFilmManager;
    private String mGroupBy;
    private String mHaving;
    private String mOrderBy;
    private String mSelection;
    private String[] mSelectionArgs;

    public SqlLiteDataLoader(Context context, FilmManager filmManager, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        super(context);
        mFilmManager = filmManager;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mGroupBy = groupBy;
        mHaving = having;
        mOrderBy = orderBy;
    }

    @Override
    protected List<Film> buildList() {
        return mFilmManager.getFavouriteFilms();
    }

    public void create(Film film) {
        new InsertTask(this).execute(film);
    }

    public void delete(Film film) {
        new DeleteTask(this).execute(film);
    }

    private class InsertTask extends ContentChangingTask<Film, Void, Void> {
        InsertTask(SqlLiteDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Film... params) {
            mFilmManager.createFilm(params[0]);
            return (null);
        }
    }

    private class DeleteTask extends ContentChangingTask<Film, Void, Void> {
        DeleteTask(SqlLiteDataLoader loader) {
            super(loader);
        }

        @Override
        protected Void doInBackground(Film... params) {
            mFilmManager.removeFilm(params[0]);
            return (null);
        }
    }
}