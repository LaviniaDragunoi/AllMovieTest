package com.example.user.allmovietest.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.user.allmovietest.R;
import com.example.user.allmovietest.movies.MovieObject;

import java.util.List;

import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.CONTENT_URI;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 */

public class ManageFavoritesUtils {

    public Context mContext;
    Cursor mCursor;


    public static ContentValues addMovieToFavoritesList(MovieObject favoriteMovie){

        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER, favoriteMovie.getMoviePoster());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, favoriteMovie.getOverview());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, favoriteMovie.getRating());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_VOUT_COUNT, favoriteMovie.getVoteCount());
        return cv;
    }


}
