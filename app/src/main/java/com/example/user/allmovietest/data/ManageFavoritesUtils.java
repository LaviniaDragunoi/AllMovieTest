package com.example.user.allmovietest.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.user.allmovietest.movies.MovieObject;

import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.CONTENT_URI;
import static java.lang.String.valueOf;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 * This class is used to make changes on the database, is an Utils class
 */
public class ManageFavoritesUtils extends ContentResolver {

    /**
     * This is the class constructor
     *
     * @param context
     */
    public ManageFavoritesUtils(Context context) {
        super(context);
    }

    /**
     * This method helps to add a movie to the favorites movie list
     *
     * @param favoriteMovie this object has all the movie's details that will be put in the database
     * @return the content values that will be used to populate the database
     */
    public static ContentValues addMovieToFavoritesList(MovieObject favoriteMovie) {

        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER, favoriteMovie.getMoviePoster());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, favoriteMovie.getOverview());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, favoriteMovie.getRating());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_COUNT, favoriteMovie.getVoteCount());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, favoriteMovie.getMovieId());
        return cv;
    }

    /**
     * This method will be used to find out if a movie is in the favorite movie's list
     *
     * @param context
     * @param movieId that will be used to query in the database for the specific movie
     * @return true if the movie is in the favorite list or false if is not
     */
    public static boolean isAmongFavorites(Context context, int movieId) {
        String selection = FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {""};
        boolean isFavorite = false;
        selectionArgs[0] = valueOf(movieId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if (cursor.getCount() > 0) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }
        return isFavorite;
    }

    /**
     * This method helps to remove from the favorite movies list a specific movie
     *
     * @param context
     * @param movieId that will be used to query in the database for the specific movie
     * @return the value of the raw deleted
     */
    public static int removeFromFavorite(Context context, int movieId) {
        Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
        Uri removeUri = uri.buildUpon().appendPath(valueOf(movieId)).build();
        return context.getContentResolver().delete(removeUri, null, null);
    }
}