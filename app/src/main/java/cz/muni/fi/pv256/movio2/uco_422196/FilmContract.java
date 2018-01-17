package cz.muni.fi.pv256.movio2.uco_422196;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ja on 8.1.2018.
 */

public class FilmContract {
    public static final String CONTENT_AUTHORITY = "cz.muni.fi.pv256.movio2.uco_422196.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WORK_TIME = "films";

    public static String inputDateToDb(Long inputDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        return formatter.format(inputDate);
    }

    public static Long getDateFromDb(String outputDate){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            date = (Date)formatter.parse(outputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static final class FilmEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WORK_TIME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WORK_TIME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WORK_TIME;


        public static final String TABLE_NAME = "FilmTable";

        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_COVER_PATH = "cover_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_SMALL_PATH = "small_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_DESCRIPTION = "description";

        public static Uri buildFilmUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
