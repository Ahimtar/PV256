package cz.muni.fi.pv256.movio2.uco_422196;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ja on 4.12.2017.
 */

public class Film implements Parcelable {
    private long mReleaseDate;
    private String mCoverPath;
    private String mTitle;
    private String mBackdrop;
    private float mPopularity;

    public Film(long releaseDate, String coverPath, String title, String backdrop, float popularity) {
        mReleaseDate = releaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mBackdrop = backdrop;
        mPopularity = popularity;
    }

    public long getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(long releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getCoverPath() {
        return mCoverPath;
    }

    public void setCoverPath(String coverPath) {
        mCoverPath = coverPath;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public void setBackdrop(String backdrop) {
        mBackdrop = backdrop;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Film(Parcel in) {
        mReleaseDate = in.readLong();
        mCoverPath = in.readString();
        mTitle = in.readString();
        mBackdrop = in.readString();
        mPopularity = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mReleaseDate);
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeString(mBackdrop);
        dest.writeFloat(mPopularity);
    }

    public static final Parcelable.Creator<Film> CREATOR = new Parcelable.Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };
}
