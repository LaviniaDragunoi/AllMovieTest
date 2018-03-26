package com.example.user.allmovietest.data;

import android.annotation.SuppressLint;
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
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.TABLE_NAME;
import static java.lang.String.valueOf;
import static java.security.AccessController.getContext;

/**
 * Created by Lavinia Dragunoi on 3/20/2018.
 */

public class ManageFavoritesUtils extends ContentResolver {

    public ManageFavoritesUtils(Context context) {
        super(context);
    }

    public static ContentValues addMovieToFavoritesList(MovieObject favoriteMovie){

        ContentValues cv = new ContentValues();
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_POSTER, favoriteMovie.getMoviePoster());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, favoriteMovie.getOverview());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RATING, favoriteMovie.getRating());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, favoriteMovie.getReleaseDate());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_VOUT_COUNT, favoriteMovie.getVoteCount());
        cv.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID, favoriteMovie.getMovieId());
        return cv;
    }

    public static boolean isAmongFavorites(Context context, int movieId){
        String selection = FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {""};
        boolean isFavorite = false;
        selectionArgs[0] = valueOf(movieId);
        Cursor cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        if(cursor.getCount() > 0) {
            isFavorite = true;
        }else {
            isFavorite = false;
        }
        return isFavorite;
        }



   public static int removeFromFavorite(Context context,int movieId){
       Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
       Uri removeUri = uri.buildUpon().appendPath(valueOf(movieId)).build();
       return context.getContentResolver().delete(removeUri, null, null);
    }

    public static boolean isDbEmpty(Context context, SQLiteDatabase db){
       @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
       boolean hasRaw;
       if(cursor.moveToFirst()){
           hasRaw = true;
       }else {
           hasRaw = false;
       }
       return hasRaw;
    }


}