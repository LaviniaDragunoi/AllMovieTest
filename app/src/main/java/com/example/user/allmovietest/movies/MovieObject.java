package com.example.user.allmovietest.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lavinia Dragunoi on 2/21/2018.
 * This class will create the movie object and will store all the displayed variables.
 */

public class MovieObject implements Parcelable {

    public static final Creator<MovieObject> CREATOR = new Creator<MovieObject>() {
        @Override
        public MovieObject createFromParcel(Parcel in) {
            return new MovieObject(in);
        }

        @Override
        public MovieObject[] newArray(int size) {
            return new MovieObject[size];
        }
    };
    //This variable will store the original title of the movie object
    private String mOriginalTitle;
    //This variable will store address for the movie poster image
    private String mMoviePoster;
    //This variable will store a plot synopsis/overview of the movie
    private String mOverview;
    //This variable will store the vote average of the movie, the rating of the movie
    private double mRating;
    //This variable wil store the popularity of the movie
    private double mPopularity;
    //This variable will store the release date of the movie;
    private String mReleaseDate;
    //This variable will store the number of counted votes
    private int mVoteCount;
    //This variable will store the movie's ID
    private int mMovieId;

    /**
     * Construct a new {@link MovieObject} object
     *
     * @param originalTitle stores the original title of the movie, it is a String
     * @param moviePoster   stores the string with poster's movie,it is a String
     * @param overview      stores the movie's overview,it is a String
     * @param rating        stores the movie's rating, it is a double
     * @param popularity    stores the movie's popularity, it is a double
     * @param releaseDate   stores the movie's release date, it is a String
     * @param voteCount     stores the movie's vote count, it is an int
     * @param movieId       stores the movie's ID, it is an int
     */
    public MovieObject(String originalTitle, String moviePoster, String overview, double rating,
                       double popularity, String releaseDate, int voteCount, int movieId) {
        mOriginalTitle = originalTitle;
        mMoviePoster = moviePoster;
        mOverview = overview;
        mRating = rating;
        mPopularity = popularity;
        mReleaseDate = releaseDate;
        mVoteCount = voteCount;
        mMovieId = movieId;
    }

    protected MovieObject(Parcel in) {
        mOriginalTitle = in.readString();
        mMoviePoster = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        mPopularity = in.readDouble();
        mReleaseDate = in.readString();
        mVoteCount = in.readInt();
        mMovieId = in.readInt();
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getMoviePoster() {
        return mMoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        mMoviePoster = moviePoster;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = rating;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) { mMovieId = movieId; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mMoviePoster);
        dest.writeString(mOverview);
        dest.writeDouble(mRating);
        dest.writeDouble(mPopularity);
        dest.writeString(mReleaseDate);
        dest.writeInt(mVoteCount);
        dest.writeInt(mMovieId);
    }
}
