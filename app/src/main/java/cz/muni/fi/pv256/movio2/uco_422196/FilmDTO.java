package cz.muni.fi.pv256.movio2.uco_422196;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ja on 6.1.2018.
 */

public class FilmDTO implements Serializable {
    @SerializedName("id")
    private String mId;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("poster_path")
    private String mCoverPath;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("backdrop_path")
    private String mSmallPath;
    @SerializedName("vote_average")
    private String mPopularity;
    @SerializedName("overview")
    private String mDescription;

    public FilmDTO(String id, String releaseDate, String coverPath, String title, String smallPath, String popularity, String description) {
        mId = id;
        mReleaseDate = releaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mSmallPath = smallPath;
        mPopularity = popularity;
        mDescription = description;
    }

    public String getId() {
        return mId;
    }

    public Long getIdAsLong() {
        return Long.parseLong(getId());
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public long getReleaseDateAsLong() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try {
            Log.d(getTitle(), getReleaseDate());
            date = formatter.parse(getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSmallPath() {
        return mSmallPath;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public Float getPopularityAsFloat() {
        return Float.parseFloat(getPopularity());
    }

    public String getDescription() {
        return mDescription;
    }
}
