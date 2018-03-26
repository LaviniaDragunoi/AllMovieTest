package com.example.user.allmovietest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.CONTENT_URI;
import static com.example.user.allmovietest.data.FavoriteContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by Lavinia Dragunoi on 3/22/2018.
 */

public class MovieContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITE_ID = 101;
    private FavoriteDBHelper favoriteDBHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //matcher Uri for the all directory
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES, FAVORITES);
        //matcher Uri for single item
        uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITES + "/#", FAVORITE_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        favoriteDBHelper = new FavoriteDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = favoriteDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnedCursor;
        switch (match){
            case FAVORITES:
                returnedCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};
                returnedCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnedCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnedCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //Get access to the Favorite Movies DB to add new favorite to the list
        final SQLiteDatabase db = favoriteDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if(id>0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favoriteDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted;
        switch (match){
            case FAVORITE_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(TABLE_NAME, "movieId=?", new String[]{id});
                break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(moviesDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
