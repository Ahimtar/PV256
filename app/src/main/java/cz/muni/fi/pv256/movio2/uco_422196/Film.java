package cz.muni.fi.pv256.movio2.uco_422196;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ja on 4.12.2017.
 */

public class Film implements Parcelable {
    private long mReleaseDate;
    private String mCoverPath;
    private String mDescription;
    private String mTitle;
    private String mSmallPath;
    private float mPopularity;

    public Film(long releaseDate, String coverPath, String title, String smallPath, float popularity, String description) {
        mReleaseDate = releaseDate;
        mCoverPath = coverPath;
        mTitle = title;
        mSmallPath = smallPath;
        mPopularity = popularity;
        mDescription = description;
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

    public String getSmallPath() {
        return mSmallPath;
    }

    public void setSmallPath(String smallPath) {
        mSmallPath = smallPath;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public String getDescription() { return mDescription; }

    public void setDescription(String description) { mDescription = description; }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Film(Parcel in) {
        mReleaseDate = in.readLong();
        mCoverPath = in.readString();
        mTitle = in.readString();
        mSmallPath = in.readString();
        mPopularity = in.readFloat();
        mDescription = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mReleaseDate);
        dest.writeString(mCoverPath);
        dest.writeString(mTitle);
        dest.writeString(mSmallPath);
        dest.writeFloat(mPopularity);
        dest.writeString(mDescription);
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
